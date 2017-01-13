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
import it.mbcraft.libraries.net.http.EasyHttp;
import it.mbcraft.libraries.net.http.HttpErrorException;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class NotifyError implements ICommand {

    private static final Logger logger = LogManager.getLogger(NotifyError.class);

    private static final String OK_RESPONSE = "OK";

    private final String myMessage;
    private final Properties params = new Properties();

    public NotifyError(String message) {
        myMessage = message;
        params.setProperty(IGlobalConfigurationKeys.MESSAGE_KEY, message);
    }

    @Override
    public void execute() {
        String remote_host = GlobalConfigurationDAO.getUpdateRemoteHost();
        String notification_address = GlobalConfigurationDAO.getErrorNotificationAddress();
        String address = "http://" + remote_host + notification_address;

        //send the error
        EasyHttp fh = new EasyHttp();
        try {
            String response = fh.doPost(address, params);

            if (!OK_RESPONSE.equals(response)) {
                logger.warn("The server returned error to error notification : "+response);
            }
        } catch (HttpErrorException ex) {
            logger.error("Unable to notify error : "+myMessage);
        } catch (Exception ex) {
            logger.error("Unable to connect to server : "+myMessage);
        }
    }
}
