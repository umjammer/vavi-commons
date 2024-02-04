/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * LittleEndianDataOutputStreamTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2004/08/26 nsano initial version <br>
 */
public class LittleEndianDataOutputStreamTest {

    /** */
    ByteArrayOutputStream baos;

    /** */
    LittleEndianDataOutputStream ledos;

    /*
     * @see TestCase#setUp()
     */
    @BeforeEach
    public void setUp() throws Exception {
        baos = new ByteArrayOutputStream();
        ledos = new LittleEndianDataOutputStream(baos);
    }

    /*
     * @see TestCase#tearDown()
     */
    @AfterEach
    public void tearDown() throws Exception {
        ledos.close();
        baos.close();
    }

    @Test
    public void testWriteByte() throws Exception {
        byte[] sample = { (byte) 0x11 };
        ledos.writeByte(0x11);
        byte[] result = baos.toByteArray();
        assertArrayEquals(sample, result);
    }

    @Test
    public void testWriteShort() throws Exception {
        byte[] sample = {
                (byte) 0x22,
                (byte) 0x11
        };
        ledos.writeShort(0x1122);
        byte[] result = baos.toByteArray();
        assertArrayEquals(sample, result);
    }

    @Test
    public void testWriteInt() throws Exception {
        byte[] sample = {
                (byte) 0x44,
                (byte) 0x33,
                (byte) 0x22,
                (byte) 0x11
        };
        ledos.writeInt(0x11223344);
        byte[] result = baos.toByteArray();
        assertArrayEquals(sample, result);
    }

    @Test
    public void testWriteLong() throws Exception {
        byte[] sample = {
                (byte) 0x88,
                (byte) 0x77,
                (byte) 0x66,
                (byte) 0x55,
                (byte) 0x44,
                (byte) 0x33,
                (byte) 0x22,
                (byte) 0x11
        };
        ledos.writeLong(0x1122334455667788L);
        byte[] result = baos.toByteArray();
        assertArrayEquals(sample, result);
    }
}

/* */
