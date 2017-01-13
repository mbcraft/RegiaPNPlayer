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

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public interface IDirectories {

    String TMP_DIR_PATH = "tmp/";

    String TMP_LOG_DIR_PATH = TMP_DIR_PATH + "log/";
    String TMP_DOWNLOAD_DIR_PATH = TMP_DIR_PATH + "download/";
    String TMP_EXTRACT_DIR_PATH = TMP_DIR_PATH + "extract/";
    String TMP_FLAGS_DIR_PATH = TMP_DIR_PATH + "flags/";
    String TMP_STATE_DIR_PATH = TMP_DIR_PATH + "state/";

    String GENERATED_PLAYLIST_FILE_PATH = TMP_STATE_DIR_PATH + "playlist.m3u";
    String HTTP_ROOT_PATH = "data/http_root/";
    String LAST_UPDATE_PATH = "data/last_update";
    String SOFTWARE_PATH = "dist/";

}
