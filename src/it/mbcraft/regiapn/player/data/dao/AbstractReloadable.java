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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.GENERIC)
abstract class AbstractReloadable implements IReloadable {

    private long lastModificationTime = 0;

    protected abstract File getResourceFile();

    protected abstract void reloadImpl(File f);

    @Override
    public final boolean exists() {
        File f = getResourceFile();
        return f!=null && f.exists() && f.length() > 0;
    }

    @Override
    public final boolean needsReloading() {
        return exists() && getResourceFile().lastModified() > lastModificationTime;
    }

    @Override
    public final void reload() {
        File f = getResourceFile();

        reloadImpl(f);

        lastModificationTime = f.lastModified();

        fireConfigurationReloaded();
    }

    @Override
    public final long getModificationTime() {
        return lastModificationTime;
    }

    private List<IConfigurationReloadListener> listeners = new ArrayList<>();

    private void fireConfigurationReloaded() {
        for (IConfigurationReloadListener listener : listeners) {
            listener.configurationReloaded();
        }
    }

    public void addConfigurationReloadListener(IConfigurationReloadListener listener) {
        listeners.add(listener);
    }

    public void removeConfigurationReloadListener(IConfigurationReloadListener listener) {
        listeners.remove(listener);
    }
}
