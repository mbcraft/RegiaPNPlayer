/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.data.models;

import java.util.List;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class Playlist {

    private String title;
    private String lastUpdate;

    private List<Playable> list;

    /**
     * @return the playlist
     */
    public List<Playable> getList() {
        return list;
    }

    /**
     * @param list the playlist to set
     */
    public void setList(List<Playable> list) {
        this.list = list;
    }

    /**
     * @return the _title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param _title the _title to set
     */
    public void setTitle(String _title) {
        this.title = _title;
    }

    /**
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Returns the total duration of this playlist
     *
     * @return The total duration of this playlist, in seconds
     */
    public long getTotalDuration() {
        long totalDuration = 0;
        for (Playable pl : list) {
            totalDuration+= pl.getDurationInSeconds();
        }
        return totalDuration;
    }
}
