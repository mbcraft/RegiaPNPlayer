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
import it.mbcraft.libraries.net.http.EasyHttp;
import it.mbcraft.libraries.net.http.HttpErrorException;
import it.mbcraft.regiapn.player.command.ApiRequestConfigurator;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.utils.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class RequestUpdateCommand implements ICommand {

    private static final String OK_RESPONSE = "OK";
    private static final Logger logger = LogManager.getLogger(RequestUpdateCommand.class);

    @Override
    public void execute() {
        logger.debug("Executing update request command : " + getClass());

        GlobalConfigurationDAO dao = GlobalConfigurationDAO.getInstance();
        if (dao.needsReloading()) {
            dao.reload();
        }

        String remote_host = GlobalConfigurationDAO.getUpdateRemoteHost();
        String request_update_page = IUpdateApiEndpoints.REQUEST_ENDPOINT;
        String address = "http://" + remote_host + request_update_page;

        Output.console("Requesting update from : " + address);

        Properties params = new Properties();
        //elenco di parametri da inviare
        ApiRequestConfigurator.pushIdentifiers(params);

        ApiRequestConfigurator.pushModulesDiffInfo(params);

        //esegue la chiamata utilizzando il metodo POST
        EasyHttp fh = new EasyHttp();
        try {
            String responseText = fh.doPost(address, params);
            fh.dispose();

            if (!OK_RESPONSE.equals(responseText)) {
                throw new IllegalStateException("Error during update : " + getClass());
            }
        } catch (HttpErrorException ex) {
            logger.error("HTTP error during update request : "+ex);
        } catch (Exception ex) {
            logger.error("Error during update request",ex);
        }
    }


}
