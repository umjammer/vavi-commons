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
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 */
public class BitOutputStream extends FilterOutputStream {

    private static final Logger logger = getLogger(BitOutputStream.class.getName());

    /** bits number */
    private int bits = 4;
    /** bit order */
    private ByteOrder bitOrder = ByteOrder.BIG_ENDIAN;

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

    /** LSB is on */
    private int mask;

    /**
     * Creates a stream to write bit by bit.
     */
    public BitOutputStream(OutputStream out, int bits, ByteOrder bitOrder) {
        super(out);
if (bits != 4 && bits != 2) {
 throw new IllegalArgumentException("currently, only supported 2, 4 bit reading");
}
        this.bits = bits;
        this.bitOrder = bitOrder;

        for (int i = 0; i < bits; i++) {
            mask |= (0x01 << i);
        }
//logger.log(Level.TRACE, bits + ", " + StringUtil.toBits(mask, 8));
//logger.log(Level.TRACE, bits + ", " + StringUtil.toBits(mask << 4, 8));
    }

    /** stacked bits */
    private int stackedBits = 0;
    /** stuck in big endian */
    private int current = 0;

    /**
     * convert 8bits to little endian.
     * <pre>
     * 2Bit
     *   1    2    3    4         4    3    2    1
     * | 01 | 11 | 10 | 01 | -> | 01 | 10 | 11 | 01 |
     * </pre>
     * @param c 8bit
     */
    private int convertEndian(int c) {
        int max = 8 / bits;
        int r = 0;
        for (int i = 0; i < max; i++) {
            int s1 = (max - 1 - i) * bits;
            int m = mask << s1;
            int v = (c & m) >> s1;
            int s2 = i * bits;
            r |= v << s2;
        }
        return r;
    }

    /**
     * Writes bits specified by {@link #bits}.
     */
    @Override
    public void write(int b) throws IOException {

        b &= mask;
//logger.log(Level.TRACE, StringUtil.toHex4(b) + ": " + StringUtil.toBits(b, 8));
        current |= b << (8 - stackedBits - bits);

        stackedBits += bits;

        if (stackedBits == 8) {
            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
//logger.log(Level.TRACE, "B: " + StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
                current = convertEndian(current);
//logger.log(Level.TRACE, "A: " + StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
            }
//logger.log(Level.TRACE, StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
            out.write(current);
            stackedBits= 0;
            current = 0;
        }
    }

    @Override
    public void flush() throws IOException {
//logger.log(Level.TRACE, "stacked bits: " + stackedBits);
        super.flush();
        if (stackedBits != 0) {
logger.log(Level.DEBUG, "stacked bits: " + stackedBits + " flushed.");
            out.write(current);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
