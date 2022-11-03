/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataInput;
import java.io.IOException;


/**
 * LittleEndianDataInput.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040929 nsano initial version <br>
 */
public interface LittleEndianDataInput extends DataInput {
    @Override
    short readShort() throws IOException;

    @Override
    int readInt() throws IOException;

    @Override
    long readLong() throws IOException;

    @Override
    String readUTF() throws IOException;

    @Override
    String readLine() throws IOException;

    @Override
    void readFully(byte[] b, int offset, int len) throws IOException;

    @Override
    void readFully(byte[] b) throws IOException;

    @Override
    int skipBytes(int len) throws IOException;

    @Override
    boolean readBoolean() throws IOException;

    @Override
    int readUnsignedShort() throws IOException;

    @Override
    int readUnsignedByte() throws IOException;

    @Override
    float readFloat() throws IOException;

    @Override
    double readDouble() throws IOException;

    @Override
    char readChar() throws IOException;

    @Override
    byte readByte() throws IOException;
}

/* */
