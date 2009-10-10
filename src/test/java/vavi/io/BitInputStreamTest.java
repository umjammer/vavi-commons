/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;

import junit.framework.TestCase;


/**
 * BitInputStreamTest.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040919 nsano initial version <br>
 */
public class BitInputStreamTest extends TestCase {

    /** */
    public void test_4Bit_BE_1() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xf5 });
        BitInputStream bis = new BitInputStream(bais); // 4bit BigEndian
        assertEquals((byte) 0xf, bis.read());
        assertEquals((byte) 0x5, bis.read());
    }

    /** */
    public void test_4Bit_BE_2() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x5f });
        BitInputStream bis = new BitInputStream(bais); // 4bit BigEndian
        assertEquals((byte) 0x5, bis.read());
        assertEquals((byte) 0xf, bis.read());
    }

    /** */
    public void test_2Bit_BE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x79 });
        BitInputStream bis = new BitInputStream(bais, 2); // 4bit BigEndian
        assertEquals((byte) 0x1, bis.read());
        assertEquals((byte) 0x3, bis.read());
        assertEquals((byte) 0x2, bis.read());
        assertEquals((byte) 0x1, bis.read());
    }

    /** */
    public void test_2Bit_LE() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x6d });
        BitInputStream bis = new BitInputStream(bais, 2, ByteOrder.LITTLE_ENDIAN); // 4bit LittleEndian
        assertEquals((byte) 0x1, bis.read());
        assertEquals((byte) 0x3, bis.read());
        assertEquals((byte) 0x2, bis.read());
        assertEquals((byte) 0x1, bis.read());
    }

    /** */
    public void test_4Bit_LE_1() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0xf5 });
        BitInputStream bis = new BitInputStream(bais, 4, ByteOrder.LITTLE_ENDIAN); // 4bit LittleEndian
        assertEquals((byte) 0x5, bis.read());
        assertEquals((byte) 0xf, bis.read());
    }

    /** */
    public void test_4Bit_LE_2() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { (byte) 0x5f });
        BitInputStream bis = new BitInputStream(bais, 4, ByteOrder.LITTLE_ENDIAN); // 4bit LittleEndian
        assertEquals((byte) 0xf, bis.read());
        assertEquals((byte) 0x5, bis.read());
    }
}

/* */
