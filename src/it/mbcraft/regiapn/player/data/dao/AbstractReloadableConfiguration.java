/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.data.dao;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.GENERIC)
abstract class AbstractReloadableConfiguration extends AbstractReloadable implements IConfiguration {

    private static final Logger logger = LogManager.getLogger(AbstractReloadableConfiguration.class);

    protected Properties pt = new Properties();

    /**
     * Carica la configurazione in memoria.
     *
     * @param f The resource file
     */
    @Override
    protected void reloadImpl(File f) {
        pt = new Properties();

        try (FileInputStream fis = new FileInputStream(f)) {
            //reloads the global config
            pt.loadFromXML(fis);

        } catch (IOException ex) {
            logger.error("Error loading configuration", ex);
        }
    }

    @Override
    public boolean has(String key) {
        return pt.containsKey(key);
    }

    @Override
    public String get(String key) {
        return pt.getProperty(key);
    }

    @Override
    public List<String> keys() {
        Enumeration<Object> keys = pt.keys();
        List<String> result = new ArrayList<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            result.add(key);
        }
        return result;
    }

}
