/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataOutput;
import java.io.IOException;


/**
 * LittleEndianDataOutput.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040929 nsano initial version <br>
 */
public interface LittleEndianDataOutput extends DataOutput {

    /** @see "https://en.wikipedia.org/wiki/Endianness#Floating_point" */
    @Override
    void writeDouble(double d) throws IOException;

    /** @see "https://en.wikipedia.org/wiki/Endianness#Floating_point" */
    @Override
    void writeFloat(float f) throws IOException;

    @Override
    void writeByte(int b) throws IOException;

    @Override
    void writeChar(int c) throws IOException;

    @Override
    void writeInt(int i) throws IOException;

    @Override
    void writeShort(int s) throws IOException;

    @Override
    void writeLong(long l) throws IOException;

    @Override
    void writeBoolean(boolean z) throws IOException;

    @Override
    void writeBytes(String b) throws IOException;

    @Override
    void writeChars(String s) throws IOException;

    @Override
    void writeUTF(String s) throws IOException;
}

/* */
