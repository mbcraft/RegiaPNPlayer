package it.mbcraft.regiapn.player.data.dao.keys;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.SPECIFIC)
public interface IGlobalConfigurationKeys {
    //nome del file
    String DEFAULT_GLOBAL_CONFIG_PATH = "data/global.xml";
    //id e messaggio
    String CLIENTE_ID_KEY = "cliente_id";   //id del cliente
    String PLAYER_ID_KEY = "player_id"; //id del player
    //security tokens
    String CLIENTE_SECURITY_TOKEN_KEY = "cliente_security_token";   //security token del cliente, non viene mai modificato
    String PLAYER_SECURITY_TOKEN_KEY = "player_security_token";   //security token del player, non viene mai modificato
    String MESSAGE_KEY = "message"; //chiave del messaggio
    //embedded http server
    String HTTP_ROOT_KEY = "http_root"; //cartella da usare come root del file server http
    //nomi file
    String PLAYLIST_PATH_KEY = "playlist_path"; //nome con percorso da http root del file della playlist
    String CONFIG_PATH_KEY = "config_path"; //nome con percorso da http root del file di configurazione
    String OVERRIDE_GLOBAL_CONFIG_PATH_KEY = "override_global_config_path"; //nome con percorso da http root del file che effettua l'override della configurazione globale.
    //update
    String UPDATE_REMOTE_HOST_KEY = "update_remote_host"; //host remoto dove richiedere e scaricare gli aggiornamenti
    String UPDATE_CHECK_ADDRESS_KEY = "update_check_address";    //indirizzo a cui controllare il pacchetto con gli aggiornamenti
    String UPDATE_REQUEST_ADDRESS_KEY = "update_request_address";    //indirizzo a cui richiedere il pacchetto con gli aggiornamenti
    String UPDATE_DOWNLOAD_ADDRESS_KEY = "update_download_address";    //indirizzo a cui scaricare il pacchetto di aggiornamenti
    //notification addresses
    String INSTANCE_STARTED_NOTIFICATION_ADDRESS_KEY = "instance_started_notification_address";    //indirizzo a cui segnalare l'avvio dell'istanza configurato
    String SERVICE_STARTED_NOTIFICATION_ADDRESS_KEY = "service_started_notification_address";    //indirizzo a cui segnalare l'avvio di un servizio
    String SERVICE_STOPPED_NOTIFICATION_ADDRESS_KEY = "service_stopped_notification_address";    //indirizzo a cui segnalare lo stop di un servizio
    String UPDATE_STARTED_NOTIFICATION_ADDRESS_KEY = "update_started_notification_address";  //indirizzo a cui segnalare l'inizio del download dei files
    String UPDATE_COMPLETED_NOTIFICATION_ADDRESS_KEY = "update_completed_notification_address";  //indirizzo a cui segnalare la fine del download dei files
    String ERROR_NOTIFICATION_ADDRESS_KEY = "error_notification_address"; //indirizzo a cui notificare gli errori
    //diff modules specification
    String MODULES_DIFF_SPECIFICATION = "modules_diff_specification"; //specifica dei moduli di cui fare la diff incrementale
}
