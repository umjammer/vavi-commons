/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import vavi.io.LittleEndianDataInputStream;
import vavi.util.StringUtil;

import static java.lang.System.getLogger;


/**
 * MultipartChunk format.
 * <p>
 * system properties
 * <ul>
 *  <li>vavi.util.win32.MultipartChunk.strict ... not allowed broken structure, default false</li>
 * </ul>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030121 nsano initial version <br>
 *          0.10 030711 nsano add setChildData() <br>
 *          0.11 030711 nsano deprecate setChildData() <br>
 *          0.12 030716 nsano add findChildOf() <br>
 */
public abstract class MultipartChunk extends Chunk {

    private static final Logger logger = getLogger(MultipartChunk.class.getName());

    /** to set true makes this class not allowed broken structure */
    public static final String MULTIPART_CHUNK_PARSE_STRICT_KEY = "vavi.util.win32.MultipartChunk.strict";

    protected static boolean isStrictMultipart() {
        return (boolean) Objects.requireNonNullElse(context.get().get(MULTIPART_CHUNK_PARSE_STRICT_KEY), false);
    }

    /** foreCC for this multipart chunk */
    protected byte[] multipartId;

    /** child chunks */
    private final List<Chunk> chunks = new ArrayList<>();

    /** Gets the multipart chunk name. */
    public String getMultipartName() {
        return new String(multipartId, StandardCharsets.US_ASCII);
    }

    /** Gets chunks. */
    protected List<Chunk> getChunks() {
        return chunks;
    }

    /**
     * @throws IllegalArgumentException input is not wav or there is undefined chunk
     */
    @Override
    public void setData(InputStream is) throws IOException {
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] tmp = new byte[4];
        ledis.readFully(tmp);
logger.log(Level.TRACE, "multipart: " + StringUtil.getDump(tmp));

        multipartId = tmp;
//logger.log(Level.TRACE, this);
        setChildrenData(ledis);
    }

    /**
     * Creates children from a stream.
     * @throws IllegalArgumentException input is not wav or there is undefined chunk
     */
    protected void setChildrenData(LittleEndianDataInputStream is) throws IOException {
        int l = getLength() - 4; // - len(length)
        while (l > 8) {
            Chunk chunk = Chunk.readFrom(is, this);
            chunks.add(chunk);
logger.log(Level.TRACE, "add child chunk: " + chunk);
            l -= chunk.getLength() + (chunk.getLength() % 2) + 4 + 4; // + padding + len(name) + len(length)
logger.log(Level.TRACE, getName() + "." + chunk.getName() + ", " + l + "/" + (getLength() - 4));
            if (!(boolean) context.get().get(Chunk.CHUNK_PARSING_KEY)) {
logger.log(Level.TRACE, "children chunk parsing canceled: " + getClass().getSimpleName());
                break;
            }
        }

        if (isStrictMultipart()) {
            if (l < 0) {
                throw new IllegalArgumentException("it seems the input is not wav");
            }
        }
if (l != 0) {
 logger.log(Level.WARNING, getName() + ", " + l + " bytes left");
}
        is.skipBytes(l);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("(").append(getMultipartName()).append(")\n");
        chunks.stream().map(c -> " " + c + "\n").forEach(sb::append);
        return sb.toString();
    }

    /** Find a child chunk by a class */
    @SuppressWarnings("unchecked")
    public <T extends Chunk> T findChildOf(Class<T> clazz) {
        for (Chunk chunk : chunks) {
            if (clazz.isInstance(chunk)) {
                return (T) chunk;
            }
        }
        throw new NoSuchElementException(clazz.getName());
    }

    /** Find child chunks by a class */
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
