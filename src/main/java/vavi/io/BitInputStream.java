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
 *
 * TODO odd bits
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 *          0.01 030714 nsano fix available() <br>
 *          0.02 030715 nsano read() BitOrder support <br>
 *          0.03 030716 nsano 2bit support <br>
 */
public class BitInputStream extends FilterInputStream {

    /** bits number */
    private int bits = 4;

    /** bit order */
    private ByteOrder bitOrder = ByteOrder.BIG_ENDIAN;

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

    /** MSB is on */
    private int mask;

    /**
     * Create a stream to read bit by bit.
     */
    public BitInputStream(InputStream in, int bits, ByteOrder bitOrder) {
        super(in);
        if (bits != 4 && bits != 2) {
            throw new IllegalArgumentException("currently, only supported 2, 4 bit reading");
        }
        this.bits = bits;
        this.bitOrder = bitOrder;

        for (int i = 0; i < bits; i++) {
            mask |= (0x80 >> i);
        }
//logger.log(Level.TRACE, bits + ", " + StringUtil.toBits(mask, 8));
//logger.log(Level.TRACE, bits + ", " + StringUtil.toBits(mask >> 4, 8));
    }

    /** remaining bits for reading */
    private int restBits = 0;

    /** big endian */
    private int current;

    /** */
    @Override
    public int available() throws IOException {
        return (in.available() * (8 / bits)) + (restBits / bits);
    }

    /**
     * Reads bits specified by {@link #bits}.
     */
    @Override
    public int read() throws IOException {

        if (restBits == 0) {
            current = in.read();
            if (current == -1) {
                return -1;
            }

            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
// Debug.println("B: " + StringUtil.toHex2(current) + ": " +
// StringUtil.toBits(current, 8));
                current = convertEndian(current);
// Debug.println("A: " + StringUtil.toHex2(current) + ": " +
// StringUtil.toBits(current, 8));
            }
            restBits = 8;
        }

        int c = (current & (mask >> (8 - restBits))) >> (restBits - bits);
        restBits -= bits;

// Debug.println("R: " + StringUtil.toHex2(c) + ": " +
// StringUtil.toBits(c, 4));
        return c;
    }

    /**
     * <pre>
     *  2Bit
     *    1    2    3    4       4    3    2    1
     *  | 01 | 11 | 10 | 01 | -&gt; | 01 | 10 | 11 | 01 |
     * </pre>
     *
     * @param c 8bit
     */
    private int convertEndian(int c) {
        int max = 8 / bits;
        int r = 0;
        for (int i = 0; i < max; i++) {
            int s1 = i * bits;
            int m = mask >> s1;
            int s2 = (max - 1 - i) * bits;
            int v = (c & m) >> s2;
            r |= v << s1;
        }
        return r;
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
                if (b != null) {
                    b[off + i] = (byte) c;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return i;
    }
}
