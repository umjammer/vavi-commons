/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;


/**
 * A stream to read bit by bit.
 * <p>
 * bits are 1 ~ 8, a value may span over byte boundaries.
 * big endian reads from MSB of each byte, little endian reads from LSB of each byte.
 * trailing bits that are fewer than {@link #bits} at the end of the stream are discarded.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 *          0.01 030714 nsano fix available() <br>
 *          0.02 030715 nsano read() BitOrder support <br>
 *          0.03 030716 nsano 2bit support <br>
 *          0.04 260915 nsano odd bits support <br>
 */
public class BitInputStream extends FilterInputStream {

    /** bits number */
    private final int bits;

    /** bit order */
    private final ByteOrder bitOrder;

    /**
     * Create a stream to read bit by bit. 4Bit, big endian．
     */
    public BitInputStream(InputStream in) {
        this(in, 4, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Create a stream to read bit by bit. big endian.
     */
    public BitInputStream(InputStream in, int bits) {
        this(in, bits, ByteOrder.BIG_ENDIAN);
    }

    /** lower {@link #bits} bits are on */
    private final int mask;

    /**
     * Create a stream to read bit by bit.
     *
     * @param bits 1 ~ 8
     */
    public BitInputStream(InputStream in, int bits, ByteOrder bitOrder) {
        super(in);
        if (bits < 1 || bits > 8) {
            throw new IllegalArgumentException("bits must be 1 ~ 8: " + bits);
        }
        this.bits = bits;
        this.bitOrder = bitOrder;
        this.mask = (1 << bits) - 1;
    }

    /** remaining bits for reading */
    private int restBits = 0;

    /** bit buffer, valid bits are lower {@link #restBits} bits */
    private int current;

    @Override
    public int available() throws IOException {
        return (in.available() * 8 + restBits) / bits;
    }

    /**
     * Reads bits specified by {@link #bits}.
     */
    @Override
    public int read() throws IOException {

        while (restBits < bits) {
            int c = in.read();
            if (c == -1) {
                return -1;
            }

            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
                current |= c << restBits;
            } else {
                current = (current << 8) | c;
            }
            restBits += 8;
        }

        int c;
        if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
            c = current & mask;
            current >>>= bits;
        } else {
            c = (current >> (restBits - bits)) & mask;
        }
        restBits -= bits;
        current &= (1 << restBits) - 1;

        return c;
    }

    /**
     * Without this, there are times when you won't be able to use this class's read function.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return i;
    }
}
