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

/**
 * Ferma il server http e il player musicale.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class StopMediaPlayerCommand implements ICommand {

    @Override
    public void execute() {

        PlayState ps = PlayState.getCurrentPlayState();
        if (ps!=null) ps.stopPlaying();

        MediaPlayerService.getInstance().stop();
        MediaPrepareService.getInstance().stop();

    }

}
