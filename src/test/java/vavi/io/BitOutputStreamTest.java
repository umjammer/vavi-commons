/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import junit.framework.TestCase;


/**
 * BitOutputStreamTest.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040919 nsano initial version <br>
 */
public class BitOutputStreamTest extends TestCase {

    /** */
    public void test_4Bit_BE_1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos); // 4bit BigEndian
        bos.write(0xf);
        bos.write(0x5);
        bos.flush();
        assertEquals((byte) 0xf5, baos.toByteArray()[0]); 
    }

    /** */
    public void test_4Bit_BE_2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos); // 4bit BigEndian
        bos.write(0x5);
        bos.write(0xf);
        bos.flush();
        assertEquals((byte) 0x5f, baos.toByteArray()[0]); 
    }

    /**
     * <pre>
     * 01 | 11 | 10 | 01 | -> 01 | 11 | 10 | 01 |
     * </pre>
     */
    public void test_2Bit_BE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos, 2); // 2bit BigEndian
        bos.write(0x01);
        bos.write(0x03);
        bos.write(0x02);
        bos.write(0x01);
        bos.flush();
        assertEquals((byte) 0x79, baos.toByteArray()[0]); 
    }

    /**
     * <pre>
     * 01 | 11 | 10 | 01 | -> | 01 | 10 | 11 | 01
     * </pre>
     */
    public void test_2Bit_LE() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos, 2, ByteOrder.LITTLE_ENDIAN); // 2bit LittleEndian
        bos.write(0x01);
        bos.write(0x03);
        bos.write(0x02);
        bos.write(0x01);
        bos.flush();
        assertEquals((byte) 0x6d, baos.toByteArray()[0]); 
    }

    /** */
    public void test_4Bit_LE_1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos, 4, ByteOrder.LITTLE_ENDIAN); // 4bit LittleEndian
        bos.write(0xf);
        bos.write(0x5);
        bos.flush();
        assertEquals((byte) 0x5f, baos.toByteArray()[0]); 
    }

    /** */
    public void test_4Bit_LE_2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos, 4, ByteOrder.LITTLE_ENDIAN); // 4bit LittleEndian
        bos.write(0x5);
        bos.write(0xf);
        bos.flush();
        assertEquals((byte) 0xf5, baos.toByteArray()[0]); 
    }
}

/* */
