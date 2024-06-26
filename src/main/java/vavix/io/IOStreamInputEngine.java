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

    /** */
    private final byte[] buffer;

    /** @see InputStreamFactory#getInputStream(InputStream) */
    private InputStream in;

    /**
     * @param out the stream to actually write out
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory) {
        this(out, factory, DEFAULT_BUFFER_SIZE);
    }

    /**
     * @param out the stream to actually write out
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory, int bufferSize) {
        this.out = out;
        this.factory = factory;
        buffer = new byte[bufferSize];
    }

    /**
     * @param in InputEngineOutputStream.InputStreamImpl
     */
    @Override
    public void initialize(InputStream in) throws IOException {
        if (this.in != null) {
            throw new IOException("Already initialized");
        } else {
            this.in = factory.getInputStream(in);
        }
    }

    @Override
    public void execute() throws IOException {
        if (in == null) {
            throw new IOException("Not yet initialized");
        } else {
            int amount = in.read(buffer, 0, buffer.length);
//Debug.println("amount: " + amount + ", in: " + in + "\n" + StringUtil.getDump(buffer, 0, amount));
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
