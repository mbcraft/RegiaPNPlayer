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
import it.mbcraft.regiapn.player.data.dao.defaults.PlayerConfigurationDefaults;
import it.mbcraft.regiapn.player.data.dao.keys.IGlobalConfigurationKeys;
import it.mbcraft.regiapn.player.data.dao.keys.IPlayerConfigurationKeys;
import it.mbcraft.regiapn.player.utils.PathHelper;
import it.mbcraft.regiapn.player.utils.SafePrimitiveParser;

import java.io.File;

/**
 * Questa classe mantiene una configurazione secondaria, per il player,
 * letta dal file config.xml presente nella cartella dei file relativi al player,
 * scaricata e aggiornata tramite aggiornamenti.
 */
@CodeClassification(CodeType.SPECIFIC)
public class PlayerConfigurationDAO extends AbstractReloadableConfiguration {

    private static PlayerConfigurationDAO _instance = null;

    /**
     * Ritorna il singleton di questo DAO.
     *
     * @return Il singleton che permette l'accesso alla risorsa.
     */
    public static PlayerConfigurationDAO getInstance() {
        if (_instance == null) _instance = new PlayerConfigurationDAO();
        return _instance;
    }

    private PlayerConfigurationDAO() {
    }

    @Override
    protected File getResourceFile() {
        GlobalConfigurationDAO ic = GlobalConfigurationDAO.getInstance();
        String configPath = ic.get(IGlobalConfigurationKeys.CONFIG_PATH_KEY);
        return new File(PathHelper.getComputedString(configPath));
    }

    //--------------------------------------------------------------------------

    public static boolean hasTimeZone() {
        return getInstance().has(IPlayerConfigurationKeys.TIME_ZONE_KEY);
    }

    public static String getTimeZone() {
        return getInstance().get(IPlayerConfigurationKeys.TIME_ZONE_KEY);
    }

    public static String[] getPlayStartTime() {
        return SafePrimitiveParser.parseDayTime(getS(IPlayerConfigurationKeys.PLAY_START_TIME_KEY), PlayerConfigurationDefaults.PLAY_START_TIME, "Unable to parse play start time!");
    }

    public static String[] getPlayEndTime() {
        return SafePrimitiveParser.parseDayTime(getS(IPlayerConfigurationKeys.PLAY_END_TIME_KEY), PlayerConfigurationDefaults.PLAY_END_TIME, "Unable to parse play end time!");
    }

    public static int getPlayEndTimeDayOffset() {
        return SafePrimitiveParser.parseInt(getS(IPlayerConfigurationKeys.PLAY_END_TIME_DAY_OFFSET_KEY), PlayerConfigurationDefaults.PLAY_END_TIME_DAY_OFFSET, "Unable to parse play end time day offset");
    }

    public static int getCheckPlayMinutesInterval() {
        return SafePrimitiveParser.parseInt(getS(IPlayerConfigurationKeys.CHECK_PLAY_MINUTES_INTERVAL_KEY), PlayerConfigurationDefaults.CHECK_PLAY_MINUTES_INTERVAL, "Unable to parse check play minutes interval!");
    }

    public static boolean hasFilesUpdateTime() {
        return getInstance().has(IPlayerConfigurationKeys.FILES_UPDATE_TIME_KEY);
    }

    public static String[] getFilesUpdateTime() {
        return SafePrimitiveParser.parseDayTime(getS(IPlayerConfigurationKeys.FILES_UPDATE_TIME_KEY), PlayerConfigurationDefaults.FILES_UPDATE_TIME, "Unable to parse update time!");
    }

    public static boolean getLoopPlaylist() {
        return SafePrimitiveParser.parseBoolean(getS(IPlayerConfigurationKeys.LOOP_PLAYLIST_KEY), PlayerConfigurationDefaults.LOOP_PLAYLIST, "Unable to parse loop playlist value");
    }

    public static boolean getStartFromBeginning() {
        return SafePrimitiveParser.parseBoolean(getS(IPlayerConfigurationKeys.START_FROM_BEGINNING_KEY), PlayerConfigurationDefaults.START_FROM_BEGINNING, "Unable to parse start from beginning value");
    }

    public static boolean hasAudioOutputMode() {
        return getInstance().has(IPlayerConfigurationKeys.AUDIO_MODE_KEY);
    }

    public static String getAudioOutputMode() {
        return getInstance().get(IPlayerConfigurationKeys.AUDIO_MODE_KEY);
    }

    private static String getS(String key) {
        return getInstance().get(key);
    }

    //--------------------------------------------------------------------------

}