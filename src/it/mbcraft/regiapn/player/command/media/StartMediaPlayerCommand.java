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

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.media.MediaPlayerService;
import it.mbcraft.regiapn.player.media.MediaPrepareService;
import it.mbcraft.regiapn.player.media.PlayState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lancia il server http e il player musicale.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class StartMediaPlayerCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(StartMediaPlayerCommand.class);

    @Override
    public void execute() {

        CheckMediaPlayerStateCommand check_player_state = new CheckMediaPlayerStateCommand();
        check_player_state.execute();

        if (!check_player_state.isMediaPlayerActive()) {

            GenerateVirtualPlaylistFromConfigCommand virt_playlist_gen = new GenerateVirtualPlaylistFromConfigCommand();
            virt_playlist_gen.execute();

            PlayState ps = new PlayState(virt_playlist_gen.getVirtualPlaylist());

            MediaPlayerService.getInstance().start(ps);
            MediaPrepareService.getInstance().start(ps);

        } else {
            logger.warn("Media Player already active!");
        }
    }

}
