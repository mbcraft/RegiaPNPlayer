/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player;


import it.mbcraft.libraries.command.usbdrive.FindUsbDeviceListRootCommand;
import it.mbcraft.libraries.command.usbdrive.UpdateUsbDrivesCommand;
import it.mbcraft.libraries.service.IManageable;
import it.mbcraft.regiapn.player.command.init.PlayerBootSetupCommand;
import it.mbcraft.regiapn.player.command.init.SetupCleanupShutdownHookCommand;
import it.mbcraft.regiapn.player.command.management.ReloadConfigurationsCommand;
import it.mbcraft.regiapn.player.command.management.flags.*;
import it.mbcraft.regiapn.player.command.notifications.RegisterOrNotifyStartedCommand;
import it.mbcraft.regiapn.player.command.updates.ExecuteFullRemoteUpdateCommand;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.utils.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Gestisce il parsing dei comandi e consente il
 * management dell'applicazione.
 *
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class Main implements IManageable {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static Main _instance = null;

    public static final String SOFTWARE_NAME = "RegiaPN Player";
    public static final String SOFTWARE_VERSION = "1.5.2";
    public static final String SOFTWARE_VENDOR = "MBCRAFT";

    private RegiaPNPlayer regia_pn = null;

    private Main() {
        PlayerBootSetupCommand boot = new PlayerBootSetupCommand();
        boot.execute();
    }

    /**
     * Ritorna "l'istanza del programma".
     *
     * @return L'istanza del programma
     */
    public static Main getInstance() {
        if (_instance == null) {
            _instance = new Main();
        }
        return _instance;
    }

    /**
     * Avvia il player/server.
     */
    private void start() {

        //setup shutdown hook
        SetupCleanupShutdownHookCommand setupHook = new SetupCleanupShutdownHookCommand();
        setupHook.execute();

        if (!RunningFlag.getInstance().isSet()) {
            //setting the running flag and callback to reset it
            RunningFlag.getInstance().set();

            //find real usb root
            FindUsbDeviceListRootCommand findRealUsbRoot = new FindUsbDeviceListRootCommand();
            findRealUsbRoot.execute();
            //setup usb detection - retains root
            UpdateUsbDrivesCommand setupUsbDetection = new UpdateUsbDrivesCommand(findRealUsbRoot.getUsbDeviceListRoot());
            setupUsbDetection.execute();

            //register or notify
            RegisterOrNotifyStartedCommand registerOrNotify = new RegisterOrNotifyStartedCommand();
            registerOrNotify.execute();
            //update if no configuration is available
            if (!PlayerConfigurationDAO.getInstance().exists()) {
                ExecuteFullRemoteUpdateCommand update = new ExecuteFullRemoteUpdateCommand();
                update.execute();
            }

            //load configuration
            ReloadConfigurationsCommand load_conf = new ReloadConfigurationsCommand();
            load_conf.execute();

            regia_pn = RegiaPNPlayer.getInstance();
            regia_pn.start();
        } else {
            Output.console("RegiaPNPlayer already started.");
        }
    }

    @Override
    public void reloadCurrentInstance() {
        ReloadFlag.getInstance().reset();
        ReloadConfigurationsCommand reload = new ReloadConfigurationsCommand();
        reload.execute();
    }

    @Override
    public void restartCurrentInstance() {
        RestartFlag.getInstance().reset();
        shutdownCurrentInstance();
    }

    @Override
    public void updateCurrentInstance() {
        UpdateFlag.getInstance().reset();
        ExecuteFullRemoteUpdateCommand update = new ExecuteFullRemoteUpdateCommand();
        update.execute();
    }


    @Override
    public void logCurrentStatus() {
        LogStatusFlag.getInstance().reset();
        regia_pn.logStatus();
    }

    /**
     * Ferma l'istanza corrente del player/server.
     */
    @Override
    public void shutdownCurrentInstance() {
        //stops the core (scheduler)
        regia_pn.stop();
        //sets the running flag to false : only next command will be read from management thread
        RunningFlag.getInstance().reset();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        logger.info("------- STARTING REGIAPN PLAYER -------");

        Main.getInstance().start();
    }

}
