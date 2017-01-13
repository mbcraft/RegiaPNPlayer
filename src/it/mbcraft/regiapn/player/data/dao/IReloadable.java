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

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.GENERIC)
public interface IReloadable {

    /**
     * Checks if the resource exists.
     *
     * @return true if the resource exist, false otherwise.
     */
    boolean exists();

    /**
     * @return true if the resource needs to be reloaded, false otherwise.
     */
    boolean needsReloading();

    /**
     * Reloads the resource
     */
    void reload();

    /**
     * Returns the modification time of the configuration
     *
     * @return a long rapresenting the time of the last change in milliseconds from epoch
     */
    long getModificationTime();
}
