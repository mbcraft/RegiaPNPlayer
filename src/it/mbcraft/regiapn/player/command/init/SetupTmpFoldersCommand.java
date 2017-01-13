/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */
package it.mbcraft.regiapn.player.command.init;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.IDirectories;

import java.io.File;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.GENERIC)
public class SetupTmpFoldersCommand implements ICommand {

    private final File extractDir;
    private final File downloadDir;
    private final File logDir;
    private final File flagsDir;
    private final File stateDir;

    public SetupTmpFoldersCommand() {
        extractDir = new File(IDirectories.TMP_EXTRACT_DIR_PATH);
        downloadDir = new File(IDirectories.TMP_DOWNLOAD_DIR_PATH);
        logDir = new File(IDirectories.TMP_LOG_DIR_PATH);
        flagsDir = new File(IDirectories.TMP_FLAGS_DIR_PATH);
        stateDir = new File(IDirectories.TMP_STATE_DIR_PATH);
    }

    @Override
    public void execute() {
        extractDir.mkdirs();
        downloadDir.mkdirs();
        logDir.mkdirs();
        flagsDir.mkdirs();
        stateDir.mkdirs();
    }

}
