/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.utils;

import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;

import java.io.File;

/**
 * This class contains various methods used for getting meaningful files.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class PathHelper {

    /**
     * Ritorna il file di configurazione del player
     *
     * @return Il file di configurazione
     */
    public static File getConfigFile() {
        String config_path_pattern = GlobalConfigurationDAO.getConfigPath();
        String config_path = getComputedString(config_path_pattern);
        return new File(config_path);
    }

    /**
     * Ritorna il file della playlist
     *
     * @return Il file della playlist
     */
    public static File getPlaylistFile() {
        String playlist_path_pattern = GlobalConfigurationDAO.getPlaylistPath();
        String playlist_path = getComputedString(playlist_path_pattern);
        return new File(playlist_path);
    }

    /**
     * Elabora un percorso con pattern sostituendo le occorrenze.
     *
     * @param fullPatternPath Il percorso pattern
     * @return Il percorso con le occorrenze sostituite
     */
    public static String getComputedString(String fullPatternPath) {
        String computedString = fullPatternPath;

        computedString = computedString.replace("{" + IGlobalConfigurationKeys.CLIENTE_ID_KEY + "}", "" + GlobalConfigurationDAO.getClienteId());
        computedString = computedString.replace("{" + IGlobalConfigurationKeys.PLAYER_ID_KEY + "}", "" + GlobalConfigurationDAO.getPlayerId());
        computedString = computedString.replace("{" + IGlobalConfigurationKeys.CLIENTE_SECURITY_TOKEN_KEY + "}", GlobalConfigurationDAO.getClienteSecurityToken());
        computedString = computedString.replace("{" + IGlobalConfigurationKeys.PLAYER_SECURITY_TOKEN_KEY + "}", GlobalConfigurationDAO.getPlayerSecurityToken());
        return computedString;
    }

}
