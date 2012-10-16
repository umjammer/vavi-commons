/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * WAVETest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class WAVETest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    /** */
    public static void main(String[] args) throws Exception {
        InputStream is = new BufferedInputStream(new FileInputStream(args[0]));
        WAVE wave = (WAVE) WAVE.readFrom(is);

//      WAVE wave = new WAVE();

//      fmt header = wave.new fmt();
//      header.setFormatId(0x0001);
//      header.setNumberChannels(1);
//      header.setSamplingRate(8000);
//      header.setBytesPerSecond(2 * 8000);
//      header.setBlockSize(2 * 8000);
//      header.setSamplingBits(16);

        OutputStream os = new BufferedOutputStream(new FileOutputStream(args[1]));
        wave.writeTo(os);
    }
}

/* */
