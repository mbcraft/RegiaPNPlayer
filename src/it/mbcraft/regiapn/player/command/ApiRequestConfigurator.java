/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command;

import it.mbcraft.regiapn.player.Main;
import it.mbcraft.regiapn.player.command.updates.GetLastUpdateIdCommand;
import it.mbcraft.regiapn.player.command.updates.diff.CollectModuleFileInfoCommand;
import it.mbcraft.regiapn.player.command.updates.diff.PackCollectedFileInfosCommand;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class ApiRequestConfigurator {

    private static final String API_CLIENTE_ID_KEY = "cliente_id";
    private static final String API_PLAYER_ID_KEY = "player_id";
    private static final String API_PLAYER_SECURITY_TOKEN_KEY = "player_security_token";
    private static final String API_UPDATE_ID_KEY = "update_id";


    private static final String API_SOFTWARE_NAME_KEY = "software_name";
    private static final String API_SOFTWARE_VERSION_KEY = "software_version";
    private static final String API_SOFTWARE_VENDOR_KEY = "software_vendor";

    private static final String API_SPECIFICATION_KEY = "specification";


    public static void pushIdentifiers(Properties pt) {

        String cliente_id = "" + GlobalConfigurationDAO.getClienteId();
        pt.setProperty(API_CLIENTE_ID_KEY, cliente_id);
        String player_id = "" + GlobalConfigurationDAO.getPlayerId();
        pt.setProperty(API_PLAYER_ID_KEY, player_id);
        String security_token = GlobalConfigurationDAO.getPlayerSecurityToken();
        pt.setProperty(API_PLAYER_SECURITY_TOKEN_KEY, security_token);

    }

    public static void pushSoftwareInfo(Properties pt) {

        String software_name = Main.SOFTWARE_NAME;
        pt.setProperty(API_SOFTWARE_NAME_KEY, software_name);

        String software_version = Main.SOFTWARE_VERSION;
        pt.setProperty(API_SOFTWARE_VERSION_KEY, software_version);

        String software_vendor = Main.SOFTWARE_VENDOR;
        pt.setProperty(API_SOFTWARE_VENDOR_KEY, software_vendor);

    }

    public static void pushCurrentUpdateId(Properties pt) {
        GetLastUpdateIdCommand cmd = new GetLastUpdateIdCommand();
        cmd.execute();

        String lastUpdateId = "" + cmd.getLastUpdateId();
        pt.setProperty(API_UPDATE_ID_KEY, lastUpdateId);
    }

    public static void pushSpecification(Properties pt, String spec) {
        pt.setProperty(API_SPECIFICATION_KEY, spec);
    }

    public static void pushModulesDiffInfo(Properties pt) {
        String modulesDiffSpec = GlobalConfigurationDAO.getModulesDiffSpecification();
        String[] specParts = modulesDiffSpec.split(",");
        List<CollectModuleFileInfoCommand> collectors = new ArrayList<>();
        for (String spec : specParts) {
            String[] specTokens = spec.split("=");
            File moduleRoot = new File(specTokens[1]);
            CollectModuleFileInfoCommand cmd = new CollectModuleFileInfoCommand(specTokens[0], moduleRoot);
            cmd.execute();
            collectors.add(cmd);
        }
        PackCollectedFileInfosCommand packer = new PackCollectedFileInfosCommand(collectors);
        packer.execute();
        pt.putAll(packer.getPackedModulesFileInfo());
    }
}
