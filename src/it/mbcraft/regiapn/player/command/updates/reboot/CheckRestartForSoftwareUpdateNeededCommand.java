package it.mbcraft.regiapn.player.command.updates.reboot;

import it.mbcraft.libraries.command.ICommand;

/**
 * Created by marco on 11/05/16.
 */
public class CheckRestartForSoftwareUpdateNeededCommand implements ICommand {

    private final long beforeUpdateTime;
    private final long afterUpdateTime;

    private boolean rebootNeeded = false;

    public CheckRestartForSoftwareUpdateNeededCommand(long beforeUpdate, long afterUpdate) {
        beforeUpdateTime = beforeUpdate;
        afterUpdateTime = afterUpdate;
    }

    public boolean isRebootNeeded() {
        return rebootNeeded;
    }

    @Override
    public void execute() {
        rebootNeeded = beforeUpdateTime < afterUpdateTime;
    }
}
