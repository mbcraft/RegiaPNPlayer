package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.IDirectories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Created by marco on 09/05/16.
 */
public class SetLastUpdateIdCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(SetLastUpdateIdCommand.class);

    private int lastUpdateId = 0;

    public SetLastUpdateIdCommand(int lastId) {
        lastUpdateId = lastId;
    }

    @Override
    public void execute() {
        File lastUpdate = new File(IDirectories.LAST_UPDATE_PATH);
        if (!lastUpdate.exists() || (lastUpdate.exists() && lastUpdate.canWrite())) {
            try {
                Integer value = new Integer(lastUpdateId);

                Files.write(lastUpdate.toPath(), value.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.DSYNC, StandardOpenOption.TRUNCATE_EXISTING);

            } catch (IOException e) {
                logger.error("Errore durante la lettura dell'id dell'ultimo update");
            }
        }
    }
}
