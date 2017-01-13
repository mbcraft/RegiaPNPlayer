/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.data.models;

import it.mbcraft.regiapn.player.utils.PathHelper;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class Spot extends Playable {

    @Override
    public String getRealPath() {
        return PathHelper.getComputedString(getPath());
    }
}
