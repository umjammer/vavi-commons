/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;


/**
 * ByteUtil.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2019/10/24 nsano initial version <br>
 */
public class ByteUtil {

    private ByteUtil() {
    }

    public static void writeLeShort(short value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
    }

    public static void writeLeInt(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 3] = (byte) ((value >>> 24) & 0xFF);
    }

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

    public static void writeBeShort(short value, byte[] buffer, int offset) {
        buffer[offset] = (byte) (value >>> 8);
        buffer[offset + 1] = (byte) (value & 0xFF);
    }

    public static void writeBeInt(int value, byte[] buffer, int offset) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }

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

    public static short readLeShort(byte[] buffer, int offset) {
        return (short) (((buffer[offset + 1] << 8) & 0xFF00) | ((buffer[offset + 0] << 0) & 0x00FF));
    }

    public static int readLeInt(byte[] buffer, int offset) {
        return ((buffer[offset + 3] << 24) & 0xFF000000) | ((buffer[offset + 2] << 16) & 0x00FF0000) |
            ((buffer[offset + 1] << 8) & 0x0000FF00) | ((buffer[offset + 0] << 0) & 0x000000FF);
    }

    public static long readLeLong(byte[] buffer, int offset) {
        return ((readLeInt(buffer, offset + 4) & 0xffffffffl) << 32) |
                (readLeInt(buffer, offset + 0) & 0xffffffffl);
    }

    public static short readBeShort(byte[] buffer, int offset) {
        return (short) (((buffer[offset] << 8) & 0xFF00) | ((buffer[offset + 1] << 0) & 0x00FF));
    }

    public static int readBeInt(byte[] buffer, int offset) {
        int value = ((buffer[offset + 0] << 24) & 0xFF000000) | ((buffer[offset + 1] << 16) & 0x00FF0000) |
            ((buffer[offset + 2] << 8) & 0x0000FF00) | ((buffer[offset + 3] << 0) & 0x000000FF);
        return value;
    }

    public static long readBeLong(byte[] buffer, int offset) {
        return ((readBeInt(buffer, offset + 0) & 0xffffffffl) << 32) |
                (readBeInt(buffer, offset + 4) & 0xffffffffl);
    }

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

    public static void writeBeUUID(UUID value, byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]).order(ByteOrder.BIG_ENDIAN);
        bb.putLong(value.getMostSignificantBits());
        bb.putLong(value.getLeastSignificantBits());
        byte[] be = bb.array();
        System.arraycopy(be, 0, buffer, offset, 16);
    }

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

    public static UUID readBeUUID(byte[] buffer, int offset) {
        byte[] temp = new byte[16];
        System.arraycopy(buffer, offset, temp, 0, 16);
        ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }
}

/* */
