/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * LittleEndianSeekableDataInputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/11 umjammer initial version <br>
 */
public class LittleEndianSeekableDataInputStream extends InputStream
    implements LittleEndianDataInput, SeekableDataInput<SeekableByteChannel> {

    private final SeekableByteChannel sbc;
    private final LittleEndianDataInputStream ledis;

    public LittleEndianSeekableDataInputStream(SeekableByteChannel sbc) {
        this.sbc = sbc;
        ledis = new LittleEndianDataInputStream(new ChannelInputStream(sbc));
    }

    @Override
    public void position(long pos) throws IOException {
        sbc.position(pos);
Debug.printf(Level.FINER, "%d, %d", pos, position());
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
        return ledis.read();
    }

    @Override
    public short readShort() throws IOException {
        return ledis.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return ledis.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return ledis.readLong();
    }

    @Override
    public String readUTF() throws IOException {
        return ledis.readUTF();
    }

    @Override
    @Deprecated
    public String readLine() throws IOException {
        return ledis.readLine();
    }

    @Override
    public void readFully(byte[] b, int offset, int len) throws IOException {
        ledis.readFully(b, offset, len); // TODO maybe different from spec.
Debug.printf(Level.FINER, "%d, %d", len, ledis.available());
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        ledis.readFully(b);
    }

    @Override
    public int skipBytes(int len) throws IOException {
        return ledis.skipBytes(len);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return ledis.readBoolean();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return ledis.readUnsignedShort();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return ledis.readUnsignedByte();
    }

    @Override
    public float readFloat() throws IOException {
        return ledis.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return ledis.readDouble();
    }

    @Override
    public char readChar() throws IOException {
        return ledis.readChar();
    }

    @Override
    public byte readByte() throws IOException {
        return ledis.readByte();
    }

    @Override
    public SeekableByteChannel origin() {
        return sbc;
    }
}
