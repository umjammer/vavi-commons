/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;


/**
 * SeekableDataInputStream.
 * <p>
 * Don't use this class in an API library. It makes low compatibility.
 * This class is for inner API use or test cases.
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/18 umjammer initial version <br>
 */
public class SeekableDataInputStream extends InputStream
    implements SeekableDataInput<SeekableByteChannel> {

    private final SeekableByteChannel sbc;
    private final DataInputStream dis;

    public SeekableDataInputStream(SeekableByteChannel sbc) {
        this.sbc = sbc;
        dis = new DataInputStream(new ChannelInputStream(sbc));
    }

    @Override
    public void position(long pos) throws IOException {
        sbc.position(pos);
    }

    @Override
    public long position() throws IOException {
        return sbc.position();
    }

    @Override
    public void close() throws IOException {
        sbc.close();
    }

    @Override
    public int read() throws IOException {
        return dis.read();
    }

    @Override
    public short readShort() throws IOException {
        return dis.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return dis.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return dis.readLong();
    }

    @Override
    public String readUTF() throws IOException {
        return dis.readUTF();
    }

    @Override
    @Deprecated
    public String readLine() throws IOException {
        return dis.readLine();
    }

    @Override
    public void readFully(byte[] b, int offset, int len) throws IOException {
        dis.readFully(b, offset, len);
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        dis.readFully(b);
    }

    @Override
    public int skipBytes(int len) throws IOException {
        return dis.skipBytes(len);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return dis.readUnsignedShort();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return dis.readUnsignedByte();
    }

    @Override
    public float readFloat() throws IOException {
        return dis.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return dis.readDouble();
    }

    @Override
    public char readChar() throws IOException {
        return dis.readChar();
    }

    @Override
    public byte readByte() throws IOException {
        return dis.readByte();
    }

    @Override
    public SeekableByteChannel origin() {
        return sbc;
    }
}
