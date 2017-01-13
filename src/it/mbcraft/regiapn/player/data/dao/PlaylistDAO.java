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
import it.mbcraft.regiapn.player.data.models.Playlist;
import it.mbcraft.regiapn.player.data.serializers.PlaylistSerializer;
import it.mbcraft.regiapn.player.utils.PathHelper;

import java.io.File;

/**
 * DAO per accedere alla playlist dell'utente.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class PlaylistDAO extends AbstractReloadable {

    private static PlaylistDAO _instance = null;

    private static Playlist playlist = null;

    public static PlaylistDAO getInstance() {
        if (_instance == null) _instance = new PlaylistDAO();
        return _instance;
    }

    /**
     * Ritorna il file della playlist.
     *
     * @return Il File della playlist
     */
    @Override
    protected File getResourceFile() {
        GlobalConfigurationDAO gc = GlobalConfigurationDAO.getInstance();
        String patternPath = gc.get(IGlobalConfigurationKeys.PLAYLIST_PATH_KEY);
        String finalPath = PathHelper.getComputedString(patternPath);
        File f = new File(finalPath);
        return f;
    }


    @Override
    public void reloadImpl(File f) {
        PlaylistSerializer xs = new PlaylistSerializer();
        playlist = (Playlist) xs.fromXML(f);
    }

    //--------------------------------------------------------------------------

    /**
     * Ritorna la playlist caricata dal file.
     *
     * @return Un'istanza di Playlist.
     */
    public static Playlist get() {
        if (playlist == null) throw new IllegalStateException("Playlist must be loaded before calling get.");
        return playlist;
    }

    //--------------------------------------------------------------------------
}
