/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.command.management;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.command.init.CheckAndOverwriteGlobalConfigCommand;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.IReloadable;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.PlaylistDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
@CodeClassification(CodeType.SPECIFIC)
public class ReloadConfigurationsCommand implements ICommand {

    @Override
    public void execute() {

        //ricarico tutte le configurazioni
        List<IReloadable> reloadables = new ArrayList<>();

        //Tutti gli elementi ricaricabili devono essere inseriti qui nell'ordine.
        reloadables.add(GlobalConfigurationDAO.getInstance());
        reloadables.add(PlayerConfigurationDAO.getInstance());
        reloadables.add(PlaylistDAO.getInstance());


        for (IReloadable rel : reloadables) {
            if (rel.exists() && rel.needsReloading())
                rel.reload();
        }

        //se necessario faccio l'overwrite della config globale
        //TODO decidere se utilizzare direttamente l'update con file in data/global.com invece di questa sostituzione
        CheckAndOverwriteGlobalConfigCommand check_global_config_ow = new CheckAndOverwriteGlobalConfigCommand();
        check_global_config_ow.execute();

    }

}
