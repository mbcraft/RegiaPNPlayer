package it.mbcraft.regiapn.player.utils;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 *
 * This class contains method to safely parse primitives from strings, avoiding exceptions.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.GENERIC)
public class SafePrimitiveParser {

    private static final Logger logger = LogManager.getLogger(SafePrimitiveParser.class);

    /**
     * Parses a boolean value, returning a default value if the parsing goes wrong.
     *
     * @param st The string to parse
     * @param default_value The default value
     * @param error_message The error message to print if the parsing goes wrong
     * @return the parsed value or the default value
     */
    public static boolean parseBoolean(String st,boolean default_value,String error_message) {
        try {
            return Boolean.parseBoolean(st);
        } catch (Exception ex) {
            logger.error(error_message,ex);
            return default_value;
        }
    }

    /**
     * Parses a long value, returning a default value if the parsing goes wrong.
     *
     * @param st The string to parse
     * @param default_value The default value
     * @param error_message The error message to print if the parsing goes wrong
     * @return the parsed value or the default value
     */
    public static long parseLong(String st,long default_value,String error_message) {
        try {
            return Long.parseLong(st);
        } catch (Exception ex) {
            logger.error(error_message,ex);
            return default_value;
        }
    }

    /**
     * Parses a double value, returning a default value if the parsing goes wrong.
     *
     * @param st The string to parse
     * @param default_value The default value
     * @param error_message The error message to print if the parsing goes wrong
     * @return the parsed value or the default value
     */
    public static double parseDouble(String st,double default_value,String error_message) {
        try {
            return Double.parseDouble(st);
        } catch (Exception ex) {
            logger.error(error_message,ex);
            return default_value;
        }
    }

    /**
     * Parses a float value, returning a default value if the parsing goes wrong.
     *
     * @param st The string to parse
     * @param default_value The default value
     * @param error_message The error message to print if the parsing goes wrong
     * @return the parsed value or the default value
     */
    public static float parseFloat(String st,float default_value,String error_message) {
        try {
            return Float.parseFloat(st);
        } catch (Exception ex) {
            logger.error(error_message,ex);
            return default_value;
        }
    }

    /**
     * Parses an int value, returning a default value if the parsing goes wrong.
     *
     * @param st The string to parse
     * @param default_value The default value
     * @param error_message The error message to print if the parsing goes wrong
     * @return the parsed value or the default value
     */
    public static int parseInt(String st,int default_value,String error_message) {
        try {
            return Integer.parseInt(st);
        } catch (Exception ex) {
            logger.error(error_message,ex);
            return default_value;
        }
    }

    /**
     * Parses a string daytime in the format hh:mm, returning a default value if the parsing
     * goes wrong, or the initial string if all is ok
     *
     * @param st The time to parse
     * @param default_value The default value
*    * @param error_message The error message to print if the parsing goes wrong
     * @return The initial value if all is ok, or the default value if something goes wrong
     */
    public static String[] parseDayTime(String st,String default_value,String error_message) {
        String[] result = st.split(":");
        if (result!=null && result.length==2) {
            try {
                int hour = Integer.parseInt(result[0]);
                int minute = Integer.parseInt(result[1]);
                if (hour<0 || hour>23) throw new NumberFormatException("Invalid hour value : "+hour);
                if (minute<0 || minute>59) throw new NumberFormatException("Invalid hour value : "+hour);

                return st.split(":");
            } catch (Exception ex) {
                logger.error(error_message,ex);
            }
        }
        return parseDayTime(default_value,"00:00","Unable to parse the DEFAULT dayTime!");
    }



}
