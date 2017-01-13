/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.command.io.*;
import it.mbcraft.libraries.encryption.CryptographicAlgorithm;
import it.mbcraft.regiapn.player.command.IDirectories;
import it.mbcraft.regiapn.player.command.management.flags.RestartFlag;
import it.mbcraft.regiapn.player.command.updates.reboot.CheckRestartForSoftwareUpdateNeededCommand;
import it.mbcraft.regiapn.player.command.updates.reboot.GetLastSoftwareUpdateTimeCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Properties;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class InstallUpdateCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(InstallUpdateCommand.class);

    @Override
    public void execute() {

        CheckRestartForSoftwareUpdateNeededCommand checkRestart = null;

        File downloadDir = new File(IDirectories.TMP_DOWNLOAD_DIR_PATH);
        //find update
        FindUpdateFileCommand upd = new FindUpdateFileCommand(downloadDir);
        upd.execute();

        if (upd.updateFound()) {

            GetLastSoftwareUpdateTimeCommand lastChangeBefore = new GetLastSoftwareUpdateTimeCommand();
            lastChangeBefore.execute();

            logger.info("Update file size : " + upd.getUpdateFile().length());
            //decrypt
            File extractFile = new File(upd.getUpdateFile().getParentFile(), upd.getUpdateFile().getName().replace(".pcu", ".zip"));
            DecryptFileCommand decrypt = new DecryptFileCommand(upd.getUpdateFile(), extractFile, CryptographicAlgorithm.SCA0, new Properties());
            decrypt.execute();

            logger.debug("Unpacked file size : " + extractFile.length());

            //unpack
            File extractDir = new File(IDirectories.TMP_EXTRACT_DIR_PATH);
            UnpackArchiveCommand extract = new UnpackArchiveCommand(extractFile, extractDir);
            extract.execute();

            VerifyUpdateSignatureCommand verify = new VerifyUpdateSignatureCommand(extractDir);
            verify.execute();

            if (verify.isValid()) {
                logger.info("Update package verification successful!");
                int newUpdateId = verify.getUpdateId();

                GetLastUpdateIdCommand currentUpdateId = new GetLastUpdateIdCommand();
                currentUpdateId.execute();

                if (currentUpdateId.getLastUpdateId() < newUpdateId) {
                    logger.info("Installing update ...");
                    verify.delete();
                    //merge

                    MergeFolderContentIntoDirCommand merge = new MergeFolderContentIntoDirCommand(extractDir, new File("."));
                    merge.execute();

                    SetLastUpdateIdCommand setLastUpdateId = new SetLastUpdateIdCommand(newUpdateId);
                    setLastUpdateId.execute();

                    GetLastSoftwareUpdateTimeCommand lastChangeAfter = new GetLastSoftwareUpdateTimeCommand();
                    lastChangeAfter.execute();

                    checkRestart = new CheckRestartForSoftwareUpdateNeededCommand(lastChangeBefore.getSoftwareLastModifiedTime(), lastChangeAfter.getSoftwareLastModifiedTime());
                    checkRestart.execute();

                    if (checkRestart.isRebootNeeded()) {
                        File extractDistDir = new File(extractDir, "dist");
                        ListFilesCommand lister = new ListFilesCommand(extractDistDir, true, new UpdateContentLogFilenameListener("Software update file changes :", extractDistDir, "Update "));
                        lister.execute();

                        MarkDirScriptsAsExecutablesCommand markScriptsExecutables = new MarkDirScriptsAsExecutablesCommand(new File("."));
                        markScriptsExecutables.execute();

                        CleanupUnusedJarLibrariesCommand cleanupLibs = new CleanupUnusedJarLibrariesCommand(new File("dist/RegiaPNPlayer.jar"),new File("dist/lib/"),true);
                        cleanupLibs.execute();
                    }
                } else {
                    logger.info("Update package is older than current update, skipping ...");
                }

            } else {
                logger.warn("Update package INVALID : " + verify.getInvalidDetails());
            }
            //cleanup
            DeleteFolderContentCommand deleteExtract = new DeleteFolderContentCommand(extractDir);
            deleteExtract.execute();
            DeleteFolderContentCommand deleteDownload = new DeleteFolderContentCommand(downloadDir);
            deleteDownload.execute();
            logger.debug("Deleting update package done.");

            if (checkRestart != null && checkRestart.isRebootNeeded()) {
                logger.debug("Software updated. Application restart is needed.");
                RestartFlag.getInstance().set();
            }
        }
    }

}
