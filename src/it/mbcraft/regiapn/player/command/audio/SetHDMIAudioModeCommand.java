package it.mbcraft.regiapn.player.command.audio;

import it.mbcraft.libraries.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by marco on 09/06/16.
 */
public class SetHDMIAudioModeCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(SetAnalogAudioModeCommand.class);

    public static final String HDMI_MODE = "2";

    @Override
    public void execute() {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("amixer", "cset", "numid=3,iface=MIXER,name='PCM Playback Route'", HDMI_MODE);
        try {
            pb.start();
        } catch (IOException e) {
            logger.error("Unable to set HDMI audio mode.", e);
        }
    }
}
