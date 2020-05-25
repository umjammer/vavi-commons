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
 * Bit 単位で読み込むストリームです．
 *
 * TODO 中途半端なビット
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030713 nsano initial version <br>
 *          0.01 030714 nsano fix available() <br>
 *          0.02 030715 nsano read() BitOrder 対応 <br>
 *          0.03 030716 nsano 2bit 対応 <br>
 */
public class BitInputStream extends FilterInputStream {

    /** ビット数 */
    private int bits = 4;

    /** ビットオーダ */
    private ByteOrder bitOrder = ByteOrder.BIG_ENDIAN;

    /**
     * Bit 単位で読み込むストリームを作成します． 4Bit, ビッグエンディアン．
     */
    public BitInputStream(InputStream in) {
        this(in, 4, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Bit 単位で読み込むストリームを作成します． ビッグエンディアン．
     */
    public BitInputStream(InputStream in, int bits) {
        this(in, bits, ByteOrder.BIG_ENDIAN);
    }

    /** MSB が立っています。 */
    private int mask;

    /**
     * Bit 単位で読み込むストリームを作成します．
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
// Debug.println(bits + ", " + StringUtil.toBits(mask, 8));
// Debug.println(bits + ", " + StringUtil.toBits(mask >> 4, 8));
    }

    /** 残っているビット数 */
    private int restBits = 0;

    /** ビッグエンディアン */
    private int current;

    /** */
    public int available() throws IOException {
        return (in.available() * (8 / bits)) + (restBits / bits);
    }

    /**
     * 指定した bit 読み込みます．
     */
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
     * こいつがないとこのクラスの read を使用しない時がある。
     */
    public int read(byte b[], int off, int len) throws IOException {
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

/* */
