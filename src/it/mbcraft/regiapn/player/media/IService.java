/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.media;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public interface IService {

    /**
     * Lancia il servizio
     */
    void start();

    /**
     * Ferma il servizio
     */
    void stop();

    /**
     * Controlla se il servizio è attivo. Se necessario effettua le dovute
     * pulizie.
     */
    boolean isRunning();
}
