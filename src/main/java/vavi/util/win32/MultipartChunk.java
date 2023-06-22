/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import vavi.io.LittleEndianDataInputStream;
import vavi.util.Debug;
import vavi.util.StringUtil;


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
    private byte[] multipartId;

    /** */
    private List<Chunk> chunks = new ArrayList<>();

    /** Gets the multipart chunk name. */
    public String getMultipartName() {
        return new String(multipartId, StandardCharsets.US_ASCII);
    }

    /** Gets chunks. */
    protected List<Chunk> getChunks() {
        return chunks;
    }

    @Override
    public void setData(InputStream is) throws IOException {
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] tmp = new byte[4];
        ledis.readFully(tmp);
Debug.println(Level.FINER, "multipart: " + StringUtil.getDump(tmp));

        multipartId = tmp;
//Debug.println(Level.FINEST, this);
        setChildrenData(ledis);
    }

    /** */
    protected void setChildrenData(LittleEndianDataInputStream is) throws IOException {
        int l = getLength() - 4; // - len(length)
        while (l > 8) {
            Chunk chunk = Chunk.readFrom(is, this);
            chunks.add(chunk);
            l -= chunk.getLength() + (chunk.getLength() % 2) + 4 + 4; // + padding + len(name) + len(length)
Debug.println(Level.FINER, getName() + "." + chunk.getName() + ", " + l + "/" + (getLength() - 4));
        }
if (l != 0) {
 Debug.println(Level.WARNING, getName() + ", " + l + " bytes left");
}
        is.skipBytes(l);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + "(" + getMultipartName() + ")\n");
        chunks.stream().map(c -> " " + c + "\n").forEach(sb::append);
        return sb.toString();
    }

    /** */
    @SuppressWarnings("unchecked")
    public <T extends Chunk> T findChildOf(Class<T> clazz) {
        for (Chunk chunk : chunks) {
            if (clazz.isInstance(chunk)) {
                return (T) chunk;
            }
        }
        throw new NoSuchElementException(clazz.getName());
    }

    /** */
    @SuppressWarnings("unchecked")
    public <T extends Chunk> List<T> findChildrenOf(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Chunk chunk : chunks) {
            if (clazz.isInstance(chunk)) {
                result.add((T) chunk);
            }
        }
        return result;
    }
}

/* */
