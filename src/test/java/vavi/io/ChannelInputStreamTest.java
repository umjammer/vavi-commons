/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ChannelInputStreamTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/10 umjammer initial version <br>
 */
class ChannelInputStreamTest {

    @Test
    void test() throws IOException {
        Path path = Paths.get("src/test/resources/cis-test.dat");
        SeekableByteChannel bc = Files.newByteChannel(path);
        DataInputStream dis = new DataInputStream(new ChannelInputStream(bc));
        bc.position(55);
        int c = dis.read();
        assertEquals(55, c);
        bc.position(22);
        c = dis.read();
        assertEquals(22, c);
        bc.position(222);
        c = dis.read();
        assertEquals(222, c);
        dis.close();
    }

    @Test
    void test2() throws IOException {
        Path path = Paths.get("src/test/resources/cis-test.dat");
        FileInputStream fis = new FileInputStream(path.toFile());
        FileChannel fc = fis.getChannel();
        DataInputStream dis = new DataInputStream(fis);
        fc.position(55);
        int c = dis.read();
        assertEquals(55, c);
        fc.position(22);
        c = dis.read();
        assertEquals(22, c);
        fc.position(222);
        c = dis.read();
        assertEquals(222, c);
        fis.close();
    }

    @Test
    void test3() throws IOException {
        Path path = Paths.get("src/test/resources/cis-test.dat");
        SeekableByteChannel sbc = Files.newByteChannel(path);
        SeekableDataInput<?> di = new LittleEndianSeekableDataInputStream(sbc);
        di.position(55);
        int c = di.readUnsignedByte();
        assertEquals(55, c);
        di.position(22);
        c = di.readUnsignedByte();
        assertEquals(22, c);
        di.position(222);
        c = di.readUnsignedByte();
        assertEquals(222, c);
        short s = di.readShort();
        assertEquals((short) 0xe0df, s);
        sbc.close();
    }

    @Test
    void test4() throws IOException {
        Path path = Paths.get("src/test/resources/cis-test.dat");
        SeekableByteChannel sbc = Files.newByteChannel(path);
        SeekableDataInput<?> di = new SeekableDataInputStream(sbc);
        di.position(55);
        int c = di.readUnsignedByte();
        assertEquals(55, c);
        di.position(22);
        c = di.readUnsignedByte();
        assertEquals(22, c);
        di.position(222);
        c = di.readUnsignedByte();
        assertEquals(222, c);
        short s = di.readShort();
        assertEquals((short) 0xdfe0, s);
        sbc.close();
    }

    //----

    /**
     * creates test data.
     */
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/test/resources/cis-test.dat");
        OutputStream os = Files.newOutputStream(path);
        for (int i = 0; i < 256; i++) {
            os.write(i);
        }
        os.close();
    }
}
