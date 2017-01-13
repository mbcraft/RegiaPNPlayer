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
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;

import java.io.File;

/**
 * Questa classe permette di accedere alla configurazione globale,
 * presente nel file global.xml che viene scaricato al momento della
 * registrazione del player.
 * Viene scaricato tramite richiesta http e non viene più modificato.
 */
@CodeClassification(CodeType.SPECIFIC)
public class GlobalConfigurationDAO extends AbstractReloadableConfiguration {


    //------------------------------------------------------

    private static GlobalConfigurationDAO _instance = null;

    private GlobalConfigurationDAO() {
    }

    public static GlobalConfigurationDAO getInstance() {
        if (_instance == null) _instance = new GlobalConfigurationDAO();
        return _instance;
    }

    @Override
    protected File getResourceFile() {
        return new File(IGlobalConfigurationKeys.DEFAULT_GLOBAL_CONFIG_PATH);
    }

    //--------------------------------------------------------------------------

    public static int getClienteId() {
        return Integer.parseInt(getInstance().get(IGlobalConfigurationKeys.CLIENTE_ID_KEY));
    }

    public static int getPlayerId() {
        return Integer.parseInt(getInstance().get(IGlobalConfigurationKeys.PLAYER_ID_KEY));
    }

    public static String getClienteSecurityToken() {
        return getInstance().get(IGlobalConfigurationKeys.CLIENTE_SECURITY_TOKEN_KEY);
    }

    public static String getPlayerSecurityToken() {
        return getInstance().get(IGlobalConfigurationKeys.PLAYER_SECURITY_TOKEN_KEY);
    }

    public static String getHttpRoot() {
        return getInstance().get(IGlobalConfigurationKeys.HTTP_ROOT_KEY);
    }

    public static String getConfigPath() {
        return getInstance().get(IGlobalConfigurationKeys.CONFIG_PATH_KEY);
    }

    public static String getPlaylistPath() {
        return getInstance().get(IGlobalConfigurationKeys.PLAYLIST_PATH_KEY);
    }

    public static String getOverrideGlobalConfigPath() {
        return getInstance().get(IGlobalConfigurationKeys.OVERRIDE_GLOBAL_CONFIG_PATH_KEY);
    }

    public static String getUpdateRemoteHost() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_REMOTE_HOST_KEY);
    }

    public static String getInstanceStartedNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.INSTANCE_STARTED_NOTIFICATION_ADDRESS_KEY);
    }

    public static String getServiceStartedNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.SERVICE_STARTED_NOTIFICATION_ADDRESS_KEY);
    }

    public static String getServiceStoppedNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.SERVICE_STOPPED_NOTIFICATION_ADDRESS_KEY);
    }

    public static String getUpdateStartedNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_STARTED_NOTIFICATION_ADDRESS_KEY);
    }

    public static String getUpdateCompletedNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_COMPLETED_NOTIFICATION_ADDRESS_KEY);
    }

    public static String getErrorNotificationAddress() {
        return getInstance().get(IGlobalConfigurationKeys.ERROR_NOTIFICATION_ADDRESS_KEY);
    }

    // updates

    public static String getUpdateCheckAddress() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_CHECK_ADDRESS_KEY);
    }

    public static String getUpdateRequestAddress() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_REQUEST_ADDRESS_KEY);
    }

    public static String getUpdateDownloadAddress() {
        return getInstance().get(IGlobalConfigurationKeys.UPDATE_DOWNLOAD_ADDRESS_KEY);
    }

    public static String getModulesDiffSpecification() {
        return getInstance().get(IGlobalConfigurationKeys.MODULES_DIFF_SPECIFICATION);
    }

    //--------------------------------------------------------------------------
}

