/*
 * Copyright (c) 2011 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import vavi.io.InputEngineOutputStream;
import vavi.io.OutputEngineInputStream;
import vavi.util.Debug;

import vavix.util.Checksum;
import vavix.util.Rot13;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2011/10/15 umjammer initial version <br>
 */
public class Test1 {

    @BeforeAll
    static void setup() throws IOException {
        if (!Files.exists(Paths.get("tmp"))) {
            Files.createDirectory(Paths.get("tmp"));
        }
    }

    /**
     * GZIP encode w/ FastByteArrayInputStream
     *
     * <pre>
     *
     * source -> -+                 +-> in
     *            |                 |
     *            +-> gzip output ->+
     *
     * </pre>
     */
    @Test
    public void test001() throws Exception {

        String s = "aHello Naohide Sano";
        byte[] bytes = s.getBytes();
        InputStream in = new FastByteArrayInputStream(bytes, 1, bytes.length - 1);
        InputStream is = new OutputEngineInputStream(new IOStreamOutputEngine(in, GZIPOutputStream::new));
        GZIPInputStream gis = new GZIPInputStream(is);
        int l = 0;
        byte[] b = new byte[100];
        while (true) {
            int r = gis.read(b);
            if (r < 0) {
                break;
            }
            l += r;
        }
        gis.close();
        String r = new String(b, 0, l);
Debug.println(r);
        assertEquals(s.substring(1), r);
    }

    static class GZIPInputStreamFactory implements IOStreamInputEngine.InputStreamFactory {
        @Override
        public InputStream getInputStream(InputStream in) throws IOException {
            // IOStreamInputEngine needs to read some bytes before initialize()
            // for reading header (*1)
Debug.println(in.getClass() + ", " + in.available());
            return new GZIPInputStream(in);
        }
    }

    /**
     * GZIP decode. w/ FastByteArrayOutputStream
     *
     * TODO incomplete (*1)
     * <pre>
     *
     *
     * </pre>
     */
    @Disabled
    @Test
    public void test002() throws Exception {
        String s = "Hello Naohide Sano";
        byte[] bytes = s.getBytes();
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        OutputStream os = new InputEngineOutputStream(new IOStreamInputEngine(baos, new GZIPInputStreamFactory()));
        os.write(bytes);
        os.flush();
        os.close();
Debug.println(baos.getByteArray().length);
        FastByteArrayInputStream bais = new FastByteArrayInputStream(baos.getByteArray());
        InputStream is = new GZIPInputStream(bais);
//        InputStream is = bais;
        int l = 0;
        byte[] b = new byte[100];
        while (true) {
            int r = is.read(b);
            if (r < 0) {
                break;
            }
            l += r;
        }
        is.close();
Debug.println(l);
        String r = new String(b, 0, l);
Debug.println(r);
        assertEquals(s, r);
    }

    /**
     * rot13 encode w/ AdvancedByteArrayInputStream
     */
    @Test
    public void test003() throws Exception {

        String s = "Hello Naohide Sano";
        byte[] bytes = s.getBytes();
        InputStream in = new AdvancedByteArrayInputStream(bytes);
        InputStream is = new OutputEngineInputStream(new IOStreamOutputEngine(in, Rot13.OutputStream::new));
        Rot13.InputStream ris = new Rot13.InputStream(is);
        int l = 0;
        byte[] b = new byte[100];
        while (true) {
            int r = ris.read(b);
            if (r < 0) {
                break;
            }
            l += r;
        }
        ris.close();
        String r = new String(b, 0, l);
System.err.println(r);
        assertEquals(s, r);
    }

    /**
     * rot13 decode w/ FastByteArrayOutputStream, FastByteArrayInputStream
     * <pre>
     *
     *
     * </pre>
     */
    @Test
    public void test004() throws Exception {

        String s = "Hello Naohide Sano";
        byte[] bytes = s.getBytes();
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        OutputStream os = new InputEngineOutputStream(new IOStreamInputEngine(baos, Rot13.InputStream::new));
        os.write(bytes);
        os.flush();
        os.close();
        FastByteArrayInputStream bais = new FastByteArrayInputStream(baos.getByteArray());
        InputStream is = new Rot13.InputStream(bais);
        int l = 0;
        byte[] b = new byte[100];
        while (true) {
            int r = is.read(b);
            if (r < 0) {
                break;
            }
            l += r;
        }
        is.close();
System.err.println(l);
        String r = new String(b, 0, l);
System.err.println(r);
        assertEquals(s, r);
    }

    /**
     * rot13 encode/decode
     * <pre>
     *
     *
     * </pre>
     */
    @Test
    public void test005() throws Exception {

        File inFile = new File("src/test/java/vavix/io/Test1.java");
Debug.println(inFile.length());
        InputStream fis = new BufferedInputStream(Files.newInputStream(inFile.toPath()));
        InputStream is = new OutputEngineInputStream(new IOStreamOutputEngine(fis, Rot13.OutputStream::new));
        File outFile = new File("tmp/out.txt");
        OutputStream fos = new BufferedOutputStream(Files.newOutputStream(outFile.toPath()));
        OutputStream os = new InputEngineOutputStream(new IOStreamInputEngine(fos, Rot13.InputStream::new));
        byte[] buf = new byte[8192];
        while (true) {
            int r = is.read(buf);
            if (r < 0) {
                break;
            }
            os.write(buf, 0, r);
        }
        os.flush();
        os.close();
        is.close();

Debug.println(inFile.length() + ", " + outFile.length());
        assertEquals(Checksum.getChecksum(inFile), Checksum.getChecksum(outFile));
    }

    /**
     * AdvancedPipedInputStream
     * <pre>
     *
     *
     * </pre>
     */
    @Test
    public void test006() throws Exception {

        File inFile = new File("src/test/java/vavix/io/Test1.java");
Debug.println(inFile.length());
        final InputStream in = new Rot13.InputStream(new BufferedInputStream(Files.newInputStream(inFile.toPath())));
        final File outFile = new File("tmp/out.txt");

        AdvancedPipedInputStream source = new AdvancedPipedInputStream();
        final AdvancedPipedInputStream.OutputStreamEx sink = source.getOutputStream();
        Thread thread = new Thread(() -> {
            try {
                OutputStream os = new Rot13.OutputStream(new BufferedOutputStream(Files.newOutputStream(outFile.toPath())));
                Streams.io(in, os);
                os.close();
            } catch (IOException e) {
                try {
                    sink.setException(e);
                } catch (IOException ignored) {
                }
            }
        });
        thread.start();
        thread.join();
        source.close();

Debug.println(inFile.length() + ", " + outFile.length());
        assertEquals(Checksum.getChecksum(inFile), Checksum.getChecksum(outFile));
    }
}
