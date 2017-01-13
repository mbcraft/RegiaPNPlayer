package it.mbcraft.regiapn.player.command.media;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.process.GetCurrentUserCommand;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.PlaylistDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by marco on 09/05/16.
 */
public class CheckConfigsForPlayerCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(CheckConfigsForPlayerCommand.class);
    private boolean configValid;

    public boolean isConfigurationValid() {
        return configValid;
    }

    @Override
    public void execute() {

        if (!GlobalConfigurationDAO.getInstance().exists())
            logger.warn("Global configuration not found!");

        if (!PlayerConfigurationDAO.getInstance().exists())
            logger.warn("Player configuration not found!");

        if (!PlaylistDAO.getInstance().exists())
            logger.warn("Playlist not found!");

        GetCurrentUserCommand getUser = new GetCurrentUserCommand();
        getUser.execute();

        configValid = GlobalConfigurationDAO.getInstance().exists() && PlayerConfigurationDAO.getInstance().exists() && PlaylistDAO.getInstance().exists();
    }
}
