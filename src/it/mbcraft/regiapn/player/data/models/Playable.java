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

import it.mbcraft.regiapn.player.utils.DateTimeHelper;

/**
 * Questa classe contiene metodi comuni per canzoni e spot.
 */
public abstract class Playable {

    private String duration;
    private String path;

    /**
     * @return the _duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param _duration the _duration to set
     */
    public void setDuration(String _duration) {
        this.duration = _duration;
    }

    /**
     * @return the _path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param _path the _path to set
     */
    public void setPath(String _path) {
        this.path = _path;
    }

    /**
     * Ritorna il numero di secondi di durata della traccia.
     *
     * @return La durata in secondi della traccia
     */
    public long getDurationInSeconds() {
        return DateTimeHelper.getSecondsFromStringDuration(getDuration());
    }

    /**
     * Ritorna il percorso finale della traccia
     *
     * @return Una stringa col percorso relativo della traccia
     */
    public abstract String getRealPath();
}