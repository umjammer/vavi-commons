/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.content.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.net.URLConnection;

import vavi.util.win32.WindowsProperties;


/**
 * ini.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/13 umjammer initial version <br>
 */
public class ini extends ContentHandler {

    /* @see java.net.ContentHandler#getContent(java.net.URLConnection) */
    @Override
    public Object getContent(URLConnection urlc) throws IOException {
        InputStream is = urlc.getInputStream();
        WindowsProperties properties = new WindowsProperties();
        properties.load(is);
        return properties;
    }
}
