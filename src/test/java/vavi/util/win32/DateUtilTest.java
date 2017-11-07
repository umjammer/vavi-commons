/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.util.Calendar;

import junit.framework.TestCase;


/**
 * DateUtilTest.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040912 nsano initial version <br>
 */
public class DateUtilTest extends TestCase {

    /** milli sec 切捨て */
    public void testFiletimeToLong() {
        long actual = DateUtil.filetimeToLong(127056483573732704L);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2003, 7, 18, 11, 48, 46);
        assertEquals(calendar.getTimeInMillis() / 1000, actual / 1000);
    }

    /** milli sec 切捨て */
    public void testDateToLong() {
        long actual = DateUtil.dateToLong(37565.911458333336);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2002, 10, 5, 21, 52, 30);
        assertEquals(calendar.getTimeInMillis() / 1000, actual / 1000);
    }

    /** milli sec 切捨て */
    public void testDosDateTimeToLong() {
        long actual = DateUtil.dosDateTimeToLong(1, 1);
        assertEquals(312822002078L / 1000, actual / 1000);
    }
}

/* */
