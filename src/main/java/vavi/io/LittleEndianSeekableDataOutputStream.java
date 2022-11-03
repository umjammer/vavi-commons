/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;


/**
 * LittleEndianSeekableDataOutputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-29 nsano initial version <br>
 */
public class LittleEndianSeekableDataOutputStream extends OutputStream
        implements LittleEndianDataOutput, SeekableDataOutput<SeekableByteChannel> {

    private SeekableByteChannel sbc;
    private LittleEndianDataOutputStream ledos;

    public LittleEndianSeekableDataOutputStream(SeekableByteChannel sbc) {
        this.sbc = sbc;
        ledos = new LittleEndianDataOutputStream(new ChannelOutputStream(sbc));
    }

    @Override
    public void write(int b) throws IOException {
        ledos.write(b);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        ledos.writeDouble(d);
    }

    @Override
    public void writeFloat(float j) throws IOException {
        ledos.writeFloat(j);
    }

    @Override
    public void writeByte(int b) throws IOException {
        ledos.writeByte(b);
    }

    @Override
    public void writeChar(int c) throws IOException {
        ledos.writeChar(c);
    }

    @Override
    public void writeInt(int i) throws IOException {
        ledos.writeInt(i);
    }

    @Override
    public void writeShort(int s) throws IOException {
        ledos.writeShort(s);
    }

    @Override
    public void writeLong(long l) throws IOException {
        ledos.writeLong(l);
    }

    @Override
    public void writeBoolean(boolean z) throws IOException {
        ledos.writeBoolean(z);
    }

    @Override
    public void writeBytes(String b) throws IOException {
        ledos.writeBytes(b);
    }

    @Override
    public void writeChars(String c) throws IOException {
        ledos.writeChars(c);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        ledos.writeUTF(s);
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
