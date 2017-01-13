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

import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.data.models.Playable;
import it.mbcraft.regiapn.player.data.models.Playlist;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * This class contains various helper methods used for getting times or
 * doing time calcs for playlists.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class DateTimeHelper {

    /**
     * Ritorna il tempo di inizio play presente nella configurazione
     *
     * @return Il tempo di inizio play, letto dalla configurazione
     */
    public static Calendar getConfiguredPlayStartTime() {
        String timeTokens[] = PlayerConfigurationDAO.getPlayStartTime();
        return getTimeFromHourAndMinute(timeTokens, 0);
    }

    /**
     * Ritorna l'indice da cui cominciare a leggere la playlist, in base
     * al tempo passato come parametro.
     *
     * @param pl            La Playlist
     * @param expectedStart Tempo di inizio previsto, in Calendar
     * @param start_from_beginning Se il play comincia sempre dall'inizio della playlist
     * @param loop_playlist Se la playlist cicla
     * @return L'indice di inizio play
     */
    public static int getStartIndexForPlaylist(Playlist pl, Calendar expectedStart, boolean start_from_beginning, boolean loop_playlist) {
        if (pl==null)
            throw new InvalidParameterException("Playlist can't be null!");
        if (expectedStart==null)
            throw new InvalidParameterException("ExpectedStart can't be null!");

        int index = 0;

        if (!start_from_beginning) {
            Calendar now = DateTimeHelper.getNow();

            long diff = now.getTimeInMillis() - expectedStart.getTimeInMillis();
            List<Playable> list = pl.getList();

            boolean ready = false;
            while (!ready) {
                long nextTrackMillis = list.get(index).getDurationInSeconds() * 1000;
                if (diff - nextTrackMillis < 0) ready = true;
                else {
                    diff -= nextTrackMillis;
                    index++;
                }
                if (loop_playlist) index %= list.size();
                else if (index == list.size()) ready = true;
            }

        }

        return index;

    }

    /**
     * Ritorna il tempo di fine play presente nella configurazione
     *
     * @return Il tempo di fine play, letto dalla configurazione
     */
    public static Calendar getConfiguredPlayEndTime() {

        String timeTokens[] = PlayerConfigurationDAO.getPlayEndTime();
        int day_offset = PlayerConfigurationDAO.getPlayEndTimeDayOffset();
        return getTimeFromHourAndMinute(timeTokens, day_offset);

    }

    /**
     * Ritorna l'ora attuale, comprensiva di time zone.
     *
     * @return L'ora come istanza di Calendar.
     */
    public static Calendar getNow() {
        Calendar now = Calendar.getInstance();
        DateFormat df = DateFormat.getInstance();
        if (PlayerConfigurationDAO.hasTimeZone()) {
            String time_zone = PlayerConfigurationDAO.getTimeZone();
            df.setTimeZone(TimeZone.getTimeZone(time_zone));
        }
        return now;
    }

    /**
     * Returns the calendar instance set to the specified hour and minute, with
     * the dayOffset value added to the current day.
     *
     * @param timeTokens The hour and minute time tokens.
     * @param dayOffset     The integer offset of day to add to the resulting time.
     * @return The resulting Calendar value
     */
    public static Calendar getTimeFromHourAndMinute(String[] timeTokens, int dayOffset) {
        Calendar time = Calendar.getInstance();
        if (PlayerConfigurationDAO.hasTimeZone()) {
            String timeZone = PlayerConfigurationDAO.getTimeZone();
            time.setTimeZone(TimeZone.getTimeZone(timeZone));
        }

        int hour = Integer.parseInt(timeTokens[0]);
        int minute = Integer.parseInt(timeTokens[1]);
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        time.add(Calendar.DAY_OF_YEAR, dayOffset);
        return time;
    }

    /**
     * Ritorna il tempo di tolleranza per l'inizio del play, impostato a 10 secondi, in quanto il tempo non ci sono thread a parte per controllare il player.
     *
     *
     * @return Il tempo di sleep, in secondi
     */
    public static int getToleranceMillis() {
        return  10 * 1000;
    }

    /**
     * Converte una durata nel formato HH:mm:ss.MM in secondi. La stringa è nel
     * formato HH:mm:ss.MM , es : 00:03:27.46
     *
     * @param duration La durata nel formato specificato.
     * @return Il numero di secondi corrispondenti.
     */
    public static long getSecondsFromStringDuration(String duration) {
        long result = 0;
        String[] tokens = duration.split("[:\\.]");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);
        int tenthMillis = Integer.parseInt(tokens[3]);
        result += hours * 3600;
        result += minutes * 60;
        result += seconds;
        if (tenthMillis > 50) {
            result++;
        }
        return result;
    }

    public static boolean isPlayTime(Calendar currentTime,Calendar startTime,Calendar endTime,long toleranceMillis,boolean checkYesterdayIfNeeded) {
        if (currentTime==null) throw new InvalidParameterException("currentTime can't be null!");
        if (startTime==null) throw new InvalidParameterException("startTime can't be null!");
        if (endTime==null) throw new InvalidParameterException("endTime can't be null!");
        if (toleranceMillis<0) throw new InvalidParameterException("toleranceMillis can't be less than zero!");

        //within start and end time window
        if (currentTime.after(startTime) && currentTime.before(endTime)) {
            return true;
        }

        //just around the start time
        if (Math.abs(currentTime.getTimeInMillis() - startTime.getTimeInMillis()) < toleranceMillis) {
            return true;
        }

        if (checkYesterdayIfNeeded && endTime.get(Calendar.DAY_OF_YEAR)-startTime.get(Calendar.DAY_OF_YEAR)>0) {
            Calendar startYesterday = (Calendar)startTime.clone();
            startYesterday.add(Calendar.DAY_OF_YEAR,-1);
            Calendar endYesterday = (Calendar)endTime.clone();
            endYesterday.add(Calendar.DAY_OF_YEAR,-1);
            return isPlayTime(currentTime,startYesterday,endYesterday,toleranceMillis,false);
        }

        return false;
    }
    /**
     * Returns if the current time is valid for playing.
     *
     * @return true if it's time to play, false otherwise.
     */
    public static boolean isPlayTime() {
        Calendar currentTime = DateTimeHelper.getNow();
        Calendar startTime = DateTimeHelper.getConfiguredPlayStartTime();
        Calendar endTime = DateTimeHelper.getConfiguredPlayEndTime();
        long playerSleepMillis = DateTimeHelper.getToleranceMillis();

        return isPlayTime(currentTime,startTime,endTime,playerSleepMillis,true);
    }

}
