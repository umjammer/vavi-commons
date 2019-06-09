/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * MultipartChunk format.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030121 nsano initial version <br>
 *          0.10 030711 nsano add setChildData() <br>
 *          0.11 030711 nsano deprecate setChildData() <br>
 *          0.12 030716 nsano add findChildOf() <br>
 */
public abstract class MultipartChunk extends Chunk {

    /** */
    private String multipartName;
    /** */
    protected Vector<Chunk> chunks = new Vector<>();

    /** */
    protected MultipartChunk() {
    }

    /** Gets the multipart chunk name. */
    public String getMultipartName() {
        return multipartName;
    }

    /** Sets the multipart chunk name. */
    public void setMultipartName(String multipartName) {
        this.multipartName = multipartName;
    }

    /** Gets chunks. */
    public Vector<Chunk> getChunks() {
        return chunks;
    }

    /** */
    public void setData(InputStream is) throws IOException {
        String name = "";
        name += (char) is.read();
        name += (char) is.read();
        name += (char) is.read();
        name += (char) is.read();

        setMultipartName(name);
Debug.println(Level.FINEST, this);
        setChildrenData(is);
    }

    /** */
    protected void setChildrenData(InputStream is) throws IOException {
        long l = getLength() - 4;
        while (l > 8) {
            Chunk chunk = Chunk.readFrom(is, this);
            chunks.addElement(chunk);
            l -= chunk.getLength() + 8;
//System.err.println("MultipartChunk::setChildrenData: " + l + "/" + (getLength() - 4));
        }
//System.err.println("MultipartChunk::setChildrenData: " + l + " bytes left");
        skip(is, l);
    }

    /* */
    public String toString() {
        return "multipartName: " + multipartName + ": " + this;
    }

    /** */
    public Chunk findChildOf(Class<?> clazz) {
        for (int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.elementAt(i);
            if (clazz.isInstance(chunk)) {
                return chunk;
            }
        }
        throw new NoSuchElementException(clazz.getName());
    }
}

/* */
