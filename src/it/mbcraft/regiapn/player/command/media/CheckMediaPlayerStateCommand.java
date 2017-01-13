package it.mbcraft.regiapn.player.command.media;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.media.PlayState;

/**
 * Created by marco on 10/05/16.
 */
public class CheckMediaPlayerStateCommand implements ICommand {

    private boolean started = false;

    public boolean isMediaPlayerActive() {
        return started;
    }

    @Override
    public void execute() {
        PlayState ps = PlayState.getCurrentPlayState();
        started = ps!=null && ps.isPlaying();
    }
}
