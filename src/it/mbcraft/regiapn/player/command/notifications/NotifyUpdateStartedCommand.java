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
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class NotifyUpdateStartedCommand extends AbstractNotificationCommand {

    @Override
    protected String getGlobalConfigurationNotificationPage() {
        return GlobalConfigurationDAO.getUpdateStartedNotificationAddress();
    }


}
