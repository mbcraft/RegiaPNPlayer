package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.IDirectories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by marco on 09/05/16.
 */
public class GetLastUpdateIdCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(GetLastUpdateIdCommand.class);

    private int lastUpdateId = 0;

    public int getLastUpdateId() {
        return lastUpdateId;
    }

    @Override
    public void execute() {
        File lastUpdate = new File(IDirectories.LAST_UPDATE_PATH);
        if (lastUpdate.exists()) {
            try {
                byte content[] = Files.readAllBytes(lastUpdate.toPath());
                String stringValue = new String(content);
                lastUpdateId = Integer.parseInt(stringValue);
            } catch (IOException e) {
                logger.error("Errore durante la lettura dell'id dell'ultimo update");
            }
        }
    }
}
