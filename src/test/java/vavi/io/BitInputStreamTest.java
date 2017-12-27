/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

import vavi.util.Debug;

import static org.junit.Assert.assertEquals;


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

    // -------------------------------------------------------------------------

    /** */
    public static void main(String[] args) throws Exception {
        InputStream is1 = new BufferedInputStream(new FileInputStream(args[0]));
Debug.dump(is1);
        is1.close();
        InputStream is2 = new BitInputStream(new BufferedInputStream(new FileInputStream(args[0])));
Debug.dump(is2);
        is2.close();
    }
}

/* */
