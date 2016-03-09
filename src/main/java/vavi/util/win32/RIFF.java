/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.io.InputStream;

import vavi.io.LittleEndianDataInputStream;


/**
 * Resource Interchange File Format.
 *
 * <pre>
 * off len dsc
 * 00  04  "RIFF"
 * 04  04  chunk length (*1)
 * 08  04  chunk type
 * 0c (*1) chunk data ...
 * </pre>
 * 
 * @target	1.1
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020424 nsano initial version <br>
 *          0.10 020507 nsano repackage <br>
 *          0.11 020507 nsano refine <br>
 *          0.10 020707 nsano refine <br>
 *          1.00 030121 nsano refactoring <br>
 *          1.01 030606 nsano show error in #readFrom <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public abstract class RIFF extends MultipartChunk {

    /** */
    protected RIFF() {
    }

    /** */
    public void setData(InputStream is) throws IOException {
        setChildrenData(is);
    }

    /**
     * Creates a Chunk object from a stream.
     */
    public static Chunk readFrom(InputStream is)
        throws IOException {

        String name = new String();
        name += (char) is.read();
        name += (char) is.read();
        name += (char) is.read();
        name += (char) is.read();

        @SuppressWarnings("resource")
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);
        long length = ledis.readInt() & 0xffffffffL;
System.err.println("RIFF::readFrom: length: " + length);

        String multipartName = new String();
        multipartName += (char) is.read();
        multipartName += (char) is.read();
        multipartName += (char) is.read();
        multipartName += (char) is.read();

        MultipartChunk chunk = null;

        try {
            String className = getClassName(multipartName, null);
            chunk = (MultipartChunk) Class.forName(className).newInstance();
        } catch (Exception e) {
System.err.println("RIFF::readFrom: multipartName: " + multipartName);
            throw new IllegalStateException(e.toString());
        }

        chunk.setName(name);
        chunk.setLength(length);
        chunk.setMultipartName(multipartName);
chunk.print();
        chunk.setData(is);

        return chunk;
    }

    /** for debug */
    protected void printData() {
        System.err.println("サブクラスに任せた");
    }
}

/* */
