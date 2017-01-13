package it.mbcraft.regiapn.player.utils;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
public class TestDateFormat {
    @Test
    public void testDateFormat() {
        Calendar cl = Calendar.getInstance();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.LONG, Locale.ITALY);
        System.out.println(df.format(cl.getTime()));
    }
}
