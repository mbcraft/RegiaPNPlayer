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

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.net.http.EasyHttp;
import it.mbcraft.libraries.net.http.HttpErrorException;
import it.mbcraft.regiapn.player.command.ApiRequestConfigurator;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
abstract class AbstractNotificationCommand implements ICommand {

    protected abstract String getGlobalConfigurationNotificationPage();

    protected void pushAdditionalParameters(Properties pt) {
    }

    private Logger getLogger() {
        return LogManager.getLogger(getClass());
    }

    @Override
    public void execute() {

        getLogger().debug("Executing notification command : " + getClass());

        GlobalConfigurationDAO dao = GlobalConfigurationDAO.getInstance();
        if (dao.needsReloading()) {
            dao.reload();
        }

        String remote_host = GlobalConfigurationDAO.getUpdateRemoteHost();
        String notification_page = getGlobalConfigurationNotificationPage();
        String address = "http://" + remote_host + notification_page;

        Properties params = new Properties();
        //elenco di parametri da inviare
        ApiRequestConfigurator.pushIdentifiers(params);
        pushAdditionalParameters(params);

        //esegue la chiamata utilizzando il metodo POST
        EasyHttp fh = new EasyHttp();
        try {
            String responseText = fh.doPost(address, params);
            fh.dispose();

            if (!fh.isLastResponseOK()) {
                getLogger().warn("Response from server is NOT ok : "+responseText);
            }
        } catch (HttpErrorException | IOException ex) {
            getLogger().error("Unable to send notification to server : "+ex.getMessage());
        } catch (Exception ex) {
            getLogger().error("Unable to connect to server : "+ex.getMessage());
        }

    }
}
