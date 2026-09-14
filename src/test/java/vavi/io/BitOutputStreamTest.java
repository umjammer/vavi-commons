/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * BitOutputStreamTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040919 nsano initial version <br>
 */
public class BitOutputStreamTest {

    /** */
    @Test
    public void test_4Bit_BE_1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos)) { // 4bit BigEndian
            bos.write(0xf);
            bos.write(0x5);
            bos.flush();
            assertEquals((byte) 0xf5, baos.toByteArray()[0]);
        }
    }

    /** */
    @Test
    public void test_4Bit_BE_2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos)) { // 4bit BigEndian
            bos.write(0x5);
            bos.write(0xf);
            bos.flush();
            assertEquals((byte) 0x5f, baos.toByteArray()[0]);
        }
    }

    /**
     * <pre>
     * 01 | 11 | 10 | 01 | -> 01 | 11 | 10 | 01 |
     * </pre>
     */
    @Test
    public void test_2Bit_BE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 2)) { // 2bit BigEndian
            bos.write(0x01);
            bos.write(0x03);
            bos.write(0x02);
            bos.write(0x01);
            bos.flush();
            assertEquals((byte) 0x79, baos.toByteArray()[0]);
        }
    }

    /**
     * <pre>
     * 01 | 11 | 10 | 01 | -> | 01 | 10 | 11 | 01
     * </pre>
     */
    @Test
    public void test_2Bit_LE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 2, ByteOrder.LITTLE_ENDIAN)) { // 2bit LittleEndian
            bos.write(0x01);
            bos.write(0x03);
            bos.write(0x02);
            bos.write(0x01);
            bos.flush();
            assertEquals((byte) 0x6d, baos.toByteArray()[0]);
        }
    }

    /** */
    @Test
    public void test_4Bit_LE_1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 4, ByteOrder.LITTLE_ENDIAN)) { // 4bit LittleEndian
            bos.write(0xf);
            bos.write(0x5);
            bos.flush();
            assertEquals((byte) 0x5f, baos.toByteArray()[0]);
        }
    }

    /** */
    @Test
    public void test_4Bit_LE_2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 4, ByteOrder.LITTLE_ENDIAN)) { // 4bit LittleEndian
            bos.write(0x5);
            bos.write(0xf);
            bos.flush();
            assertEquals((byte) 0xf5, baos.toByteArray()[0]);
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_3Bit_BE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 3)) { // 3bit BigEndian
            bos.write(0b101);
            bos.write(0b101);
            bos.write(0b100);
            bos.write(0b110);
            bos.write(0b110);
            bos.flush(); // last 1 bit is padded with 0
            assertArrayEquals(new byte[] { (byte) 0xb6, (byte) 0x6c }, baos.toByteArray());
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_5Bit_LE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 5, ByteOrder.LITTLE_ENDIAN)) { // 5bit LittleEndian
            bos.write(0b10110);
            bos.write(0b01101);
            bos.write(0b11011);
            bos.flush(); // MSB is padded with 0
            assertArrayEquals(new byte[] { (byte) 0xb6, (byte) 0x6d }, baos.toByteArray());
        }
    }

    /** round trip with {@link BitInputStream} */
    @Test
    public void test_roundTrip() throws Exception {
        for (ByteOrder order : new ByteOrder[] { ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN }) {
            for (int bits = 1; bits <= 8; bits++) {
                int count = 24 / bits; // trailing padding bits are discarded by the reader
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (BitOutputStream bos = new BitOutputStream(baos, bits, order)) {
                    for (int i = 0; i < count; i++) {
                        bos.write(i * 37);
                    }
                }
                BitInputStream bis = new BitInputStream(new ByteArrayInputStream(baos.toByteArray()), bits, order);
                for (int i = 0; i < count; i++) {
                    assertEquals((i * 37) & ((1 << bits) - 1), bis.read(), order + ", " + bits + ", " + i);
                }
            }
        }
    }

    /** flush twice doesn't write twice */
    @Test
    public void test_flushTwice() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bos = new BitOutputStream(baos, 3)) {
            bos.write(0b101);
            bos.flush();
            bos.flush();
        }
        assertArrayEquals(new byte[] { (byte) 0xa0 }, baos.toByteArray());
    }

    //-------------------------------------------------------------------------

    /** */
    public static void main(String[] args) throws Exception {
    }
}
