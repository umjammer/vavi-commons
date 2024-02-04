/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * ByteUtil.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2019/10/24 nsano initial version <br>
 */
public class ByteUtil {

    private ByteUtil() {
    }

    /** */
    public static byte[] getLeBytes(short value) {
        byte[] bytes = new byte[Short.BYTES];
        writeLeShort(value, bytes);
        return bytes;
    }

    /** */
    public static void writeLeShort(short value, byte[] buffer) {
        writeLeShort(value, buffer, 0);
    }

    /** */
    public static void writeLeShort(short value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
    }

    /** */
    public static byte[] getLeBytes(int value) {
        byte[] bytes = new byte[Integer.BYTES];
        writeLeInt(value, bytes);
        return bytes;
    }

    /** */
    public static void writeLeInt(int value, byte[] buffer) {
        writeLeInt(value, buffer, 0);
    }

    /** */
    public static void writeLeInt(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 3] = (byte) ((value >>> 24) & 0xFF);
    }

    /** */
    public static void writeLeLong(long value, byte[] buffer) {
        writeLeLong(value, buffer, 0);
    }

    /** */
    public static void writeLeLong(long value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 3] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 4] = (byte) ((value >>> 32) & 0xFF);
        buffer[offset + 5] = (byte) ((value >>> 40) & 0xFF);
        buffer[offset + 6] = (byte) ((value >>> 48) & 0xFF);
        buffer[offset + 7] = (byte) ((value >>> 56) & 0xFF);
    }

    /** */
    public static byte[] getBeBytes(short value) {
        byte[] bytes = new byte[Short.BYTES];
        writeBeShort(value, bytes);
        return bytes;
    }

    /** */
    public static void writeBeShort(short value, byte[] buffer) {
        writeBeShort(value, buffer, 0);
    }

    /** */
    public static void writeBeShort(short value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value >>> 8);
        buffer[offset + 1] = (byte) (value & 0xFF);
    }

    /** */
    public static byte[] getBeBytes(int value) {
        byte[] bytes = new byte[Integer.BYTES];
        writeBeInt(value, bytes);
        return bytes;
    }

    /** @since 1.1.10 */
    public static void writeBe24(int value, byte[] buffer) {
        writeBe24(value, buffer, 0);
    }

    /** @since 1.1.10 */
    public static void writeBe24(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 2] = (byte) (value & 0xFF);
    }

    /** */
    public static void writeBeInt(int value, byte[] buffer) {
        writeBeInt(value, buffer, 0);
    }

    /** */
    public static void writeBeInt(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }

    /** */
    public static void writeBeLong(long value, byte[] buffer) {
        writeBeLong(value, buffer, 0);
    }

    /** */
    public static void writeBeLong(long value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 56) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 48) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 40) & 0xFF);
        buffer[offset + 3] = (byte) ((value >>> 32) & 0xFF);
        buffer[offset + 4] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 5] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 6] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 7] = (byte) (value & 0xFF);
    }

    /** */
    public static byte[] getBeBytes(long value) {
        byte[] bytes = new byte[Long.BYTES];
        writeBeLong(value, bytes);
        return bytes;
    }

    /** */
    public static byte[] getLeBytes(long value) {
        byte[] bytes = new byte[Long.BYTES];
        writeLeLong(value, bytes);
        return bytes;
    }

    /** */
    public static short readLeShort(byte[] buffer) {
        return readLeShort(buffer, 0);
    }

    /** */
    public static short readLeShort(byte[] buffer, int offset) {
        return (short) (((buffer[offset + 1] << 8) & 0xFF00) | ((buffer[offset + 0] << 0) & 0x00FF));
    }

    /** */
    public static int readLeInt(byte[] buffer) {
        return readLeInt(buffer, 0);
    }

    /** */
    public static int readLeInt(byte[] buffer, int offset) {
        return ((buffer[offset + 3] << 24) & 0xFF00_0000) | ((buffer[offset + 2] << 16) & 0x00FF_0000) |
            ((buffer[offset + 1] << 8) & 0x0000_FF00) | ((buffer[offset + 0] << 0) & 0x000_000FF);
    }

    /** */
    public static long readLeLong(byte[] buffer) {
        return readLeLong(buffer, 0);
    }

    /** */
    public static long readLeLong(byte[] buffer, int offset) {
        return ((readLeInt(buffer, offset + 4) & 0xffff_ffffL) << 32) |
                (readLeInt(buffer, offset + 0) & 0xffff_ffffL);
    }

    /** */
    public static short readBeShort(byte[] buffer) {
        return readBeShort(buffer, 0);
    }

    /** */
    public static short readBeShort(byte[] buffer, int offset) {
        return (short) (((buffer[offset] << 8) & 0xFF00) | ((buffer[offset + 1] << 0) & 0x00FF));
    }

    /** @since 1.1.10 */
    public static int readBe24(byte[] buffer) {
        return readBe24(buffer, 0);
    }

    /** @since 1.1.10 */
    public static int readBe24(byte[] buffer, int offset) {
        int value = ((buffer[offset + 0] << 16) & 0x00FF_0000) |
                ((buffer[offset + 1] << 8) & 0x0000_FF00) | ((buffer[offset + 2] << 0) & 0x0000_00FF);
        return value;
    }

    /** */
    public static int readBeInt(byte[] buffer) {
        return readBeInt(buffer, 0);
    }

    /** */
    public static int readBeInt(byte[] buffer, int offset) {
        int value = ((buffer[offset + 0] << 24) & 0xFF00_0000) | ((buffer[offset + 1] << 16) & 0x00FF_0000) |
            ((buffer[offset + 2] << 8) & 0x0000_FF00) | ((buffer[offset + 3] << 0) & 0x0000_00FF);
        return value;
    }

    /** */
    public static long readBeLong(byte[] buffer) {
        return readBeLong(buffer, 0);
    }

    /** */
    public static long readBeLong(byte[] buffer, int offset) {
        return ((readBeInt(buffer, offset + 0) & 0xffff_ffffL) << 32) |
                (readBeInt(buffer, offset + 4) & 0xffff_ffffL);
    }

    /** */
    public static void writeLeUUID(UUID value, byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]).order(ByteOrder.BIG_ENDIAN);
        bb.putLong(value.getMostSignificantBits());
        bb.putLong(value.getLeastSignificantBits());
        byte[] le = bb.array();
        writeLeInt(readBeInt(le, 0), buffer, offset + 0);
        writeLeShort(readBeShort(le, 4), buffer, offset + 4);
        writeLeShort(readBeShort(le, 6), buffer, offset + 6);
        System.arraycopy(le, 8, buffer, offset + 8, 8);
    }

    /** */
    public static void writeBeUUID(UUID value, byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]).order(ByteOrder.BIG_ENDIAN);
        bb.putLong(value.getMostSignificantBits());
        bb.putLong(value.getLeastSignificantBits());
        byte[] be = bb.array();
        System.arraycopy(be, 0, buffer, offset, 16);
    }

    /** */
    public static UUID readLeUUID(byte[] buffer, int offset) {
        byte[] temp = new byte[16];
        writeBeInt(readLeInt(buffer, offset + 0), temp, 0);
        writeBeShort(readLeShort(buffer, offset + 4), temp, 4);
        writeBeShort(readLeShort(buffer, offset + 6), temp, 6);
        System.arraycopy(buffer, offset + 8, temp, 8, 8);
        ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }

    /** */
    public static UUID readBeUUID(byte[] buffer, int offset) {
        byte[] temp = new byte[16];
        System.arraycopy(buffer, offset, temp, 0, 16);
        ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }

    /** @return in small case */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /** @return length */
    public static int strlen(byte[] buf) {
        return strlen(buf, 0);
    }

    /** @return length */
    public static int strlen(byte[] buf, int pos) {
        for (int i = pos; i < buf.length; i++) {
            if (buf[i] == 0) {
                return i - pos;
            }
        }
        return buf.length - pos;
    }

    /** @return index, -1: not found */
    public static int indexOf(byte[] buf, byte target) {
        return indexOf(buf, target, 0);
    }

    /** @return index, -1: not found */
    public static int indexOf(byte[] buf, byte target, int pos) {
        for (int i = pos; i < buf.length; i++) {
            if (buf[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /** @since 1.1.9 */
    public static byte[] toByteArray(List<Byte> o) {
        byte[] a = new byte[o.size()];
        IntStream.range(0, o.size()).forEach(i -> a[i] = o.get(i));
        return a;
    }

    /** @since 1.1.9 */
    public static List<Byte> toList(byte[] o) {
        List<Byte> a = new ArrayList<>(o.length);
        IntStream.range(0, o.length).forEach(i -> a.add(o[i]));
        return a;
    }

    /** @since 1.1.9 */
    public static <T extends Number> byte[] toByteArrayGenerics(List<T> o) {
        byte[] a = new byte[o.size()];
        IntStream.range(0, o.size()).forEach(i -> a[i] = o.get(i).byteValue());
        return a;
    }

    /** @since 1.1.9 */
    public static byte[] hexStringToBytes(String h) {
        return toByteArrayGenerics(Arrays.stream(h.split("(?<=\\G.{2})"))
                .map(a -> Integer.parseInt(a, 16)).collect(Collectors.toList()));
    }
}

/* */
