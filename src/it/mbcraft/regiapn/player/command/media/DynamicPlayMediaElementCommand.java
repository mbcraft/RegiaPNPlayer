package it.mbcraft.regiapn.player.command.media;

import it.mbcraft.libraries.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 09/07/16.
 */
public class DynamicPlayMediaElementCommand implements ICommand {

    private final Logger logger = LogManager.getLogger(DynamicPlayMediaElementCommand.class);

    private ProcessBuilder pb = new ProcessBuilder();
    private final File toPlay;

    public DynamicPlayMediaElementCommand(File toPlay) {
        if (toPlay==null || !toPlay.exists())
            throw new InvalidParameterException("The media element to play can't be null and must exist!");
        this.toPlay = toPlay;
    }

    private void configureWithOMXPlayer() throws IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add("omxplayer");
        commandParts.add("-o");
        commandParts.add("both");

        String finalMediaPath = toPlay.getPath();

        logger.debug("Media path to play is : "+finalMediaPath);

        commandParts.add(finalMediaPath);
        pb = pb.directory(new File(".")).command(commandParts);
    }

    private void configureWithCVLCPlayer() throws IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add("cvlc");
        commandParts.add("--play-and-exit");

        String finalMediaPath = toPlay.getPath();

        logger.debug("Media path to play is : "+finalMediaPath);
        commandParts.add(finalMediaPath);
        pb = pb.directory(new File(".")).command(commandParts);
    }

    public ProcessBuilder getConfiguredProcessBuilder() {
        return pb;
    }

    @Override
    public void execute() {
        try {
            File homeOfPi = new File("/home/pi/");
            if (homeOfPi.exists())
                configureWithOMXPlayer();
            else
                configureWithCVLCPlayer();
        } catch (IOException ex) {
            logger.error("Error during file name resolution",ex);
        }
    }
}
