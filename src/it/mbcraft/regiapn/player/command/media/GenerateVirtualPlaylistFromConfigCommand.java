package it.mbcraft.regiapn.player.command.media;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.PlaylistDAO;
import it.mbcraft.regiapn.player.data.models.Playlist;
import it.mbcraft.regiapn.player.utils.DateTimeHelper;

import java.util.Calendar;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 09/07/16.
 */
public class GenerateVirtualPlaylistFromConfigCommand implements ICommand {

    private VirtualPlaylistGeneratorCommand generator;

    public Playlist getVirtualPlaylist() {
        return generator.getVirtualPlaylist();
    }

    @Override
    public void execute() {

        Playlist pl = PlaylistDAO.get();
        Calendar start = DateTimeHelper.getConfiguredPlayStartTime();
        Calendar end = DateTimeHelper.getConfiguredPlayEndTime();
        boolean start_from_beginning = PlayerConfigurationDAO.getStartFromBeginning();
        boolean loop_playlist = PlayerConfigurationDAO.getLoopPlaylist();

        //adjust start and end if start is far away in time
        Calendar now = DateTimeHelper.getNow();
        boolean today = now.after(start);
        if (!today) {
            start.add(Calendar.DAY_OF_YEAR,-1);
            end.add(Calendar.DAY_OF_YEAR,-1);
        }

        generator = new VirtualPlaylistGeneratorCommand(pl,start,end,start_from_beginning,loop_playlist);
        generator.execute();
    }
}
