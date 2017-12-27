/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * UtilInputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050817 nsano initial version <br>
 */
public class UtilInputStream extends FilterInputStream {

    /** */
    private String encoding = null;

    /** */
    public UtilInputStream(InputStream in) {
        super(in);
    }

    /** */
    public UtilInputStream(InputStream in, String encoding) {
        super(in);
        this.encoding = encoding;
    }

    /** caution: buffer max size is 8192 */
    public String readLine() throws IOException {
        byte[] buffer = new byte[8192];
        int length = readLine(buffer, 0, buffer.length);
        if (length == -1) {
            return null;
        }
        if (encoding == null) {
            return new String(buffer, 0, length);
        } else {
            return new String(buffer, 0, length, encoding);
        }
    }

    /** */
    /*public*/ int readLine(byte[] b, int off, int len) throws IOException {
        int current = off;
        while (current - off < len) {
            int r = read();
            if (r == -1) {
                if (current == off) {
                    return -1;
                }
                break;
            }
            if (r == '\r') {
                continue;
            }
            if (r == '\n') {
                break;
            }
            b[current] = (byte) r;
            ++current;
        }
        return current - off;
    }

    /** */
    public String readAsciiz() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int b = read();
            if (b == 0x00) {
                break;
            } else if (b == -1) {
                throw new EOFException("before zero terminated");
            }
            baos.write(b);
        }
        if (encoding == null) {
            return new String(baos.toByteArray());
        } else {
            return new String(baos.toByteArray(), encoding);
        }
    }
}

/* */
