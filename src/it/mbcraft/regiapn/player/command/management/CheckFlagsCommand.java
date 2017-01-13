package it.mbcraft.regiapn.player.command.management;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.process.flags.FileFlag;
import it.mbcraft.regiapn.player.Main;
import it.mbcraft.regiapn.player.command.management.flags.*;

/**
 * Created by marco on 11/06/16.
 */
public class CheckFlagsCommand implements ICommand {

    private final FileFlag statusFlag = LogStatusFlag.getInstance();
    private final FileFlag stopFlag = StopFlag.getInstance();
    private final FileFlag restartFlag = RestartFlag.getInstance();
    private final FileFlag updateFlag = UpdateFlag.getInstance();
    private final FileFlag reloadFlag = ReloadFlag.getInstance();

    @Override
    public void execute() {

        if (stopFlag.isSet()) {
            stopFlag.reset();
            Main.getInstance().shutdownCurrentInstance();
        }

        if (restartFlag.isSet()) {
            Main.getInstance().shutdownCurrentInstance();
        }

        if (reloadFlag.isSet()) {
            reloadFlag.reset();
            Main.getInstance().reloadCurrentInstance();
        }

        if (updateFlag.isSet()) {
            updateFlag.reset();
            Main.getInstance().updateCurrentInstance();
        }

        if (statusFlag.isSet()) {
            statusFlag.reset();
            Main.getInstance().logCurrentStatus();
        }

    }
}
