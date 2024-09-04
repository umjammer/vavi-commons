/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


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

        ByteUtil.writeBeLong(0xfedcba9876543210L, buf, 0);
        long v = ByteUtil.readBeLong(buf, 0);
        assertEquals(0xfedcba9876543210L, v);
    }

    @Test
    void testBe64U() throws Exception {
        byte[] buf = new byte[8];

        ByteUtil.writeBeLong(0xfedc_ba98_7654_3210L, buf, 0);
        long v = ByteUtil.readBeLong(buf, 0);
        assertEquals(0xfedc_ba98_7654_3210L, v);
    }

    @Test
    void testLe64() throws Exception {
        byte[] buf = new byte[8];
        ByteUtil.writeLeLong(0xfed_cba98_7654_3210L, buf, 0);
        long v = ByteUtil.readLeLong(buf, 0);
        assertEquals(0xfedc_ba98_7654_3210L, v);
    }

    @Test
    void testLe64U() throws Exception {
        byte[] buf = new byte[8];
        ByteUtil.writeLeLong(0xfedc_ba98_7654_3210L, buf, 0);
        long v = ByteUtil.readLeLong(buf, 0);
        assertEquals(0xfedc_ba98_7654_3210L, v);
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
    void textUUID() {
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

    @Test
    void testToHexString() {
        byte[] b = new byte[] {0x12, 0x34, 0x56, 0x78, (byte) 0xde, (byte) 0xad, (byte) 0xbe, (byte) 0xef};
        assertEquals("12345678deadbeef", ByteUtil.toHexString(b));
        byte[] b2 = new byte[] {(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe};
        assertEquals("cafebabe", ByteUtil.toHexString(b2));
    }

    @Test
    void testStrlen() {
        byte[] b = "umjammer\u0000vavivavi".getBytes();
        assertEquals(8, ByteUtil.strlen(b));
        assertEquals(5, ByteUtil.strlen(b, 3));
        assertEquals(8, ByteUtil.strlen(b, 9));

    }
    @Test
    void testIndexOf() {
        byte[] b = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.US_ASCII);
        assertEquals(13, ByteUtil.indexOf(b, (byte) 'n'));
        assertEquals(14, ByteUtil.indexOf(b, (byte) 'o', 13));
        assertEquals(14, ByteUtil.indexOf(b, (byte) 'o', 14));
        assertEquals(-1, ByteUtil.indexOf(b, (byte) 'a', 14));
    }

    @Test
    void testToByteArray() {
        byte[] r = ByteUtil.toByteArray(Arrays.asList((byte) 0x30, (byte) 0x31, (byte) 0x32));
        assertArrayEquals(new byte[] {'0', '1', '2'}, r);
    }

    @Test
    void testToList() throws Exception {
        List<Byte> r = ByteUtil.toList(new byte[] {'0', '1', '2'});
        assertIterableEquals(Arrays.asList((byte) '0', (byte) '1', (byte) '2'), r);
    }

    @Test
    void testToByteArrayGenerics() {
        byte[] r = ByteUtil.toByteArrayGenerics(Arrays.asList(0x30, 0x31, 0x32));
        assertArrayEquals(new byte[] {'0', '1', '2'}, r);
        r = ByteUtil.toByteArrayGenerics(Arrays.asList(0x80, 0x31, 0xf2));
        assertArrayEquals(new byte[] {(byte) 0x80, '1', (byte) 0xf2}, r);
    }

    @Test
    void testHexStringToBytes() {
        byte[] r = ByteUtil.hexStringToBytes("303132");
        assertArrayEquals(new byte[] {'0', '1', '2'}, r);
        r = ByteUtil.hexStringToBytes("8031f2");
        assertArrayEquals(new byte[] {(byte) 0x80, '1', (byte) 0xf2}, r);
    }

    @Test
    void test24() throws Exception {
        byte[] buf = new byte[3];

        ByteUtil.writeBe24(0xfedcba, buf, 0);
        assertArrayEquals(new byte[] { (byte) 0xfe, (byte) 0xdc, (byte) 0xba }, buf);
        int v = ByteUtil.readBe24(buf, 0);
        assertEquals(0xfedcba, v);

        ByteUtil.writeLe24(0xfedcba, buf, 0);
        assertArrayEquals(new byte[] { (byte) 0xba, (byte) 0xdc, (byte) 0xfe }, buf);
        v = ByteUtil.readLe24(buf, 0);
        assertEquals(0xfedcba, v);

        ByteUtil.writeBe24(0x123456, buf, 0);
        v = ByteUtil.readBe24(buf, 0);
        assertEquals(0x123456, v);

        ByteUtil.writeLe24(0x123456, buf, 0);
        v = ByteUtil.readLe24(buf, 0);
        assertEquals(0x123456, v);
    }
}
