/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.data.serializers;

import com.thoughtworks.xstream.XStream;
import it.mbcraft.regiapn.player.data.models.Playlist;
import it.mbcraft.regiapn.player.data.models.Song;
import it.mbcraft.regiapn.player.data.models.Spot;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class PlaylistSerializer extends XStream {
    public PlaylistSerializer() {

        ignoreUnknownElements();

        alias("playlist", Playlist.class);
        alias("song", Song.class);
        alias("spot", Spot.class);
    }
}
