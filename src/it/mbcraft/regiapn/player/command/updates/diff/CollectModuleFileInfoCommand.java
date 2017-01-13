package it.mbcraft.regiapn.player.command.updates.diff;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by marco on 13/05/16.
 */
@CodeClassification(CodeType.GENERIC)
public class CollectModuleFileInfoCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(CollectModuleFileInfoCommand.class);

    private final String myName;
    private final File myRoot;

    private List<FileInfo> collectedFileInfo = new ArrayList<>();

    public CollectModuleFileInfoCommand(String name, File root) {
        myName = name;
        myRoot = root;
    }

    public String getModuleName() {
        return myName;
    }

    public List<FileInfo> getCollectedFileInfo() {
        return collectedFileInfo;
    }

    @Override
    public void execute() {
        try {
            Files.walkFileTree(myRoot.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    collectedFileInfo.add(new FileInfo(myRoot, file.toFile()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException ex) {
            logger.error("Errore durante la visita dei file del modulo " + myName, ex);
        }

        collectedFileInfo = Collections.unmodifiableList(collectedFileInfo);
    }


}
