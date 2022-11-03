/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ChannelOutputStreamTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-29 nsano initial version <br>
 */
public class ChannelOutputStreamTest {

    @Test
    void test1() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel sbc = Channels.newChannel(baos);
        ChannelOutputStream cos = new ChannelOutputStream(sbc);
        cos.write("umjammer".getBytes());
        cos.flush();
        cos.close();
        assertEquals("umjammer", baos.toString());
    }

    @Test
    void test2() throws Exception {
        Path path = Paths.get("tmp/ChannelOutputStreamTest_test2.dat");
        if (!Files.exists(path.getParent()))
            Files.createDirectory(path.getParent());
Debug.println(path.toAbsolutePath());
        LittleEndianSeekableDataOutputStream lesdos = new LittleEndianSeekableDataOutputStream(Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE));
        lesdos.write("umjammer".getBytes());
        lesdos.writeFloat(1.234f);
Debug.println("pos: " + lesdos.position());
        lesdos.writeDouble(Math.PI);
        lesdos.position(0);
        lesdos.write("XmjXmmXr".getBytes());
        lesdos.flush();
        lesdos.close();

        LittleEndianSeekableDataInputStream lesdis = new LittleEndianSeekableDataInputStream(Files.newByteChannel(path));
        byte[] b = new byte[8];
        lesdis.readFully(b);
        assertArrayEquals("XmjXmmXr".getBytes(), b);
        lesdis.position(12);
        assertEquals(Math.PI, lesdis.readDouble());
        lesdis.position(8);
        assertEquals(1.234f, lesdis.readFloat());

        Files.delete(path);
    }
}
