/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.init;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class CheckAndOverwriteGlobalConfigCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(CheckAndOverwriteGlobalConfigCommand.class);

    @Override
    public void execute() {
        GlobalConfigurationDAO dao = GlobalConfigurationDAO.getInstance();
        if (!dao.exists())
            throw new IllegalStateException("Global configuration file does not exists!");
        File globalConfig = new File(IGlobalConfigurationKeys.DEFAULT_GLOBAL_CONFIG_PATH);
        File overwriteGlobalConfig = new File(GlobalConfigurationDAO.getOverrideGlobalConfigPath());
        if (overwriteGlobalConfig.exists()) {
            try {
                Files.move(overwriteGlobalConfig.toPath(), globalConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
                overwriteGlobalConfig.delete();
                dao.reload();
            } catch (IOException ex) {
                logger.error("Unable to rewrite global config file.", ex);
            }
        }

    }

}