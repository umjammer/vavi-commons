/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * LittleEndianDataOutputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040826 nsano initial version <br>
 */
public class LittleEndianDataOutputStream extends FilterOutputStream
    implements LittleEndianDataOutput {

    /**
     * Creates Little Endian OutputStream.
     */
    public LittleEndianDataOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        // @see "https://en.wikipedia.org/wiki/Endianness#Floating_point"
        writeLong(Double.doubleToLongBits(d));
    }

    @Override
    public void writeFloat(float f) throws IOException {
        // @see "https://en.wikipedia.org/wiki/Endianness#Floating_point"
        writeInt(Float.floatToIntBits(f));
    }

    @Override
    public void writeByte(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void writeChar(int c) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void writeInt(int i) throws IOException {
        writeShort((i & 0xffff));
        writeShort(((i >> 16) & 0xffff));
    }

    @Override
    public void writeShort(int s) throws IOException {
        out.write(s & 0xff);
        out.write((s >> 8) & 0xff);
    }

    @Override
    public void writeLong(long l) throws IOException {
        writeInt((int) l);
        writeInt((int) (l >> 32));
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void writeBytes(String s) throws IOException {
        for (byte b : s.getBytes()) {
            writeByte(b);
        }
    }

    @Override
    public void writeChars(String s) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }
}

/* */
