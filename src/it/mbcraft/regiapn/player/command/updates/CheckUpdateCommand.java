package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.net.http.EasyHttp;
import it.mbcraft.libraries.net.http.HttpErrorException;
import it.mbcraft.regiapn.player.command.ApiRequestConfigurator;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * Created by marco on 09/05/16.
 */
public class CheckUpdateCommand implements ICommand {

    private static final String NEW_UPDATE_AVAILABLE_RESPONSE = "NEW UPDATE AVAILABLE";
    private static final String NO_UPDATES_YET_RESPONSE = "NO UPDATES YET";
    private static final String ERROR_RESPONSE_PREFIX = "ERROR";

    private static final Logger logger = LogManager.getLogger(CheckUpdateCommand.class);


    private String errorResponse = null;
    private boolean updateAvailable = false;
    private boolean error = false;

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void execute() {
        logger.debug("Executing update check ...");

        GlobalConfigurationDAO dao = GlobalConfigurationDAO.getInstance();
        if (dao.needsReloading()) {
            dao.reload();
        }

        String remote_host = GlobalConfigurationDAO.getUpdateRemoteHost();
        String check_update_page = IUpdateApiEndpoints.CHECK_ENDPOINT;
        String address = "http://" + remote_host + check_update_page;

        Properties params = new Properties();
        //elenco di parametri da inviare
        ApiRequestConfigurator.pushIdentifiers(params);
        ApiRequestConfigurator.pushCurrentUpdateId(params);
        ApiRequestConfigurator.pushSoftwareInfo(params);

        //esegue la chiamata utilizzando il metodo GET
        EasyHttp fh = new EasyHttp();
        try {
            String responseString = fh.doGet(address, params);
            fh.dispose();

            updateAvailable = responseString.equals(NEW_UPDATE_AVAILABLE_RESPONSE);
            error = responseString.startsWith(ERROR_RESPONSE_PREFIX);

            if (error) {
                errorResponse = responseString.substring(responseString.indexOf(":") + 1);
            }
        } catch (HttpErrorException ex) {
            logger.error("HTTP Error : Unable to check update from " + address+ " : "+ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unable to check for update : "+ex.getMessage());
        }
    }
}
