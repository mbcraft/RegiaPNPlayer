package it.mbcraft.regiapn.player.data.dao.keys;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.SPECIFIC)
public interface IUpdateSignatureKeys {
    String UPDATE_ID_KEY = "update_id"; //numero dell'update
    String CLIENTE_ID_KEY = "cliente_id";   //id del cliente
    String PLAYER_ID_KEY = "player_id"; //id del player
    String PLAYER_SECURITY_TOKEN_KEY = "player_security_token";   //security token del player, non viene mai modificato
}
