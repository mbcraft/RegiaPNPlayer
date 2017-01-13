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

import java.util.List;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.GENERIC)
public interface IConfiguration {

    List<String> keys();

    boolean has(String key);

    String get(String key);

}
