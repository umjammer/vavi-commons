/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol;

import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

import vavi.net.www.protocol.classpath.Handler;


/**
 * ClasspathURLStreamHandlerProvider.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-24 nsano initial version <br>
 */
public class ClasspathURLStreamHandlerProvider extends URLStreamHandlerProvider {

    @Override
    public URLStreamHandler createURLStreamHandler(String s) {
        if (s.equals("classpath")) {
//System.err.println("scheme: " + s);
            return new Handler();
        } else {
            return null;
        }
    }
}
