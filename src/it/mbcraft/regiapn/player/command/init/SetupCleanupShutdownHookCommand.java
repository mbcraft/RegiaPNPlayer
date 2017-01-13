package it.mbcraft.regiapn.player.command.init;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.management.flags.RunningFlag;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 02/07/16.
 */
public class SetupCleanupShutdownHookCommand implements ICommand,Runnable {
    @Override
    public void execute() {

        Thread th = new Thread(this);
        th.setName("Shutdown hook thread");

        Runtime.getRuntime().addShutdownHook(th);
    }

    @Override
    public void run() {
        RunningFlag.getInstance().reset();
    }
}
