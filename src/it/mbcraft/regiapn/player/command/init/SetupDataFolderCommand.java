package it.mbcraft.regiapn.player.command.init;

import it.mbcraft.libraries.command.ICommand;

import java.io.File;

/**
 * Created by marco on 08/06/16.
 */
public class SetupDataFolderCommand implements ICommand {

    private final File dataDir;

    public SetupDataFolderCommand() {
        dataDir = new File("data/");
    }

    @Override
    public void execute() {
        dataDir.mkdir();
    }
}
