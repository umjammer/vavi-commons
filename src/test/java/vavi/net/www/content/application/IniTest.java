/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.content.application;

import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import vavi.net.www.content.ContentHandlerUtil;
import vavi.net.www.protocol.URLStreamHandlerUtil;
import vavi.util.win32.WindowsProperties;

import static org.junit.Assert.assertTrue;


/**
 * IniTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/13 umjammer initial version <br>
 */
public class IniTest {

    @Test
    public void test() throws Exception {
        URLStreamHandlerUtil.loadService();
        ContentHandlerUtil.loadService();

        URL url = new URL("classpath:test.ini");
        Object content = url.getContent();
System.err.println(content);
        assertTrue(WindowsProperties.class.isInstance(content));
    }

    @Test
    public void test2() throws Exception {
        URLStreamHandlerUtil.loadService();
        URLConnection.setContentHandlerFactory(mimetype -> {
            if (mimetype.equalsIgnoreCase("application/ini"))
                return new vavi.net.www.content.application.ini();
            else
                return null;
        });

        URL url = new URL("classpath:test.ini");
        Object content = url.getContent();
System.err.println(content);
        assertTrue(WindowsProperties.class.isInstance(content));
    }
}

/* */
