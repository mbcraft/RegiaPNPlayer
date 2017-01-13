/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.data.dao;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.regiapn.player.data.dao.keys.IBootConfigurationKeys;

import java.io.File;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class BootConfigurationDAO extends AbstractReloadableConfiguration {

    private static BootConfigurationDAO _instance = null;

    private File bootFile;

    private BootConfigurationDAO() {
        bootFile = new File(IBootConfigurationKeys.DATA_PLAYER_BOOT_PATH);
        if (!bootFile.exists()) {
            bootFile = new File(IBootConfigurationKeys.ABS_BOOT_PLAYER_BOOT_PATH);
        }
    }


    public static BootConfigurationDAO getInstance() {
        if (_instance == null) _instance = new BootConfigurationDAO();
        return _instance;
    }

    @Override
    protected File getResourceFile() {
        return bootFile;
    }

    public void delete() {
        bootFile.delete();
    }

    //--------------------------------------------------------------------------

    public static String getRegistrationProtocol() {
        return getInstance().get(IBootConfigurationKeys.REGISTRATION_PROTOCOL_KEY);
    }

    public static String getRegistrationHost() {
        return getInstance().get(IBootConfigurationKeys.REGISTRATION_HOST_KEY);
    }

    public static String getRegistrationPage() {
        return getInstance().get(IBootConfigurationKeys.REGISTRATION_PAGE_KEY);
    }

    public static String getRegistrationCode() {
        return getInstance().get(IBootConfigurationKeys.REGISTRATION_CODE_KEY);
    }

    //--------------------------------------------------------------------------

}
