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
import it.mbcraft.regiapn.player.command.ApiRequestConfigurator;
import it.mbcraft.regiapn.player.data.dao.BootConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.keys.IBootConfigurationKeys;
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;
import it.mbcraft.regiapn.player.utils.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**
 * Questo comando effettua la registrazione del player lato server, e salva la
 * configurazione rilasciata sotto forma di file xml nel file global.xml che
 * contiene la configurazione generica del player.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
class RegisterPlayerCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(RegisterPlayerCommand.class);

    @Override
    public void execute() {
        BootConfigurationDAO dao = BootConfigurationDAO.getInstance();

        if (dao.exists()) {
            dao.reload();

            String protocol = BootConfigurationDAO.getRegistrationProtocol();
            String host = BootConfigurationDAO.getRegistrationHost();
            String page = BootConfigurationDAO.getRegistrationPage();
            String full_address = protocol + "://" + host + page;

            Properties params = new Properties();
            //elenco di parametri da inviare
            params.setProperty(IBootConfigurationKeys.REGISTRATION_CODE_KEY, BootConfigurationDAO.getRegistrationCode());
            ApiRequestConfigurator.pushSoftwareInfo(params);
            // ...
            logger.debug("Registering player instance at " + full_address);
            //
            EasyHttp fh = new EasyHttp();
            try {
                String responseText = fh.doPost(full_address, params);
                fh.dispose();

                File f = new File(IGlobalConfigurationKeys.DEFAULT_GLOBAL_CONFIG_PATH);
                try {
                    Files.write(f.toPath(), responseText.getBytes(), StandardOpenOption.CREATE);
                    logger.info("Configuration file global.xml saved.");
                } catch (IOException ex) {
                    logger.error("Unable to write global.xml file to data storage.", ex);
                    throw new IllegalStateException("Unable to write global.xml file to disk.");
                }
            } catch (HttpErrorException | IOException ex) {

                logger.warn("Unable to register player to server : "+ex.getMessage());
                Output.console("Unable to register player to server : "+ex.getMessage());
                System.exit(1);
            }
        } else {
            logger.error("player_boot.xml not found : player will terminate");
            Output.console("Unable to register : player will terminate");
            System.exit(1);
        }
    }
}