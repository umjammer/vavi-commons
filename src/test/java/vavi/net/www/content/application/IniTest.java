/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.content.application;

import java.net.URL;
import java.net.URLConnection;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import vavi.net.www.content.ContentHandlerUtil;
import vavi.net.www.protocol.URLStreamHandlerUtil;
import vavi.util.win32.WindowsProperties;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * IniTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/13 umjammer initial version <br>
 */
@Disabled
public class IniTest {

    /** TODO doesn't work */
    @Test
    public void test() throws Exception {
        URLStreamHandlerUtil.loadService();
        ContentHandlerUtil.loadService(); // TODO <--

        URL url = new URL("classpath:test.ini");
        Object content = url.getContent();
System.err.println(content);
        assertInstanceOf(WindowsProperties.class, content);
    }

    /** TODO doesn't work */
    @Test
    public void test2() throws Exception {
        URLStreamHandlerUtil.loadService();
        URLConnection.setContentHandlerFactory(mimetype -> { // TODO <--
System.err.println(mimetype); // got 
            if (mimetype.equalsIgnoreCase("application/ini"))
                return new vavi.net.www.content.application.ini();
            else
                return null;
        });

        URL url = new URL("classpath:test.ini");
        Object content = url.getContent();
System.err.println(content);
        assertInstanceOf(WindowsProperties.class, content);
    }
}
