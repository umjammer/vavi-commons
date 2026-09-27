/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol.data;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;


/**
 * DataURLStreamHandler.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-24 nsano initial version <br>
 */
class DataURLStreamHandler extends URLStreamHandler {

    /**
     * A URLConnection for use with URLs returned by MemoryClassLoader.getResource.
     */
    private static class MemoryURLConnection extends URLConnection {
        private final byte[] bytes;
        private InputStream in;
        private final String mimeType;

        MemoryURLConnection(URL u, byte[] bytes, String mimeType) {
            super(u);
            this.bytes = bytes;
            this.mimeType = mimeType;
        }

        @Override
        public void connect() throws IOException {
            if (!connected) {
                if (bytes == null) {
                    throw new FileNotFoundException(getURL().getPath());
                }
                in = new ByteArrayInputStream(bytes);
                connected = true;
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            connect();
            return in;
        }

        @Override
        public long getContentLengthLong() {
            return bytes.length;
        }

        @Override
        public String getContentType() {
            return mimeType;
        }
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        DataUri dataUri = DataUri.parse(url.toString(), StandardCharsets.UTF_8);
//System.err.println("url: " + url.toString().substring(0, url.toString().indexOf(',')) + ", mime: " + dataUri.getMime() + ", data: " + dataUri.getData().length);
        return new MemoryURLConnection(url, dataUri.getData(), dataUri.getMime());
    }
}
