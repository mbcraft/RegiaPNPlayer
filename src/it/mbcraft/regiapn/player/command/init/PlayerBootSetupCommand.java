package it.mbcraft.regiapn.player.command.init;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.command.io.DeleteFolderCommand;
import it.mbcraft.regiapn.player.command.IDirectories;
import it.mbcraft.regiapn.player.command.management.flags.ShutdownOnUsbWithoutUpdatesFlag;

import java.io.File;

/**
 * Created by marco on 11/06/16.
 */
public class PlayerBootSetupCommand implements ICommand {
    @Override
    public void execute() {

        File tmpDir = new File(IDirectories.TMP_DIR_PATH);
        //only try to delete folders in tmp only if tmp exists ...
        if (tmpDir.exists()) {
            DeleteFolderCommand deleteDownload = new DeleteFolderCommand(new File(IDirectories.TMP_DOWNLOAD_DIR_PATH));
            deleteDownload.execute();

            DeleteFolderCommand deleteExtract = new DeleteFolderCommand(new File(IDirectories.TMP_EXTRACT_DIR_PATH));
            deleteExtract.execute();

            DeleteFolderCommand deleteState = new DeleteFolderCommand(new File(IDirectories.TMP_STATE_DIR_PATH));
            deleteState.execute();

            DeleteFolderCommand deleteFlags = new DeleteFolderCommand(new File(IDirectories.TMP_FLAGS_DIR_PATH));
            deleteFlags.execute();
        }

        //Keep all logs
        //... nothing to do ...

        SetupTmpFoldersCommand setupTmp = new SetupTmpFoldersCommand();
        setupTmp.execute();

        ShutdownOnUsbWithoutUpdatesFlag.getInstance().set();

        SetupDataFolderCommand setupData = new SetupDataFolderCommand();
        setupData.execute();
    }
}
