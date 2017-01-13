package it.mbcraft.regiapn.player.data.dao.keys;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.SPECIFIC)
public interface IBootConfigurationKeys {
    String REGISTRATION_PROTOCOL_KEY = "registration_protocol";
    String REGISTRATION_HOST_KEY = "registration_host";
    String REGISTRATION_PAGE_KEY = "registration_page";
    String REGISTRATION_CODE_KEY = "registration_code";
    //player_boot.xml is in data/
    String DATA_PLAYER_BOOT_PATH = "data/player_boot.xml";
    String ABS_BOOT_PLAYER_BOOT_PATH = "/boot/player_boot.xml";
}
