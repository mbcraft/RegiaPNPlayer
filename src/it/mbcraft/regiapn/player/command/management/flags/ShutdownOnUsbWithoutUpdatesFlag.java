package it.mbcraft.regiapn.player.command.management.flags;

import it.mbcraft.libraries.process.flags.FileFlag;
import it.mbcraft.regiapn.player.command.IDirectories;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 07/07/16.
 */
public class ShutdownOnUsbWithoutUpdatesFlag extends FileFlag {
    public ShutdownOnUsbWithoutUpdatesFlag() {
        super(IDirectories.TMP_FLAGS_DIR_PATH + "no_upd_usb_shutdown.f");
    }

    private static FileFlag _instance = null;

    public static FileFlag getInstance() {
        if (_instance == null) _instance = new ShutdownOnUsbWithoutUpdatesFlag();
        return _instance;
    }
}

