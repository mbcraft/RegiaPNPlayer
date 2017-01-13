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

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class FindUpdatePackageSignatureCommand implements ICommand {

    private final String UPDATE_SIGNATURE_PREFIX = "update_signature";
    private final File updateRoot;
    private File updateSignature;

    public FindUpdatePackageSignatureCommand(File packageRoot) {
        updateRoot = packageRoot;
    }

    public boolean hasFoundSignature() {
        return updateSignature != null;
    }

    public File getUpdateSignatureFile() {
        return updateSignature;
    }

    @Override
    public void execute() {
        File[] found = updateRoot.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith(UPDATE_SIGNATURE_PREFIX)) return true;
                else return false;
            }
        });

        if (found.length > 0) {
            updateSignature = found[0];
        }
    }

}
