package it.mbcraft.regiapn.player.utils;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 22/07/16.
 */
public class TestDateTimeHelper {

    @Test
    public void testGetTimeFromHourAndMinutes() {
        String[] time= new String[]{"05","43"};

        Calendar now = Calendar.getInstance();

        Calendar result = DateTimeHelper.getTimeFromHourAndMinute(time,0);

        Assert.assertEquals("L'ora non corrisponde!!",result.get(Calendar.HOUR),5);
        Assert.assertEquals("I minuti non corrispondono!!",result.get(Calendar.MINUTE),43);

        Calendar result2 = DateTimeHelper.getTimeFromHourAndMinute(time,1);

        Assert.assertEquals("L'ora non corrisponde!!",result2.get(Calendar.HOUR),5);
        Assert.assertEquals("I minuti non corrispondono!!",result2.get(Calendar.MINUTE),43);

        Assert.assertEquals("Non c'è un giorno di differenza fra i due risultati!",1,result2.get(Calendar.DAY_OF_YEAR)-result.get(Calendar.DAY_OF_YEAR));
        Assert.assertEquals("Non c'è un giorno di differenza fra i due risultati!",1,result2.get(Calendar.DAY_OF_YEAR)-now.get(Calendar.DAY_OF_YEAR));

    }



    @Test
    public void testIsPlayTime() {
        Calendar start = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","15"},0);
        Calendar end = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","15"},0);

        Calendar currentTime1 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","10"},0);
        Calendar currentTime2 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","15"},0);
        Calendar currentTime3 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","20"},0);
        Calendar currentTime4 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","50"},0);

        Calendar currentTime5 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","10"},0);
        Calendar currentTime6 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","15"},0);
        Calendar currentTime7 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","20"},0);
        Calendar currentTime8 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"07","50"},1);

        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime1,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime2,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime3,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime4,start,end,10000,true));

        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime5,start,end,10000,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime6,start,end,10000,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime7,start,end,10000,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime8,start,end,10000,true));
        //playerChecker getToleranceMillis
        long playerCheckerSleepMillis = DateTimeHelper.getToleranceMillis();

        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime1,start,end,playerCheckerSleepMillis,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime2,start,end,playerCheckerSleepMillis,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime3,start,end,playerCheckerSleepMillis,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime4,start,end,playerCheckerSleepMillis,true));

        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime5,start,end,playerCheckerSleepMillis,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime6,start,end,playerCheckerSleepMillis,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime7,start,end,playerCheckerSleepMillis,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime8,start,end,playerCheckerSleepMillis,true));
    }

    @Test
    public void testIsPlayTimeAcrossMidnight() {
        Calendar start = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"20","00"},0);
        Calendar end = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","00"},1);

        Assert.assertTrue("End non è dopo Start!!",start.before(end));

        Calendar currentTime1 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"19","50"},0);
        Calendar currentTimeST = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"20","00"},0);

        Calendar currentTime2 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"20","15"},0);
        Calendar currentTime3 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"00","00"},0);
        Calendar currentTime4 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"09","50"},0);

        Calendar currentTime5 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"00","00"},1);
        Calendar currentTime6 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"09","50"},1);

        Calendar currentTimeEND0 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","00"},0);
        Calendar currentTimeEND1 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"10","00"},0);


        Calendar currentTime7 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"12","00"},0);
        Calendar currentTime8 = DateTimeHelper.getTimeFromHourAndMinute(new String[]{"12","00"},1);

        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime1,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTimeST,start,end,10000,true));

        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime2,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime3,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime4,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime5,start,end,10000,true));
        Assert.assertTrue(DateTimeHelper.isPlayTime(currentTime6,start,end,10000,true));

        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTimeEND0,start,end,10000,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTimeEND1,start,end,10000,true));


        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime7,start,end,10000,true));
        Assert.assertFalse(DateTimeHelper.isPlayTime(currentTime8,start,end,10000,true));

    }


}
