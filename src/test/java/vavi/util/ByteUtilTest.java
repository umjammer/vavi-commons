/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ByteUtilTest.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2019/10/13 nsano initial version <br>
 */
class ByteUtilTest {

    @Test
    void test16() throws Exception {
        byte[] buf = new byte[2];

        ByteUtil.writeBeShort((short) 0xfedc, buf, 0);
        short v = ByteUtil.readBeShort(buf, 0);
        assertEquals((short) 0xfedc, v);

        ByteUtil.writeLeShort((short) 0xfedc, buf, 0);
        v = ByteUtil.readLeShort(buf, 0);
        assertEquals((short) 0xfedc, v);

        ByteUtil.writeBeShort((short) 0xfedc, buf, 0);
        v = ByteUtil.readBeShort(buf, 0);
        assertEquals((short) 0xfedc, v);

        ByteUtil.writeLeShort((short) 0xfedc, buf, 0);
        v = ByteUtil.readLeShort(buf, 0);
        assertEquals((short) 0xfedc, v);
    }

    @Test
    void test32() throws Exception {
        byte[] buf = new byte[4];

        ByteUtil.writeBeInt(0xfedcba98, buf, 0);
        int v = ByteUtil.readBeInt(buf, 0);
        assertEquals(0xfedcba98, v);

        ByteUtil.writeLeInt(0xfedcba98, buf, 0);
        v = ByteUtil.readLeInt(buf, 0);
        assertEquals(0xfedcba98, v);

        ByteUtil.writeBeInt(0xfedcba98, buf, 0);
        v = ByteUtil.readBeInt(buf, 0);
        assertEquals(0xfedcba98, v);

        ByteUtil.writeLeInt(0xfedcba98, buf, 0);
        v = ByteUtil.readLeInt(buf, 0);
        assertEquals(0xfedcba98, v);
    }

    @Test
    void testBe64() throws Exception {
        byte[] buf = new byte[8];

        ByteUtil.writeBeLong(0xfedcba9876543210l, buf, 0);
        long v = ByteUtil.readBeLong(buf, 0);
        assertEquals(0xfedcba9876543210l, v);
    }

    @Test
    void testBe64U() throws Exception {
        byte[] buf = new byte[8];

        ByteUtil.writeBeLong(0xfedcba9876543210l, buf, 0);
        long v = ByteUtil.readBeLong(buf, 0);
        assertEquals(0xfedcba9876543210l, v);
    }

    @Test
    void testLe64() throws Exception {
        byte[] buf = new byte[8];
        ByteUtil.writeLeLong(0xfedcba9876543210l, buf, 0);
        long v = ByteUtil.readLeLong(buf, 0);
        assertEquals(0xfedcba9876543210l, v);
    }

    @Test
    void testLe64U() throws Exception {
        byte[] buf = new byte[8];
        ByteUtil.writeLeLong(0xfedcba9876543210l, buf, 0);
        long v = ByteUtil.readLeLong(buf, 0);
        assertEquals(0xfedcba9876543210l, v);
    }

    @Test
    void testBeUUID() throws Exception {
        byte[] buf = new byte[16];
        UUID uuid = UUID.randomUUID();

        ByteUtil.writeBeUUID(uuid, buf, 0);
        UUID v = ByteUtil.readBeUUID(buf, 0);
        assertEquals(uuid, v);

        UUID uuid2 = UUID.fromString("6d7ad8eb-f061-4aac-8b7e-4da04d959d77");
        ByteUtil.writeBeUUID(uuid2, buf, 0);
        UUID v2 = ByteUtil.readBeUUID(buf, 0);
        assertEquals(uuid2, v2);
    }

    @Test
    void testLeUUID() throws Exception {
        byte[] buf = new byte[16];
        UUID uuid = UUID.randomUUID();

        ByteUtil.writeLeUUID(uuid, buf, 0);
        UUID v = ByteUtil.readLeUUID(buf, 0);
        assertEquals(uuid, v);

        UUID uuid2 = UUID.fromString("6d7ad8eb-f061-4aac-8b7e-4da04d959d77");
        ByteUtil.writeLeUUID(uuid2, buf, 0);
        UUID v2 = ByteUtil.readLeUUID(buf, 0);
        assertEquals(uuid2, v2);
    }

    @Test
    void textXXX() {
        byte[] bytes = new byte[] {
            0x66, 0x77, (byte) 0xC2, 0x2D, 0x23, (byte) 0xF6, 0x00, 0x42, (byte) 0x9D, 0x64, 0x11, 0x5E, (byte) 0x9B,
            (byte) 0xFD, 0x4A, 0x08
        };
        UUID v = ByteUtil.readLeUUID(bytes, 0);
        UUID expected = UUID.fromString("2dc27766-f623-4200-9d64-115e9bfd4a08");
        assertEquals(expected, v);

        byte[] buf = new byte[16];
        ByteUtil.writeLeUUID(expected, buf, 0);
        assertArrayEquals(bytes, buf);
    }
}

/* */
