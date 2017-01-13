package it.mbcraft.regiapn.player.jobs;

import it.mbcraft.regiapn.player.command.management.ReloadConfigurationsCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
public class CheckReloadConfigJob implements Job {

    private static Logger logger = LogManager.getLogger(CheckReloadConfigJob.class);

    public static JobDetail makeJobDetail() {
        return newJob(CheckReloadConfigJob.class).withIdentity("configs_reload_job", "default").build();
    }

    public static Trigger makeTrigger(JobDetail detail) {
        return newTrigger()
                .withIdentity("configs_reload", "default")
                .withSchedule(cronSchedule("15 */2 * * * ?"))
                .forJob(detail)
                .build();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.debug("Checking if some configs need to be reloaded ...");

        ReloadConfigurationsCommand cmd = new ReloadConfigurationsCommand();
        cmd.execute();
    }
}
