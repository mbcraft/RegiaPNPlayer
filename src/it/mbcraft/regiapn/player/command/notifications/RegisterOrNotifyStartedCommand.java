/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.notifications;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.data.dao.BootConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class RegisterOrNotifyStartedCommand implements ICommand {

    @Override
    public void execute() {
        if (!BootConfigurationDAO.getInstance().exists()) {
            throw new IllegalStateException("No player_boot.xml found.");
        } else {
            if (!GlobalConfigurationDAO.getInstance().exists()) {
                //registro il player
                RegisterPlayerCommand register_cmd = new RegisterPlayerCommand();
                register_cmd.execute();

            }

            NotifyInstanceStartedCommand notify_started_cmd = new NotifyInstanceStartedCommand();
            notify_started_cmd.execute();
        }
    }

}
