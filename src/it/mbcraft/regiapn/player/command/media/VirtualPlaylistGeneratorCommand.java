/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.media;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.data.models.Playable;
import it.mbcraft.regiapn.player.data.models.Playlist;
import it.mbcraft.regiapn.player.utils.DateTimeHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Genera una Playlist virtuale per il player dinamicamente, in base all'orario di inizio e
 * al tempo attuale.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class VirtualPlaylistGeneratorCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(VirtualPlaylistGeneratorCommand.class);

    private final Playlist refPlaylist;
    private final Calendar expectedStart, expectedEnd;
    private final Playlist virtualPlaylist;
    private final boolean start_from_beginning;
    private final boolean loop_playlist;

    public VirtualPlaylistGeneratorCommand(Playlist reference, Calendar start, Calendar end, boolean from_begin ,boolean loop) {
        refPlaylist = reference;
        expectedStart = start;
        expectedEnd = end;
        start_from_beginning = from_begin;
        loop_playlist = loop;
        virtualPlaylist = new Playlist();
        virtualPlaylist.setTitle("Virtual Playlist");
        virtualPlaylist.setLastUpdate(Calendar.getInstance().toString());
    }

    public Playlist getVirtualPlaylist() {
        return virtualPlaylist;
    }

    @Override
    public void execute() {

        List<Playable> tracks = refPlaylist.getList();
        List<Playable> virtualTracks = new ArrayList<>();

        int startIndex = DateTimeHelper.getStartIndexForPlaylist(refPlaylist, expectedStart, start_from_beginning, loop_playlist);

        if (startIndex < 0)
            throw new IllegalStateException("Something has gone wrong with business logic : index should never be -1.");

        logger.info("Start index for playlist : "+startIndex);

        Calendar now = DateTimeHelper.getNow();

        int index = startIndex;

        while (now.before(expectedEnd)) {

            Playable p = tracks.get(index++);
            now.add(Calendar.SECOND, (int) p.getDurationInSeconds());
            virtualTracks.add(p);

            if (index==tracks.size()) {
                if (loop_playlist)
                    index = 0;
                else
                    break;
            }
        }

        virtualPlaylist.setList(virtualTracks);

        logVirtualPlaylistInfo();
    }

    private void logVirtualPlaylistInfo() {
        Calendar expectedEnd = Calendar.getInstance();
        expectedEnd.add(Calendar.SECOND,(int)virtualPlaylist.getTotalDuration());

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.FULL, Locale.ITALY);


        logger.info("Virtual playlist generated : "+virtualPlaylist.getList().size()+" entries,"+virtualPlaylist.getTotalDuration()+" seconds");
        logger.info("Expected end : "+df.format(expectedEnd.getTime()));
    }

}