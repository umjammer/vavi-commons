/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.ByteOrder;

import static java.lang.System.getLogger;


/**
 * A stream to write bit by bit.
 * <p>
 * bits are 1 ~ 8, a value may span over byte boundaries.
 * big endian writes from MSB of each byte, little endian writes from LSB of each byte.
 * {@link #flush()} writes stacked bits that are fewer than 8 as a byte padded with 0.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 *          0.01 260915 nsano odd bits support <br>
 */
public class BitOutputStream extends FilterOutputStream {

    private static final Logger logger = getLogger(BitOutputStream.class.getName());

    /** bits number */
    private final int bits;
    /** bit order */
    private final ByteOrder bitOrder;

    /**
     * Creates a stream to write bit by bit.
     * 4Bit, big endian
     */
    public BitOutputStream(OutputStream out) {
        this(out, 4, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Creates a stream to write bit by bit.
     * big endian
     */
    public BitOutputStream(OutputStream out, int bits) {
        this(out, bits, ByteOrder.BIG_ENDIAN);
    }

    /** lower {@link #bits} bits are on */
    private final int mask;

    /**
     * Creates a stream to write bit by bit.
     *
     * @param bits 1 ~ 8
     */
    public BitOutputStream(OutputStream out, int bits, ByteOrder bitOrder) {
        super(out);
        if (bits < 1 || bits > 8) {
            throw new IllegalArgumentException("bits must be 1 ~ 8: " + bits);
        }
        this.bits = bits;
        this.bitOrder = bitOrder;
        this.mask = (1 << bits) - 1;
    }

    /** stacked bits */
    private int stackedBits = 0;
    /** bit buffer, valid bits are lower {@link #stackedBits} bits */
    private int current = 0;

    /**
     * Writes bits specified by {@link #bits}.
     */
    @Override
    public void write(int b) throws IOException {
        b &= mask;

        if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
            current |= b << stackedBits;
        } else {
            current = (current << bits) | b;
        }
        stackedBits += bits;

        while (stackedBits >= 8) {
            stackedBits -= 8;
            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
                out.write(current & 0xff);
                current >>>= 8;
            } else {
                out.write((current >> stackedBits) & 0xff);
                current &= (1 << stackedBits) - 1;
            }
        }
    }

    @Override
    public void flush() throws IOException {
        if (stackedBits != 0) {
logger.log(Level.DEBUG, "stacked bits: " + stackedBits + " flushed.");
            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
                out.write(current & 0xff);
            } else {
                out.write((current << (8 - stackedBits)) & 0xff);
            }
            stackedBits = 0;
            current = 0;
        }
        super.flush();
    }
}
