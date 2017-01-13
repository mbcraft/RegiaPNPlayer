package it.mbcraft.regiapn.player.command.updates.reboot;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.IDirectories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by marco on 11/05/16.
 */
public class GetLastSoftwareUpdateTimeCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(GetLastSoftwareUpdateTimeCommand.class);

    private long lastGlobalModifiedTime = 0;

    public long getSoftwareLastModifiedTime() {
        return lastGlobalModifiedTime;
    }

    @Override
    public void execute() {
        File f = new File(IDirectories.SOFTWARE_PATH);

        try {
            Files.walkFileTree(f.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (lastGlobalModifiedTime < file.toFile().lastModified())
                        lastGlobalModifiedTime = file.toFile().lastModified();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    logger.error("Error during check for reboot new software : unable to visit dist directory (" + file.toFile().getPath() + ").");
                    lastGlobalModifiedTime = Long.MAX_VALUE;
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            logger.error("Error during read software last modified time.", ex);
        }
    }

}
