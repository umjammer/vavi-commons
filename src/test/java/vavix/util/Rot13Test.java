/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Rot13Test.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 060203 nsano initial version <br>
 */
public class Rot13Test {

    /** */
    @Test
    public void test1() throws Exception {
        String string = "Naohide Sano 1970";
        StringBuilder sb = new StringBuilder();
        for (char c : string.toCharArray()) {
            sb.append((char) Rot13.codec(c));
        }
Debug.println(sb.toString());
        string = sb.toString();
        sb = new StringBuilder();
        for (char c : string.toCharArray()) {
            sb.append((char) Rot13.codec(c));
        }
Debug.println(sb.toString());
        assertEquals("Naohide Sano 1970", sb.toString());
    }

    /** */
    @Test
    public void test2() throws Exception {
        String string = "Naohide Sano 1970";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Rot13.OutputStream ros = new Rot13.OutputStream(baos);
        ros.write(string.getBytes());
Debug.println(baos.toString());
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Rot13.InputStream ris = new Rot13.InputStream(bais);
        byte[] b = new byte[256];
        int r = ris.read(b, 0, b.length);
        ris.close();
        ros.close();
Debug.println(new String(b, 0, r));
        assertEquals("Naohide Sano 1970", new String(b, 0, r));
    }
}
