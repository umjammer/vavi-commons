/*
 * Copyright (c) 2003 by Naohide Sano, All right reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * This is a class for handling Win32 date-related structures.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
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
     * Get java long (msec since 1970/1/1) from FILETIME (long).
     */
    public static long filetimeToLong(long filetime) {

        if ((filetime & H_MASK) == 0) {
            return 0;
        }

        double d = ((filetime & H_MASK) >>> 32) * 4.0D * (1 << 30);
        d += (filetime & L_MASK & 0xfff00000L);
        d *= 1.0e-4;

        /* now adjust by 369 years to make the secs since 1970 */
        d -= TIME_FIXUP_CONSTANT * 1000;

        if (0 >= d) {
            return 0;
        }

        return (long) (d + (filetime & L_MASK & 0xfffff));
    }

    /**
     * Get java long (msec since 1970/1/1) from DATE (double).
     *
     * @param date OLE Automation date.
     */
    public static long dateToLong(double date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(1899, Calendar.DECEMBER, 30, 0, 0, 0);
        double past = 24 * 60 * 60 * 1000 * date;
        return calendar.getTime().getTime() + (long) past;
    }

    /**
     * Get java long (msec since 1970/1/1) from DOS DATE (WORD) and DOS TIME (WORD).
     *
     * @param date The top 7 bits represent the year since 1980, 4 bits represent the month,
     *            and the remaining 5 bits represent the day.
     * @param time The top 5 bits are hours, 6 bits are minutes, and the remaining 5 bits are seconds divided by 2.
     */
    public static long dosDateTimeToLong(int date, int time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
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

    /**
     * @see "https://stackoverflow.com/questions/5398557/java-library-for-dealing-with-win32-filetime"
     */
    private static final Instant ZERO = Instant.parse("1601-01-01T00:00:00Z");

    /**
     * @see "https://stackoverflow.com/questions/5398557/java-library-for-dealing-with-win32-filetime"
     * @return filetime
     */
    public static long toFileTime(long epochMillis) {
        Duration duration = Duration.between(ZERO, Instant.ofEpochMilli(epochMillis));
        return duration.getSeconds() * 10_000_000 + duration.getNano() / 100;
    }

    /**
     * @see "https://stackoverflow.com/questions/5398557/java-library-for-dealing-with-win32-filetime"
     * @return epoch millis
     */
    public static long fromFileTime(long fileTime) {
        Duration duration = Duration.of(fileTime / 10, ChronoUnit.MICROS).plus(fileTime % 10 * 100, ChronoUnit.NANOS);
        return ZERO.plus(duration).toEpochMilli();
    }
}
