package it.mbcraft.regiapn.player.jobs;

import it.mbcraft.regiapn.player.command.management.CheckFlagsCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by marco on 11/06/16.
 */
public class FlagCheckingJob implements Job {

    private static final Logger logger = LogManager.getLogger(FlagCheckingJob.class);

    public static JobDetail makeJobDetail() {
        return newJob(FlagCheckingJob.class).withIdentity("flags_check_job", "default").build();
    }

    public static Trigger makeTrigger(JobDetail detail) {
        return newTrigger()
                .withIdentity("flags_check", "default")
                .withSchedule(cronSchedule("30 * * * * ?"))
                .forJob(detail)
                .build();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.debug("Checking flags job running ...");

        CheckFlagsCommand check = new CheckFlagsCommand();
        check.execute();
    }


}
