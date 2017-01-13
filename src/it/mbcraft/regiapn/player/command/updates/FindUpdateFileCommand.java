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

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class FindUpdateFileCommand implements ICommand {


    private final File search;
    private boolean found = false;
    private File updateFile = null;

    public FindUpdateFileCommand(File searchDir) {
        search = searchDir;
    }

    public File getUpdateFile() {
        return updateFile;
    }

    public boolean updateFound() {
        return found;
    }

    @Override
    public void execute() {

        File filesToSearch[] = search.listFiles();

        for (File a : filesToSearch) {
            String bundleSuffix = ".pcu";
            if (a.getName().endsWith(bundleSuffix)) {
                found = true;
                updateFile = a;
                return;
            }
        }
    }

}
