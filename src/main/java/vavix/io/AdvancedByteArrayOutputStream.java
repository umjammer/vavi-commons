/*
 * http://www.ibm.com/developerworks/jp/java/library/j-io2/
 */

package vavix.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An unsynchronized ByteArrayOutputStream alternative that efficiently provides
 * read-only access to the internal byte array with no unnecessary copying.
 *
 * @author Copyright (c) 2002 Merlin Hughes <merlin@merlin.org>
 */
public class AdvancedByteArrayOutputStream extends OutputStream {
    /* */
    private static final int DEFAULT_INITIAL_BUFFER_SIZE = 8192;

    /** internal buffer */
    private byte[] buffer;

    /** */
    private int index, capacity;

    /** is the stream closed? */
    private boolean closed;

    /** is the buffer shared? */
    private boolean shared;

    /* */
    public AdvancedByteArrayOutputStream() {
        this(DEFAULT_INITIAL_BUFFER_SIZE);
    }

    /* */
    public AdvancedByteArrayOutputStream(int initialBufferSize) {
        capacity = initialBufferSize;
        buffer = new byte[capacity];
    }

    /* */
    public void write(int datum) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (index >= capacity) {
                // expand the internal buffer
                capacity = capacity * 2 + 1;
                byte[] tmp = new byte[capacity];
                System.arraycopy(buffer, 0, tmp, 0, index);
                buffer = tmp;
                // the new buffer is not shared
                shared = false;
            }
            // store the byte
            buffer[index++] = (byte) datum;
        }
    }

    /* */
    public void write(byte[] data, int offset, int length) throws IOException {
        if (data == null) {
            throw new NullPointerException();
        } else if ((offset < 0) || (offset + length > data.length) || (length < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (index + length > capacity) {
                // expand the internal buffer
                capacity = capacity * 2 + length;
                byte[] tmp = new byte[capacity];
                System.arraycopy(buffer, 0, tmp, 0, index);
                buffer = tmp;
                // the new buffer is not shared
                shared = false;
            }
            // copy in the subarray
            System.arraycopy(data, offset, buffer, index, length);
            index += length;
        }
    }

    /* */
    public void close() {
        closed = true;
    }

    /* */
    public byte[] toByteArray() {
        // return a copy of the internal buffer
        byte[] result = new byte[index];
        System.arraycopy(buffer, 0, result, 0, index);
        return result;
    }

    /* */
    public void writeTo(OutputStream out) throws IOException {
        // write the internal buffer directly
        out.write(buffer, 0, index);
    }

    /* */
    public InputStream toInputStream() {
        // return a stream reading from the shared internal buffer
        shared = true;
        return new AdvancedByteArrayInputStream(buffer, 0, index);
    }

    /* */
    public void reset() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (shared) {
                // create a new buffer if it is shared
                buffer = new byte[capacity];
                shared = false;
            }
            // reset index
            index = 0;
        }
    }
}

/* */
