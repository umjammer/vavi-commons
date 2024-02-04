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

import vavi.io.OutputEngine;


/**
 * An output engine that copies data from an InputStream through a
 * FilterOutputStream to the target OutputStream.
 *
 * @author Copyright (c) 2002 Merlin Hughes <merlin@merlin.org>
 */
public class IOStreamOutputEngine implements OutputEngine {
    /** */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** */
    private InputStream in;

    /** */
    private OutputStreamFactory factory;

    /** */
    private byte[] buffer;

    /** */
    private OutputStream out;

    /** */
    public IOStreamOutputEngine(InputStream in, OutputStreamFactory factory) {
        this(in, factory, DEFAULT_BUFFER_SIZE);
    }

    /** */
    public IOStreamOutputEngine(InputStream in, OutputStreamFactory factory, int bufferSize) {
        this.in = in;
        this.factory = factory;
        buffer = new byte[bufferSize];
    }

    /* */
    public void initialize(OutputStream out) throws IOException {
        if (this.out != null) {
            throw new IOException("Already initialized");
        } else {
            this.out = factory.getOutputStream(out);
        }
    }

    /* */
    public void execute() throws IOException {
        if (out == null) {
            throw new IOException("Not yet initialized");
        } else {
            int amount = in.read(buffer);
            if (amount < 0) {
                out.close();
            } else {
                out.write(buffer, 0, amount);
            }
        }
    }

    /* */
    public void finish() throws IOException {
        in.close();
    }

    /** */
    public interface OutputStreamFactory {
        OutputStream getOutputStream(OutputStream out) throws IOException;
    }
}

/* */
