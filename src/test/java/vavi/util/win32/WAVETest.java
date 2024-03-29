/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * WAVETest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
class WAVETest {

    @Test
    void test() throws Exception {
        InputStream is = new BufferedInputStream(WAVETest.class.getResourceAsStream("/test.wav"));
        WAVE wave = WAVE.readFrom(is, WAVE.class);
        assertEquals(176470, wave.getLength());
        assertNotNull(wave.findChildOf(WAVE.data.class).getWave());
    }

    @Test
    void test2() throws Exception {
        System.setProperty("vavi.util.win32.WAVE.data.notLoadData", "true");
        InputStream is = new BufferedInputStream(WAVETest.class.getResourceAsStream("/test.wav"));
        WAVE wave = WAVE.readFrom(is, WAVE.class);
        assertEquals(176470, wave.getLength());
        assertNull(wave.findChildOf(WAVE.data.class).getWave());
    }

    //----

    /** */
    public static void main(String[] args) throws Exception {
        InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(args[0])));
        WAVE wave = WAVE.readFrom(is, WAVE.class);

//      WAVE wave = new WAVE();

//      fmt header = wave.new fmt();
//      header.setFormatId(0x0001);
//      header.setNumberChannels(1);
//      header.setSamplingRate(8000);
//      header.setBytesPerSecond(2 * 8000);
//      header.setBlockSize(2 * 8000);
//      header.setSamplingBits(16);

        OutputStream os = new BufferedOutputStream(Files.newOutputStream(Paths.get(args[1])));
        wave.writeTo(os);
    }
}
