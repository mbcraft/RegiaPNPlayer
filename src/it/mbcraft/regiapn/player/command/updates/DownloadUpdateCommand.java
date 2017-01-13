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
import it.mbcraft.regiapn.player.command.IDirectories;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class DownloadUpdateCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(DownloadUpdateCommand.class);
    private static final String OK_RESPONSE = "OK";

    private File downloadedFile = null;

    @Override
    public void execute() {
        logger.debug("Executing update download command : " + getClass());

        GlobalConfigurationDAO dao = GlobalConfigurationDAO.getInstance();
        if (dao.needsReloading()) {
            dao.reload();
        }

        String remote_host = GlobalConfigurationDAO.getUpdateRemoteHost();
        String download_update_page = IUpdateApiEndpoints.DOWNLOAD_ENDPOINT;
        String address = "http://" + remote_host + download_update_page;

        Properties params = new Properties();
        //elenco di parametri da inviare
        ApiRequestConfigurator.pushIdentifiers(params);
        //more to come ...
        // ....
        // ....
        // ....
        File downloadDir = new File(IDirectories.TMP_DOWNLOAD_DIR_PATH);
        downloadDir.mkdir();
        //esegue la chiamata utilizzando il metodo GET
        EasyHttp fh = new EasyHttp();
        try {
            String updateFilename = fh.doPostDownload(address, params, downloadDir);
            fh.dispose();

            downloadedFile = new File(downloadDir, updateFilename);

        } catch (HttpErrorException ex) {
            logger.error("HTTP error during update download",ex);
        }
        catch (Exception ex) {
            logger.error("Error during update download",ex);
        }
    }

    public File getDownloadedFile() {
        return downloadedFile;
    }

}
