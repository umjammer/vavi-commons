/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import vavix.util.Rot13;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/05/16 umjammer initial version <br>
 */
class Test1 {

    @Test
    void test01() throws Exception {

        final String data = "Naohide Sano 1970";

        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());

        // encode
        DataInputStream is = new DataInputStream(new OutputEngineInputStream(new OutputEngine() {
            OutputStream out;

            @Override
            public void initialize(OutputStream out) throws IOException {
                this.out = new Rot13.OutputStream(out);
            }

            byte[] buf = new byte[8192];

            @Override
            public void execute() throws IOException {
                int r = bais.read(buf);
                out.write(buf, 0, r);
            }

            @Override
            public void finish() throws IOException {
            }
        }));

        byte[] result = new byte[data.length()];
        is.readFully(result);
        is.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // decode
        DataOutputStream os = new DataOutputStream(new InputEngineOutputStream(new InputEngine() {
            InputStream in;

            public void initialize(InputStream in) throws IOException {
                this.in = new Rot13.InputStream(in);
            }

            byte[] buf = new byte[8192];

            public void execute() throws IOException {
                int r = in.read(buf);
                baos.write(buf, 0, r);
            }

            public void finish() throws IOException {
            }
        }));

        os.write(result);
        os.close();

        assertEquals(data, baos.toString());
    }
}
