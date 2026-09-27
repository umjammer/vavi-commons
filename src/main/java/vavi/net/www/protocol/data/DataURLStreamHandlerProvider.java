/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol.data;

import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;


/**
 * DataURLStreamHandlerProvider.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-24 nsano initial version <br>
 */
public class DataURLStreamHandlerProvider extends URLStreamHandlerProvider {

    @Override
    public URLStreamHandler createURLStreamHandler(String s) {
        if (s.equals("data")) {
//System.err.println("scheme: " + s);
            return new DataURLStreamHandler();
        } else {
            return null;
        }
    }
}
