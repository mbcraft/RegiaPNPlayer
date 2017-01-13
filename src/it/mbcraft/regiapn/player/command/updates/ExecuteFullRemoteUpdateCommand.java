/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.management.ReloadConfigurationsCommand;
import it.mbcraft.regiapn.player.command.notifications.NotifyUpdateCompletedCommand;
import it.mbcraft.regiapn.player.command.notifications.NotifyUpdateStartedCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class ExecuteFullRemoteUpdateCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(ExecuteFullRemoteUpdateCommand.class);

    @Override
    public void execute() {

        ReloadConfigurationsCommand reloadConfig = new ReloadConfigurationsCommand();
        reloadConfig.execute();

        CheckUpdateCommand checkUpdate = new CheckUpdateCommand();
        checkUpdate.execute();

        if (checkUpdate.isUpdateAvailable()) {
            logger.info("New update found!");

            RequestUpdateCommand requestUpdate = new RequestUpdateCommand();
            requestUpdate.execute();

            NotifyUpdateStartedCommand notifyStarted = new NotifyUpdateStartedCommand();
            notifyStarted.execute();

            DownloadUpdateCommand downloadUpdate = new DownloadUpdateCommand();
            downloadUpdate.execute();

            InstallUpdateCommand installUpdate = new InstallUpdateCommand();
            installUpdate.execute();

            NotifyUpdateCompletedCommand notifyUpdate = new NotifyUpdateCompletedCommand();
            notifyUpdate.execute();

        } else {
            if (checkUpdate.isError())
                logger.error("Error during check for updates :" + checkUpdate.getErrorResponse());
            else
                logger.info("No new updates found.");
        }


    }

}
