/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player;

import it.mbcraft.regiapn.player.command.media.StopMediaPlayerCommand;
import it.mbcraft.regiapn.player.data.dao.IConfigurationReloadListener;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.jobs.*;
import it.mbcraft.regiapn.player.media.MediaPlayerService;
import it.mbcraft.regiapn.player.media.PlayState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * This class contains all the major calls to other required components.
 * <p>
 * - start and stop
 * - modules update job
 * - player check job
 * - usb drive checking job
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class RegiaPNPlayer implements IConfigurationReloadListener {

    private static RegiaPNPlayer instance = null;

    private static final Logger logger = LogManager.getLogger(RegiaPNPlayer.class);

    private final Scheduler scheduler;
    private final List<TriggerKey> scheduledJobKeys = new ArrayList<>();

    private RegiaPNPlayer() {

        PlayerConfigurationDAO.getInstance().addConfigurationReloadListener(this);


        StdSchedulerFactory factory = new StdSchedulerFactory();
        Properties pt = new Properties();
        pt.setProperty("org.quartz.scheduler.instanceName", "RegiaPNPlayer_Scheduler");
        pt.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        pt.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        pt.setProperty("org.quartz.scheduler.batchTriggerAcquisitionMaxCount", "1");
        pt.setProperty("org.quartz.threadPool.threadCount", "3");

        try {
            factory.initialize(pt);
            scheduler = factory.getScheduler();
        } catch (SchedulerException ex) {
            logger.error("Error initializing the scheduler", ex);
            throw new IllegalStateException("Unable to initialize scheduler.");
        }

    }

    public static RegiaPNPlayer getInstance() {
        if (instance==null) instance = new RegiaPNPlayer();
        return instance;
    }

    @Override
    public void configurationReloaded() {
        removeJobs();
        initializeJobs();

        logger.info("RegiaPNPlayer scheduler jobs reloaded");
    }

    /**
     * Starts the scheduler
     */
    public void start() {

        initializeJobs();

        try {
            scheduler.start();
        } catch (SchedulerException ex) {
            logger.error("Unable to start the scheduler",ex);
        }
    }

    /**
     * Initialize the modules update job
     *
     */
    private void initializeUpdatesJob() {
        if (PlayerConfigurationDAO.hasFilesUpdateTime()) {


            JobDetail detail = UpdateJob.makeJobDetail();
            Trigger trigger = UpdateJob.makeTrigger(detail);

            try {
                logger.info("Initializing updates job ...");
                scheduler.scheduleJob(detail, trigger);
                scheduledJobKeys.add(trigger.getKey());
            } catch (SchedulerException ex) {
                logger.error("Unable to schedule job", ex);
            }

        } else {
            logger.info("Files update time missing, skipping update cron job ...");
        }
    }

    /**
     * Initialize the player checker job
     *
     */
    private void initializePlayerStatusCheckJob() {
        //start player
        JobDetail detail = MediaPlayerStatusCheckerJob.makeJobDetail();
        Trigger trigger = MediaPlayerStatusCheckerJob.makeTrigger(detail);

        try {
            logger.info("Initializing media player status checker job ...");
            scheduler.scheduleJob(detail, trigger);
            scheduledJobKeys.add(trigger.getKey());
        } catch (SchedulerException ex) {
            logger.error("Unable to schedule job", ex);
        }
    }

    /**
     * Initialize the usb drive checking job
     *
     */
    private void initializeUsbDriveCheckJob() {
        JobDetail detail = CheckUsbDriveForUpdatesJob.makeJobDetail();
        Trigger trigger = CheckUsbDriveForUpdatesJob.makeTrigger(detail);

        try {
            logger.info("Initializing usb drive check job ...");
            scheduler.scheduleJob(detail, trigger);
            scheduledJobKeys.add(trigger.getKey());
        } catch (SchedulerException ex) {
            logger.error("Unable to schedule job", ex);
        }
    }

    private void initializeFlagsCheckJob() {
        JobDetail detail = FlagCheckingJob.makeJobDetail();
        Trigger trigger = FlagCheckingJob.makeTrigger(detail);

        try {
            logger.info("Initializing flags check job ...");
            scheduler.scheduleJob(detail, trigger);
            scheduledJobKeys.add(trigger.getKey());
        } catch (SchedulerException ex) {
            logger.error("Unable to schedule job", ex);
        }
    }

    private void initializeConfigReloadJob() {
        JobDetail detail = CheckReloadConfigJob.makeJobDetail();
        Trigger trigger = CheckReloadConfigJob.makeTrigger(detail);

        try {
            logger.info("Initializing configs reload job ...");
            scheduler.scheduleJob(detail, trigger);
            scheduledJobKeys.add(trigger.getKey());
        } catch (SchedulerException ex) {
            logger.error("Unable to schedule job", ex);
        }
    }

    /**
     * Initializes all the jobs to execute periodically
     *
     */
    private void initializeJobs() {

        initializeFlagsCheckJob();
        initializeConfigReloadJob();
        initializeUsbDriveCheckJob();
        initializeUpdatesJob();
        initializePlayerStatusCheckJob();
    }

    /**
     * Removes all job triggers from the scheduler
     */
    private void removeJobs() {
        for (TriggerKey tk : scheduledJobKeys) {
            try {
                scheduler.unscheduleJob(tk);
            } catch (SchedulerException ex) {
                logger.error("Error removing job trigger from scheduler",ex);
            }
        }
        scheduledJobKeys.clear();
    }

    /**
     * Stops the scheduler
     */
    public void stop() {

        //remove all jobs from the scheduler
        removeJobs();

        StopMediaPlayerCommand stopMp = new StopMediaPlayerCommand();
        stopMp.execute();

        try {
            logger.info("Shutting down scheduler ...");
            scheduler.shutdown();
        } catch (SchedulerException ex) {
            logger.error("Error during shutdown of music player and server", ex);
        }
    }

    /**
     * Stampa a linea di comando lo status corrente.
     * (forse dovrei ritornarlo come stringa in modo da restituirlo alla socket ...)
     * javax.management ????
     */
    public void logStatus() {

        logger.info(Main.SOFTWARE_VENDOR + " " + Main.SOFTWARE_NAME + " " + Main.SOFTWARE_VERSION);
        logger.info("Service list :");
        String sch = " - Scheduler : ";
        try {
            if (scheduler != null && scheduler.isStarted()) {
                sch += "STARTED ";
                if (scheduler.isInStandbyMode())
                    sch += "STANDBY";
            } else
                sch += "NOT RUNNING";
        } catch (SchedulerException e) {
            sch += "EXCEPTION!!";
        }
        logger.info(sch);
        //scheduled job list
        for (TriggerKey tk : scheduledJobKeys) {
            logger.info("Scheduled job : NAME:"+tk.getName()+ " - GROUP:" +tk.getGroup());
        }

        String ms = " - Media player : ";
        PlayState state = PlayState.getCurrentPlayState();
        if (state!=null && state.isPlaying())
            ms += "RUNNING";
        else
            ms += "NOT RUNNING";
        logger.info(ms);

        logger.info("Thread list :");
        Thread[] threadList = new Thread[Thread.activeCount()];
        Thread.enumerate(threadList);
        for (Thread t : threadList) {
            logger.info(" - " + t.getName() + " " + (t.isDaemon() ? "(daemon)" : ""));
        }

    }
}