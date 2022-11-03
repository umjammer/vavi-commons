/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;


/**
 * SeekableDataOutputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-29 nsano initial version <br>
 */
public class SeekableDataOutputStream extends OutputStream
        implements SeekableDataOutput<SeekableByteChannel> {

    private SeekableByteChannel sbc;
    private DataOutputStream dos;

    public SeekableDataOutputStream(SeekableByteChannel sbc) {
        this.sbc = sbc;
        dos = new DataOutputStream(new ChannelOutputStream(sbc));
    }

    @Override
    public void write(int b) throws IOException {
        dos.write(b);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        dos.writeDouble(d);
    }

    @Override
    public void writeFloat(float j) throws IOException {
        dos.writeDouble(j);
    }

    @Override
    public void writeByte(int b) throws IOException {
        dos.writeByte(b);
    }

    @Override
    public void writeChar(int arg0) throws IOException {
        dos.writeChar(arg0);
    }

    @Override
    public void writeInt(int i) throws IOException {
        dos.writeInt(i);
    }

    @Override
    public void writeShort(int s) throws IOException {
        dos.writeShort(s);
    }

    @Override
    public void writeLong(long l) throws IOException {
        dos.writeLong(l);
    }

    @Override
    public void writeBoolean(boolean z) throws IOException {
        dos.writeBoolean(z);
    }

    @Override
    public void writeBytes(String b) throws IOException {
        dos.writeBytes(b);
    }

    @Override
    public void writeChars(String c) throws IOException {
        dos.writeChars(c);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        dos.writeUTF(s);
    }

    @Override
    public void position(long newPosition) throws IOException {
        sbc.position(newPosition);
    }

    @Override
    public long position() throws IOException {
        return sbc.position();
    }

    @Override
    public SeekableByteChannel origin() {
        return sbc;
    }
}
