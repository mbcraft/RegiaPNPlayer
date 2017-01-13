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

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.updates.ExecuteFullRemoteUpdateCommand;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Questo job deve scaricare gli aggiornamenti :
 * <p>
 * - software
 * - file di configurazione
 * - spot
 * - canzoni
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class UpdateJob implements Job {

    private static final Logger logger = LogManager.getLogger(UpdateJob.class);

    public static JobDetail makeJobDetail() {
        return newJob(UpdateJob.class).withIdentity("update_job", "default").build();
    }

    public static Trigger makeTrigger(JobDetail detail) {
        //update
        String[] updateTimeTokens = PlayerConfigurationDAO.getFilesUpdateTime();

        int hour = Integer.parseInt(updateTimeTokens[0]);
        int minute = Integer.parseInt(updateTimeTokens[1]);
        return newTrigger()
                .withIdentity("update_trigger", "default")
                .withSchedule(dailyAtHourAndMinute(hour, minute))
                .forJob(detail)
                .build();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        logger.debug("Checking for updates job running ...");

        //notify the update before actually doing it
        ICommand fullUpdateCommand = new ExecuteFullRemoteUpdateCommand();
        fullUpdateCommand.execute();
    }

}
