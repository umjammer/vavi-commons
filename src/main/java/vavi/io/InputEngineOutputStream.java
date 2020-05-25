/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An output stream that writes data from an InputEngine.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 */
public class InputEngineOutputStream extends OutputStream {
    /** */
    private static final int DEFAULT_INITIAL_BUFFER_SIZE = 8192;

    /** */
    private InputEngine engine;

    /** */
    private byte[] buffer;

    /** */
    private int index, limit, capacity;

    /** */
    private boolean closed, eof;

    /** */
    public InputEngineOutputStream(InputEngine engine) throws IOException {
        this(engine, DEFAULT_INITIAL_BUFFER_SIZE);
    }

    /** */
    public InputEngineOutputStream(InputEngine engine, int initialBufferSize) throws IOException {
        this.engine = engine;
        capacity = initialBufferSize;
        buffer = new byte[capacity];
        engine.initialize(new InputStreamImpl());
    }

    /** */
    private byte[] one = new byte[1];

    /* */
    public void write(int b) throws IOException {
        one[0] = (byte) (b & 0xff);
        write(one, 0, 1);
    }

    /**
     * <li> limit が capacity に達したら {@link InputEngine#execute()}</li>
     *
     * <pre>
     * buffer
     * |              +limit  +capacity
     * |oooooooooooooo+-------|
     * reader
     * |   +index
     * |xxx+------------------|
     * </pre>
     */
    public void write(byte[] data, int offset, int length) throws IOException {
//Debug.println("offset: " + offset + ", length: " + length + "\n" + StringUtil.getDump(data, offset, length));
        if (data == null) {
            throw new NullPointerException();
        } else if ((offset < 0) || (offset + length > data.length) || (length < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (capacity - limit < length) {
                int filling = capacity - limit;
                // first time
                //
                //  buffer
                //  |              +limit  +capacity
                //  |oooooooooooooo+-------|
                //  write
                //  |                      +limit = capacity
                //  |--------------oooooooo|
                //                 <------> filling
                //
                System.arraycopy(data, offset, buffer, limit, filling); // full
                offset += filling;
                limit += filling; // = capacity
                engine.execute();

                // second time ...
                //
                //  buffer
                //  |                      +limit = capacity
                //  |--------------oooooooo|
                //  write
                //  |                      ******************************
                //  |                      <----------------------------> rest
                //  while rest > capacity
                //  |                      +limit = capacity
                //  |oooooooooooooooooooooo|
                //  |                      ********
                //  |                      <------> rest
                //
                int rest = length - filling;
                while (rest > capacity) {
                    limit = 0;
                    System.arraycopy(data, offset, buffer, limit, capacity); // full
                    offset += capacity;
                    limit = capacity;
                    engine.execute();
                    rest -= capacity;
                }

                // rest
                length = rest;
                // reset
                limit = 0;
            }
            System.arraycopy(data, offset, buffer, limit, length);
//Debug.println(StringUtil.getDump(buffer, limit, length));
            limit += length;
        }
    }

    /** 必ずすること */
    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        engine.execute();
    }

    /* */
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            engine.finish();
        }
    }

    /**
     * index: last pointer of buffer
     */
    private int readImpl(byte[] data, int offset, int length) {
        if (limit - index <= 0) {
            return -1;
        } else if (length > limit - index) {
            length = limit - index;
        }
//Debug.println("\n" + StringUtil.getDump(buffer, 0, index));
        System.arraycopy(buffer, index, data, offset, length);
        index += length;
        if (index == capacity) {
            index = 0;
            limit = 0;
        }
        return length;
    }

    /** */
    private class InputStreamImpl extends InputStream {
        /* */
        public int available() {
            return index;
        }

        /* */
        public int read() throws IOException {
            int r = read(one, 0, 1);
            if (r != 1) {
                return -1;
            } else {
                return one[0];
            }
        }

        /* */
        public int read(byte[] data, int offset, int length) throws IOException {
            if (data == null) {
                throw new NullPointerException("data");
            } else if ((offset < 0) || (offset > data.length) || (length < 0) ||
                     ((offset + length) > data.length) || ((offset + length) < 0)) {
                throw new IndexOutOfBoundsException("off: " + offset + ", len: " + length);
            } else if (eof) {
                throw new IOException("Stream closed");
            } else {
                int r = readImpl(data, offset, length);
                return r;
            }
        }

        /* */
        public void close() {
            eof = true;
        }
    }
}

/* */
