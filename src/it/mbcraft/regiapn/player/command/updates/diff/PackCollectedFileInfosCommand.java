package it.mbcraft.regiapn.player.command.updates.diff;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by marco on 13/05/16.
 */
@CodeClassification(CodeType.GENERIC)
public class PackCollectedFileInfosCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(PackCollectedFileInfosCommand.class);

    private final List<CollectModuleFileInfoCommand> myCollectorsIterator;
    private final HashMap<String, String> pt = new HashMap();
    private Map<String, String> finalResult;

    public PackCollectedFileInfosCommand(List<CollectModuleFileInfoCommand> collectors) {
        myCollectorsIterator = collectors;
    }

    public Map<String, String> getPackedModulesFileInfo() {
        return finalResult;
    }

    private String encodePath(String path) {
        try {
            return URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Impossibile codificare i dati per l'invio http", e);
            throw new IllegalStateException("Impossibile preparare i parametri per l'invio.");
        }
    }

    @Override
    public void execute() {
        int module_count = 0;
        Iterator<CollectModuleFileInfoCommand> it = myCollectorsIterator.iterator();
        while (it.hasNext()) {
            CollectModuleFileInfoCommand collector = it.next();
            pt.put("module_" + module_count + "__name", collector.getModuleName());
            int file_count = 0;
            Iterator<FileInfo> fileInfos = collector.getCollectedFileInfo().iterator();
            while (fileInfos.hasNext()) {
                FileInfo fi = fileInfos.next();

                pt.put("m_" + module_count + "_" + file_count + "_p", encodePath(fi.getRelativePath()));
                pt.put("m_" + module_count + "_" + file_count + "_s", "" + fi.getSize());
                pt.put("m_" + module_count + "_" + file_count + "_d", fi.getSha1Digest());
                file_count++;
            }

            pt.put("module_" + module_count + "__file_count", "" + file_count);

            module_count++;
        }

        pt.put("modules_count", "" + module_count);

        finalResult = Collections.unmodifiableMap(pt);
    }
}
