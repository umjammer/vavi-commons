/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vavi.util.Debug;
import vavi.util.StringUtil;

import static org.junit.Assert.assertEquals;


/**
 * LittleEndianDataInputStreamTest.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040826 nsano initial version <br>
 */
public class LittleEndianDataInputStreamTest {

    /** */
    ByteArrayInputStream bais;

    /** */
    LittleEndianDataInputStream ledis;

    /** */
    ByteArrayInputStream bais2;

    /** */
    LittleEndianDataInputStream ledis2;

    /** */
    @Before
    public void setUp() throws Exception {
        byte[] sample = {
                (byte) 0x11,
                (byte) 0x22,
                (byte) 0x33,
                (byte) 0x44,
                (byte) 0x55,
                (byte) 0x66,
                (byte) 0x77,
                (byte) 0x88
        };
        bais = new ByteArrayInputStream(sample);
        ledis = new LittleEndianDataInputStream(bais);

        byte[] sample2 = {
            (byte) 0xf1,
            (byte) 0xf2,
            (byte) 0xf3,
            (byte) 0xf4,
            (byte) 0xf5,
            (byte) 0xf6,
            (byte) 0xf7,
            (byte) 0xf8
        };
        bais2 = new ByteArrayInputStream(sample2);
        ledis2 = new LittleEndianDataInputStream(bais2);
    }

    /** */
    @After
    public void tearDown() throws Exception {
        ledis.close();
        bais.close();
    }

    /** */
    @Test
    public void testReadShort() throws Exception {
        short actual = ledis.readShort();
        assertEquals(0x2211, actual);
    }

    /** */
    @Test
    public void testReadInt() throws Exception {
        int actual = ledis.readInt();
Debug.println(StringUtil.toHex4(actual));
        assertEquals(0x44332211, actual);
    }

    /** */
    @Test
    public void testReadLong() throws Exception  {
        long actual = ledis.readLong();
        assertEquals(0x8877665544332211L, actual);
    }

    /** */
    @Test
    public void testReadShort2() throws Exception {
        short actual = ledis2.readShort();
        assertEquals((short) 0xf2f1, actual);
    }

    /** */
    @Test
    public void testReadUnsignedShort() throws Exception {
        int actual = ledis.readUnsignedShort();
        assertEquals(0x2211, actual);
    }

    /** */
    @Test
    public void testReadUnsignedShort2() throws Exception {
        int actual = ledis2.readUnsignedShort();
        assertEquals(0xf2f1, actual);
    }

    /** */
    @Test
    public void testReadInt2() throws Exception {
        int actual = ledis2.readInt();
Debug.println(StringUtil.toHex4(actual));
        assertEquals(0xf4f3f2f1, actual);
    }

    /** */
    @Test
    public void testReadLong2() throws Exception  {
        long actual = ledis2.readLong();
        assertEquals(0xf8f7f6f5f4f3f2f1L, actual);
    }
}
