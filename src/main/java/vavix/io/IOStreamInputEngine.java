/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import vavi.io.InputEngine;


/**
 * An input engine that copies data from an OutputStream through a
 * FilterInputStream to the target InputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 */
public class IOStreamInputEngine implements InputEngine {

    /** */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** the stream to actually write out */
    private final OutputStream out;

    /** */
    private final InputStreamFactory factory;

    /**
     * when true, {@link InputStreamFactory#getInputStream(InputStream)} is deferred
     * until the first {@link #execute()}, for streams like GZIPInputStream that
     * read a header on construction, when no data is available yet at {@link #initialize(InputStream)}
     */
    private final boolean lazy;

    /** */
    private final byte[] buffer;

    /** the source stream given at {@link #initialize(InputStream)} */
    private InputStream source;

    /** @see InputStreamFactory#getInputStream(InputStream) */
    private InputStream in;

    /**
     * @param out the stream to actually write out
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory) {
        this(out, factory, DEFAULT_BUFFER_SIZE, false);
    }

    /**
     * @param out the stream to actually write out
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory, int bufferSize) {
        this(out, factory, bufferSize, false);
    }

    /**
     * @param out the stream to actually write out
     * @param lazy see {@link #lazy}
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory, boolean lazy) {
        this(out, factory, DEFAULT_BUFFER_SIZE, lazy);
    }

    /**
     * @param out the stream to actually write out
     * @param lazy see {@link #lazy}
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory, int bufferSize, boolean lazy) {
        this.out = out;
        this.factory = factory;
        this.lazy = lazy;
        buffer = new byte[bufferSize];
    }

    /**
     * @param in InputEngineOutputStream.InputStreamImpl
     */
    @Override
    public void initialize(InputStream in) throws IOException {
        if (this.source != null) {
            throw new IOException("Already initialized");
        } else {
            this.source = in;
            if (!lazy) {
                this.in = factory.getInputStream(in);
            }
        }
    }

    @Override
    public void execute() throws IOException {
        if (source == null) {
            throw new IOException("Not yet initialized");
        } else {
            if (in == null) {
                in = factory.getInputStream(source);
            }
            int amount = in.read(buffer, 0, buffer.length);
//logger.log(Level.TRACE, "amount: " + amount + ", in: " + in + "\n" + StringUtil.getDump(buffer, 0, amount));
            if (amount < 0) {
                in.close();
            } else {
                out.write(buffer, 0, amount);
            }
        }
    }

    @Override
    public void finish() throws IOException {
        out.flush();
        out.close();
    }

    /** */
    public interface InputStreamFactory {
        InputStream getInputStream(InputStream in) throws IOException;
    }
}
