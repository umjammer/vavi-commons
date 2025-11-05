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
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
@PropsEntity(url = "file:local.properties")
class WAVETest {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property
    String wav = "src/test/resources/test.wav";

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    @Test
    void test() throws Exception {
        InputStream is = new BufferedInputStream(WAVETest.class.getResourceAsStream("/test.wav"));
        WAVE wave = WAVE.readFrom(is, WAVE.class);
        assertEquals(176470, wave.getLength());
        assertNotNull(wave.findChildOf(WAVE.data.class).getWave());
    }

    @Test
    void test2() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("vavi.util.win32.WAVE.data.notLoadData", true);
        InputStream is = new BufferedInputStream(WAVETest.class.getResourceAsStream("/test.wav"));
        WAVE wave = WAVE.readFrom(is, WAVE.class, context);
        assertEquals(176470, wave.getLength());
        assertNull(wave.findChildOf(WAVE.data.class).getWave());
    }

    @Test
    @Disabled("source is wrong")
    void test0() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put(WAVE.CHUNK_PARSE_STRICT_KEY, false);
        context.put(WAVE.MULTIPART_CHUNK_PARSE_STRICT_KEY, false);
        context.put(WAVE.WAVE_DATA_NOT_LOAD_KEY, true);
        WAVE wave = WAVE.readFrom(new BufferedInputStream(Files.newInputStream(Paths.get(wav))), WAVE.class, context);
Debug.print("formatId: " + wave.getHeader().getFormatId());
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
