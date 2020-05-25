/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package vavix.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Various stream-related utility methods.
 *
 * @author Copyright (c) 2002 Merlin Hughes <merlin@merlin.org>
 */
public class Streams {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private Streams() {
    }

    /** */
    public static void io(InputStream in, OutputStream out) throws IOException {
        io(in, out, DEFAULT_BUFFER_SIZE);
    }

    /** */
    public static void io(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0)
            out.write(buffer, 0, amount);
    }

    /** */
    public static OutputStream synchronizedOutputStream(OutputStream out) {
        return new SynchronizedOutputStream(out);
    }

    /** */
    public static OutputStream synchronizedOutputStream(OutputStream out, Object lock) {
        return new SynchronizedOutputStream(out, lock);
    }

    /** */
    static class SynchronizedOutputStream extends OutputStream {
        private OutputStream out;

        private Object lock;

        SynchronizedOutputStream(OutputStream out) {
            this(out, out);
        }

        SynchronizedOutputStream(OutputStream out, Object lock) {
            this.out = out;
            this.lock = lock;
        }

        public void write(int datum) throws IOException {
            synchronized (lock) {
                out.write(datum);
            }
        }

        public void write(byte[] data) throws IOException {
            synchronized (lock) {
                out.write(data);
            }
        }

        public void write(byte[] data, int offset, int length) throws IOException {
            synchronized (lock) {
                out.write(data, offset, length);
            }
        }

        public void flush() throws IOException {
            synchronized (lock) {
                out.flush();
            }
        }

        public void close() throws IOException {
            synchronized (lock) {
                out.close();
            }
        }
    }
}
