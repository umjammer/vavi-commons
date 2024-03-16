/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.loggingtest; // for not excluding this class (corner cutting)

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vavi.util.logging.VaviFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * VaviFormatterTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/23 umjammer initial version <br>
 */
public class VaviFormatterTest {

    static String backup;

    @BeforeAll
    static void before() {
        backup = System.getProperty("vavi.util.logging.VaviFormatter.classMethod");
    }

    @AfterAll
    static void after() {
        if (backup != null) {
            System.setProperty("vavi.util.logging.VaviFormatter.classMethod", backup);
        }
    }

    @Test
    public void test() {
        System.setProperty("vavi.util.logging.VaviFormatter.classMethod", "vavi\\.util\\.logging\\.VaviFormatter#format");
        LogRecord record = new LogRecord(Level.INFO, "test");
        VaviFormatter formatter = new VaviFormatter();
        assertTrue(formatter.format(record).contains("at vavi.util.loggingtest.VaviFormatterTest.test(VaviFormatterTest.java:"), formatter.format(record));
    }

    @Test
    public void test2() {
        Pattern p = Pattern.compile("sun\\.util\\.logging\\.PlatformLogger.JavaLoggerProxy#doLog");
        Matcher m = p.matcher("sun.util.logging.PlatformLogger$JavaLoggerProxy#doLog");
        assertTrue(m.matches());
    }
}
