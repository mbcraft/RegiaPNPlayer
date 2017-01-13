package it.mbcraft.regiapn.player.data.dao;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.GENERIC)
public interface IConfigurationReloadListener {

    void configurationReloaded();

}
