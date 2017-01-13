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

import it.mbcraft.libraries.command.io.CopyFileCommand;
import it.mbcraft.libraries.command.io.TryDeleteRootOwnedFileCommand;
import it.mbcraft.libraries.command.system.SystemShutdownCommand;
import it.mbcraft.libraries.command.usbdrive.FindNewUsbDriveCommand;
import it.mbcraft.regiapn.player.command.IDirectories;
import it.mbcraft.regiapn.player.command.management.flags.ShutdownOnUsbWithoutUpdatesFlag;
import it.mbcraft.regiapn.player.command.updates.FindUpdateFileCommand;
import it.mbcraft.regiapn.player.command.updates.InstallUpdateCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.io.File;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Questa classe deve monitorare le modifiche nei drive USB presenti e aggiornare lo stato di conseguenza.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class CheckUsbDriveForUpdatesJob implements Job {

    private static final Logger logger = LogManager.getLogger(CheckUsbDriveForUpdatesJob.class);

    public static JobDetail makeJobDetail() {
        return newJob(CheckUsbDriveForUpdatesJob.class).withIdentity("usb_drive_check_job", "default").build();
    }

    public static Trigger makeTrigger(JobDetail detail) {
        return newTrigger()
                .withIdentity("usb_drive_check_trigger", "default")
                .withSchedule(cronSchedule("0 * * * * ?"))
                .forJob(detail)
                .build();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        logger.debug("Checking for new usb drives job running ...");

        FindNewUsbDriveCommand findUsb = new FindNewUsbDriveCommand();
        findUsb.execute();

        if (findUsb.hasFoundUsbDrive()) {

            logger.info("New usb drive found!");
            logger.info("Checking for updates ...");

            File usbDriveRoot = findUsb.getUsbDriveRoot();

            FindUpdateFileCommand findUpdate = new FindUpdateFileCommand(usbDriveRoot);
            findUpdate.execute();

            if (findUpdate.updateFound()) {
                logger.info("New update found! Installing ...");
                File downloadDir = new File(IDirectories.TMP_DOWNLOAD_DIR_PATH);
                CopyFileCommand copyFile = new CopyFileCommand(findUpdate.getUpdateFile(), downloadDir);
                copyFile.execute();

                InstallUpdateCommand install = new InstallUpdateCommand();
                install.execute();

                findUpdate.getUpdateFile().delete();
                //file can not be deleted? Try to delete as root with sudo
                if (findUpdate.getUpdateFile().exists()) {
                    logger.info("Unable to delete update file, trying with sudo ...");
                    TryDeleteRootOwnedFileCommand tryRootDelete = new TryDeleteRootOwnedFileCommand(findUpdate.getUpdateFile());
                    tryRootDelete.execute();
                    if (findUpdate.getUpdateFile().exists())
                        logger.error("Update file was NOT deleted!");
                }
                logger.info("Installation complete!");
            } else {
                logger.debug("No new updates found.");
                //shutting down
                if (ShutdownOnUsbWithoutUpdatesFlag.getInstance().isSet()) {
                    logger.info("ShutdownOnNoUsbUpdateFlag is set and usb drive is empty : shutting down ...");
                    SystemShutdownCommand shutdown = new SystemShutdownCommand();
                    shutdown.execute();
                }
            }
        } else {
            logger.debug("No new usb drives found.");
        }
    }

}
