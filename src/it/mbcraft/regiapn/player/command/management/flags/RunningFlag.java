/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.management.flags;

import it.mbcraft.libraries.process.flags.FileFlag;
import it.mbcraft.regiapn.player.command.IDirectories;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class RunningFlag extends FileFlag {

    private RunningFlag() {
        super(IDirectories.TMP_FLAGS_DIR_PATH + "instance.run");
    }

    private static FileFlag _instance = null;

    public static FileFlag getInstance() {
        if (_instance == null) _instance = new RunningFlag();
        return _instance;
    }
}
