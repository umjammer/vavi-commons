/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * URLStreamHandlerUtilTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/06 umjammer initial version <br>
 */
@Disabled
public class URLStreamHandlerUtilTest {

    static String backup;

    @BeforeAll
    static void before() {
        backup = System.getProperty("java.protocol.handler.pkgs");
    }

    @AfterAll
    static void after() {
        if (backup != null) {
            System.setProperty("java.protocol.handler.pkgs", backup);
        }
    }

    @Test
    @Disabled("because implemented java9 URLStreamHandlerProvider")
    public void test() throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "");

        assertThrows(MalformedURLException.class, () -> { // on java9 no exception because of description in @Disabled.
            new URL("classpath:test");
        });

        System.setProperty("java.protocol.handler.pkgs", "");
        URLStreamHandlerUtil.loadService();
        assertEquals("vavi.net.www.protocol", System.getProperty("java.protocol.handler.pkgs"));

        URL url = new URL("classpath:test");

        System.setProperty("java.protocol.handler.pkgs", "unknown.protocol");
        URLStreamHandlerUtil.loadService();
        assertEquals("unknown.protocol|vavi.net.www.protocol", System.getProperty("java.protocol.handler.pkgs"));

        url = new URL("classpath:test2");

        System.setProperty("java.protocol.handler.pkgs", "");

        url = new URL("classpath:test3"); // should be success, cause already the handler loaded?
    }

    @Test
    public void test2() throws Exception {
        new URL("classpath:test");
    }
}
