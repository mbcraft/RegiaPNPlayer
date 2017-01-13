/*
 * 
 *    Copyright MBCRAFT di Marco Bagnaresi - © 2015
 *    All rights reserved - Tutti i diritti riservati
 * 
 *    Mail : info [ at ] mbcraft [ dot ] it 
 *    Web : http://www.mbcraft.it
 * 
 */

package it.mbcraft.regiapn.player.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * @author Marco Bagnaresi <marco.bagnaresi@gmail.com>
 */
public class Output {

    private static final Logger logger = LogManager.getLogger("output");

    public static void writeLog(InputStream stream, File dest) {
        BufferedReader bif = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream, 100 * 100)));
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(dest, false));
            String line = null;
            while ((line = bif.readLine()) != null) {
                pw.println(line);
            }
            pw.flush();
        } catch (IOException e) {
            logger.info("Exception during log write - process terminated");
        } finally {
            try {
                bif.close();
            } catch (IOException e) {
                logger.info("Exception during process input close - process terminated");
            }
            pw.close();
        }

    }

    public static void console(String text) {
        System.out.println(text);
    }
}
