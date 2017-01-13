/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.jobs;

import it.mbcraft.regiapn.player.command.media.CheckConfigsForPlayerCommand;
import it.mbcraft.regiapn.player.command.media.StartMediaPlayerCommand;
import it.mbcraft.regiapn.player.command.media.StopMediaPlayerCommand;
import it.mbcraft.regiapn.player.command.updates.ExecuteFullRemoteUpdateCommand;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.media.PlayState;
import it.mbcraft.regiapn.player.utils.DateTimeHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Questo job si occupa di controllare se il player/server http devono essere
 * avviati, stoppati e provvede a farlo correttamente.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class MediaPlayerStatusCheckerJob implements Job {

    private static final Logger logger = LogManager.getLogger(MediaPlayerStatusCheckerJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        logger.debug("Checking for media player job running ...");

        CheckConfigsForPlayerCommand checkCmd = new CheckConfigsForPlayerCommand();
        checkCmd.execute();

        if (checkCmd.isConfigurationValid()) {
            PlayState ps = PlayState.getCurrentPlayState();
            if (DateTimeHelper.isPlayTime()) {
                if (ps==null) {
                    logger.info("Starting media player ...");

                    //start the server if not started
                    StartMediaPlayerCommand cmd = new StartMediaPlayerCommand();
                    cmd.execute();

                    logger.info("Media player started!");
                } else
                    logger.debug("Play time - playing ...");
            } else {
                if (ps!=null && ps.isPlaying()) {
                    logger.info("Stopping media player ...");

                    //stop the player if started
                    StopMediaPlayerCommand cmd = new StopMediaPlayerCommand();
                    cmd.execute();

                    logger.info("Media player stopped!");
                } else
                    logger.debug("No play time - not playing ...");
            }

        } else {
            logger.warn("No player configuration or playlist found!");
            logger.info("Forcing remote update ...");
            ExecuteFullRemoteUpdateCommand execUpd = new ExecuteFullRemoteUpdateCommand();
            execUpd.execute();
        }
    }

    public static JobDetail makeJobDetail() {
        return newJob(MediaPlayerStatusCheckerJob.class).withIdentity("play_job", "default").build();
    }

    public static Trigger makeTrigger(JobDetail detail) {
        int minutesInterval = PlayerConfigurationDAO.getCheckPlayMinutesInterval();

        return newTrigger()
                .withIdentity("play_trigger", "default")
                .withSchedule(cronSchedule("0 */" + minutesInterval + " * * * ?"))
                .forJob(detail)
                .build();
    }
}