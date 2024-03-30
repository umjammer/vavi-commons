/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import vavi.io.InputEngineOutputStream;
import vavi.io.OutputEngineInputStream;
import vavi.util.StringUtil;

import vavix.util.Rot13;
import vavix.util.Rot13.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test2.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/05/16 umjammer initial version <br>
 */
class Test2 {

    @Test
    void test02() throws Exception {

        final String data = "Naohide Sano 1970";

        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());

        // encode
        DataInputStream is = new DataInputStream(new OutputEngineInputStream(new IOStreamOutputEngine(bais, OutputStream::new)));

        byte[] result = new byte[data.length()];
        is.readFully(result);
        is.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // decode
        DataOutputStream os = new DataOutputStream(new InputEngineOutputStream(new IOStreamInputEngine(baos, Rot13.InputStream::new)));

        os.write(result);
        os.close();

        assertEquals(data, baos.toString());
    }

    @Test
    void test03() throws Exception {

        final String data = "あいうえお漢字";

        StringReader reader = new StringReader(data);

        // encode
        InputStream is = new OutputEngineInputStream(new ReaderWriterOutputEngine(reader, "Windows-31J"));

        byte[] result = new byte[256];
        int length = 0;
        while (true) {
            int r = is.read();
            if (r < 0) {
                break;
            }
            result[length++] = (byte) r;
        }
        is.close();
System.err.println(StringUtil.getDump(result, 0, length));
        StringWriter writer = new StringWriter();

        // decode
        DataOutputStream os = new DataOutputStream(new InputEngineOutputStream(new ReaderWriterInputEngine(writer, "Windows-31J")));

        os.write(result, 0, length);
        os.flush();
        os.close();

System.err.println(StringUtil.getDump(writer.getBuffer().toString().getBytes()));
        assertEquals(data, writer.getBuffer().toString());
    }
}
