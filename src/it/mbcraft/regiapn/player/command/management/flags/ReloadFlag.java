package it.mbcraft.regiapn.player.command.management.flags;

import it.mbcraft.libraries.process.flags.FileFlag;
import it.mbcraft.regiapn.player.command.IDirectories;

/**
 * Created by marco on 11/06/16.
 */
public class ReloadFlag extends FileFlag {
    protected ReloadFlag() {
        super(IDirectories.TMP_FLAGS_DIR_PATH + "reload.now");
    }

    private static FileFlag _instance = null;

    public static FileFlag getInstance() {
        if (_instance == null) _instance = new ReloadFlag();
        return _instance;
    }
}
