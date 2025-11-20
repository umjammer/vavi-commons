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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Test
    public void testReadBoolean() throws Exception {
        boolean[] bools = new boolean[] {false, true, true, false, true};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(baos);
        for (boolean b : bools) {
            ledos.writeBoolean(b);
        }
        byte[] bytes = baos.toByteArray();
        assertFalse(bytes[0] != 0);
        assertTrue(bytes[1] != 0);
        assertTrue(bytes[2] != 0);
        assertFalse(bytes[3] != 0);
        assertTrue(bytes[4] != 0);
    }

    @Test
    public void testReadChar() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(baos);
        ledos.writeChar('佐');
        assertArrayEquals(new byte[] {0x50, 0x4f}, baos.toByteArray());
    }
}
