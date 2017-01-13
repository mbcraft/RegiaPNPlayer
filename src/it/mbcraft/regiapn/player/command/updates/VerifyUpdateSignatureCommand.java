package it.mbcraft.regiapn.player.command.updates;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.regiapn.player.data.dao.GlobalConfigurationDAO;
import it.mbcraft.regiapn.player.data.dao.keys.IUpdateSignatureKeys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class VerifyUpdateSignatureCommand implements ICommand {

    private static final Logger logger = LogManager.getLogger(VerifyUpdateSignatureCommand.class);
    private final File updateExtractDir;
    private boolean valid = false;
    private int updateId = 0;
    private File signatureFile;

    private boolean validSecurityToken = false;
    private boolean validPlayerId = false;
    private boolean validClienteId = false;

    public VerifyUpdateSignatureCommand(File extractDir) {
        updateExtractDir = extractDir;
    }

    public boolean isValid() {
        return valid;
    }

    public int getUpdateId() {
        return updateId;
    }

    public void delete() {
        signatureFile.delete();
    }

    @Override
    public void execute() {
        String UPDATE_SIGNATURE_FILENAME = "update_signature.xml";
        signatureFile = new File(updateExtractDir, UPDATE_SIGNATURE_FILENAME);
        if (signatureFile.exists()) {
            Properties pt = new Properties();
            try (FileInputStream fis = new FileInputStream(signatureFile)) {
                pt.loadFromXML(fis);
                int clienteId = GlobalConfigurationDAO.getClienteId();
                int playerId = GlobalConfigurationDAO.getPlayerId();
                String playerSecurityToken = GlobalConfigurationDAO.getPlayerSecurityToken();

                validClienteId = Integer.parseInt(pt.getProperty(IUpdateSignatureKeys.CLIENTE_ID_KEY)) == clienteId;
                validPlayerId = Integer.parseInt(pt.getProperty(IUpdateSignatureKeys.PLAYER_ID_KEY)) == playerId;
                validSecurityToken = pt.getProperty(IUpdateSignatureKeys.PLAYER_SECURITY_TOKEN_KEY).equals(playerSecurityToken);

                valid = validClienteId && validPlayerId && validSecurityToken;
                updateId = Integer.parseInt(pt.getProperty(IUpdateSignatureKeys.UPDATE_ID_KEY));
            } catch (IOException ex) {
                logger.error("Error during verification of update signature", ex);
            }
        }
    }

    public String getInvalidDetails() {
        return "CLIENTE ID MATCHING : " + validClienteId + " , PLAYER ID MATCHING : " + validPlayerId + " , PLAYER SECURITY TOKEN MATCHING : " + validSecurityToken;
    }
}
