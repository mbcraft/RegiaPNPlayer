package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.io.ListFilesCommand.IFilenameListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.security.InvalidParameterException;

/**
 * Created by marco on 11/06/16.
 */
public class UpdateContentLogFilenameListener implements IFilenameListener {

    private final Logger logger = LogManager.getLogger(UpdateContentLogFilenameListener.class);

    private final String myTitle;
    private final String myPrefix;

    private final Path rootPath;

    public UpdateContentLogFilenameListener(String title, File root, String prefix) {
        if (root == null || !root.isDirectory())
            throw new InvalidParameterException("Root path is null or not a directory!");

        myTitle = title == null ? "" : title;
        myPrefix = prefix == null ? "" : prefix;
        rootPath = root.toPath();
    }

    public void init() {
        logger.info(myTitle);
    }

    @Override
    public void fileFound(Path file) {
        logger.info(myPrefix + " file found : " + rootPath.relativize(file));
    }

    @Override
    public void directoryFound(Path dir) {
        logger.info(myPrefix + " directory found : " + rootPath.relativize(dir));
    }
}
