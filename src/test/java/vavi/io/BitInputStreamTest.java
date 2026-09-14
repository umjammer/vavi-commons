/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import vavi.util.Debug;
import vavi.util.StringUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * BitInputStreamTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040919 nsano initial version <br>
 */
public class BitInputStreamTest {

    /** */
    @Test
    public void test_4Bit_BE_1() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xf5, (byte) 0xfe });
        try (BitInputStream bis = new BitInputStream(bais)) { // 4bit BigEndian
            assertEquals((byte) 0xf, bis.read());
            assertEquals((byte) 0x5, bis.read());
            assertEquals((byte) 0xf, bis.read());
            assertEquals((byte) 0xe, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** */
    @Test
    public void test_4Bit_BE_2() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x5f, (byte) 0xef });
        try (BitInputStream bis = new BitInputStream(bais)) { // 4bit BigEndian
            assertEquals((byte) 0x5, bis.read());
            assertEquals((byte) 0xf, bis.read());
            assertEquals((byte) 0xe, bis.read());
            assertEquals((byte) 0xf, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** */
    @Test
    public void test_2Bit_BE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x79 });
        try (BitInputStream bis = new BitInputStream(bais, 2)) { // 2bit BigEndian
            assertEquals((byte) 0x1, bis.read());
            assertEquals((byte) 0x3, bis.read());
            assertEquals((byte) 0x2, bis.read());
            assertEquals((byte) 0x1, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** */
    @Test
    public void test_2Bit_LE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 2, ByteOrder.LITTLE_ENDIAN)) { // 2bit LittleEndian
            assertEquals((byte) 0x1, bis.read());
            assertEquals((byte) 0x3, bis.read());
            assertEquals((byte) 0x2, bis.read());
            assertEquals((byte) 0x1, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** */
    @Test
    public void test_4Bit_LE_1() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xf5 });
        try (BitInputStream bis = new BitInputStream(bais, 4, ByteOrder.LITTLE_ENDIAN)) { // 4bit LittleEndian
            assertEquals((byte) 0x5, bis.read());
            assertEquals((byte) 0xf, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** */
    @Test
    public void test_4Bit_LE_2() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x5f });
        try (BitInputStream bis = new BitInputStream(bais, 4, ByteOrder.LITTLE_ENDIAN)) { // 4bit LittleEndian
            assertEquals((byte) 0xf, bis.read());
            assertEquals((byte) 0x5, bis.read());
            assertEquals(0, bis.available());
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_3Bit_BE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xb6, (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 3)) { // 3bit BigEndian
            assertEquals(5, bis.available());
            assertEquals(0b101, bis.read());
            assertEquals(0b101, bis.read());
            assertEquals(0b100, bis.read());
            assertEquals(0b110, bis.read());
            assertEquals(0b110, bis.read());
            assertEquals(0, bis.available());
            assertEquals(-1, bis.read());
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_3Bit_LE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xb6, (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 3, ByteOrder.LITTLE_ENDIAN)) { // 3bit LittleEndian
            assertEquals(0b110, bis.read());
            assertEquals(0b110, bis.read());
            assertEquals(0b110, bis.read()); // 1 of 0x6d + 10 of 0xb6
            assertEquals(0b110, bis.read());
            assertEquals(0b110, bis.read());
            assertEquals(0, bis.available());
            assertEquals(-1, bis.read());
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_5Bit_BE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xb6, (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 5)) { // 5bit BigEndian
            assertEquals(0b10110, bis.read());
            assertEquals(0b11001, bis.read());
            assertEquals(0b10110, bis.read());
            assertEquals(-1, bis.read());
        }
    }

    /** 10110110 01101101 */
    @Test
    public void test_5Bit_LE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xb6, (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 5, ByteOrder.LITTLE_ENDIAN)) { // 5bit LittleEndian
            assertEquals(0b10110, bis.read());
            assertEquals(0b01101, bis.read()); // 01 of 0x6d + 101 of 0xb6
            assertEquals(0b11011, bis.read());
            assertEquals(-1, bis.read());
        }
    }

    /** */
    @Test
    public void test_8Bit_BE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xb6, (byte) 0x6d });
        try (BitInputStream bis = new BitInputStream(bais, 8)) {
            assertEquals(0xb6, bis.read());
            assertEquals(0x6d, bis.read());
            assertEquals(-1, bis.read());
        }
    }

    // -------------------------------------------------------------------------

    /** */
    public static void main(String[] args) throws Exception {
        InputStream is1 = new BufferedInputStream(Files.newInputStream(Paths.get(args[0])));
Debug.println(StringUtil.getDump(is1));
        is1.close();
        InputStream is2 = new BitInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(args[0]))));
Debug.println(StringUtil.getDump(is2));
        is2.close();
    }
}
