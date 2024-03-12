/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import vavi.util.Debug;


/**
 * Bit 単位で書き込むストリームです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 */
public class BitOutputStream extends FilterOutputStream {

    /** ビット数 */
    private int bits = 4;
    /** ビットオーダ */
    private ByteOrder bitOrder = ByteOrder.BIG_ENDIAN;

    /**
     * Bit 単位で読み込むストリームを作成します．
     * 4Bit, ビッグエンディアン．
     */
    public BitOutputStream(OutputStream out) {
        this(out, 4, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Bit 単位で読み込むストリームを作成します．
     * ビッグエンディアン．
     */
    public BitOutputStream(OutputStream out, int bits) {
        this(out, bits, ByteOrder.BIG_ENDIAN);
    }

    /** LSB が立っています。 */
    private int mask;

    /**
     * Bit 単位で書き込むストリームを作成します．
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
//Debug.println(bits + ", " + StringUtil.toBits(mask, 8));
//Debug.println(bits + ", " + StringUtil.toBits(mask << 4, 8));
    }

    /** 積み上げられたビット数 */
    private int stackedBits = 0;
    /** ビッグエンディアンで積み上げる */
    private int current = 0;

    /**
     * 8 ビットをリトルエンディアンに変換します。
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
     * 指定した bit 書き込みます．
     */
    public void write(int b) throws IOException {

        b &= mask;
//Debug.println(StringUtil.toHex4(b) + ": " + StringUtil.toBits(b, 8));
        current |= b << (8 - stackedBits - bits);

        stackedBits += bits;

        if (stackedBits == 8) {
            if (ByteOrder.LITTLE_ENDIAN.equals(bitOrder)) {
//Debug.println("B: " + StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
                current = convertEndian(current);
//Debug.println("A: " + StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
            }
//Debug.println(StringUtil.toHex4(current) + ": " + StringUtil.toBits(current, 8));
            out.write(current);
            stackedBits= 0;
            current = 0;
        }
    }

    /** */
    public void flush() throws IOException {
//Debug.println("stacked bits: " + stackedBits);
        super.flush();
        if (stackedBits != 0) {
Debug.println("stacked bits: " + stackedBits + " flushed.");
            out.write(current);
        }
    }

    /** */
    public void close() throws IOException {
        super.close();
    }
}
