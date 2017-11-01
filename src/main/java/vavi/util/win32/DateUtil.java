/*
 * Copyright (c) 2003 by Naohide Sano, All right reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.util.Calendar;


/**
 * Win32 の日付関連の構造体を扱うためのクラスです．
 * 何でこんなにいっぱいあるの? さすが M$、統一感無いねぇ。
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030211 nsano initial version <br>
 *          0.01 030211 nsano add dateToLong <br>
 *          0.02 030227 nsano add dosDateTimeToLong <br>
 *          0.03 030311 nsano fix dosDateTimeToLong month <br>
 */
public final class DateUtil {

    /** */
    private static final double TIME_FIXUP_CONSTANT =
        369.0 * 365.25 * 24 * 60 * 60 - (3.0 * 24 * 60 * 60 + 6.0 * 60 * 60);

    /** */
    private static final long H_MASK = 0xffffffff00000000L;
    /** */
    private static final long L_MASK = 0xffffffffL;

    /** */
    private DateUtil() {}

    /**
     * FILETIME (long) から java long (msec since 1970/1/1) を取得します．
     */
    public static final long filetimeToLong(long filetime) {

        if ((filetime & H_MASK) == 0) {
            return 0;
        }

        double d = ((filetime & H_MASK) >>> 32) * 4.0D * (1 << 30);
        d += (filetime & L_MASK & 0xfff00000);
        d *= 1.0e-4;

        /* now adjust by 369 years to make the secs since 1970 */
        d -= TIME_FIXUP_CONSTANT * 1000;

        if (0 >= d) {
            return 0;
        }

        return (long) (d + (filetime & L_MASK & 0xfffff));
    }

    /**
     * DATE (double) から java long (msec since 1970/1/1) を取得します．
     * 
     * @param date OLE Automation date.
     */
    public static final long dateToLong(double date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1899, Calendar.DECEMBER, 30, 00, 00, 00);
        double past = 24 * 60 * 60 * 1000 * date;
        return calendar.getTime().getTime() + (long) past;
    }

    /**
     * DOS DATE (int) と DOS TIME (int) から
     * java long (msec since 1970/1/1) を取得します．
     * 
     * @param	date	上位から 7 bit が 1980 年からの年数，4 bit が月，
     *			残りの 5 bit が日を表す
     * @param	time	上位から 5 bit が時間，6 bit が分，
     *			残りの 5 bit が秒を 2 で割ったもの
     */
    public static final long dosDateTimeToLong(int date, int time) {

        Calendar calendar = Calendar.getInstance();
        int y = 1980 + ((date & 0xfe00) >> 9);
        int M = ((date & 0x1e0) >> 5) - 1;
        int d = date & 0x1f;
        int h = (time & 0xf800) >> 11;
        int m = (time & 0x7e0) >> 5;
        int s = (time & 0x1f) * 2;
//System.err.println(y + "/" + M + "/" + d + " " + h + ":" + m + ":" + s);
        calendar.set(y, M, d, h, m, s);
        return calendar.getTime().getTime();
    }
}

/* */
