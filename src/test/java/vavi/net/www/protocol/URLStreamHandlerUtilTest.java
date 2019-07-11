/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * URLStreamHandlerUtilTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/06 umjammer initial version <br>
 */
@Disabled // TODO
public class URLStreamHandlerUtilTest {

    @Test
    public void test() throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "");

        @SuppressWarnings("unused")
        URL url = null;
        try {
            url = new URL("classpath:test");
            fail(); // TODO
        } catch (MalformedURLException e) {
        }

        System.setProperty("java.protocol.handler.pkgs", "");
        URLStreamHandlerUtil.loadService();
        assertEquals("vavi.net.www.protocol", System.getProperty("java.protocol.handler.pkgs"));

        url = new URL("classpath:test");

        System.setProperty("java.protocol.handler.pkgs", "unknown.protocol");
        URLStreamHandlerUtil.loadService();
        assertEquals("unknown.protocol|vavi.net.www.protocol", System.getProperty("java.protocol.handler.pkgs"));

        url = new URL("classpath:test2");

        System.setProperty("java.protocol.handler.pkgs", "");

        url = new URL("classpath:test3"); // should be success, cause already the handler loaded?
    }
}

/* */
