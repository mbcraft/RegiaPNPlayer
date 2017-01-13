package it.mbcraft.regiapn.player.command.audio;

import it.mbcraft.libraries.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by marco on 10/06/16.
 */
public class SetVolumeCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(SetVolumeCommand.class);

    public static final int MAX_VALUE = 400;
    public static final int MIN_VALUE = -10239;

    private final int volume;

    public SetVolumeCommand() {
        this(0);
    }

    public SetVolumeCommand(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new InvalidParameterException("The volume value must be between " + MIN_VALUE + " and " + MAX_VALUE + ". V : " + value);
        volume = value;
    }

    @Override
    public void execute() {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("amixer", "cset", "numid=1,iface=MIXER,name='PCM Playback Volume'", "" + volume);
        try {
            pb.start();
        } catch (IOException e) {
            logger.catching(e);
        }
    }
}
