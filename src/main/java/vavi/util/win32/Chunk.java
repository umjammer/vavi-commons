/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;

import vavi.io.LittleEndianDataInputStream;
import vavi.io.LittleEndianDataOutputStream;
import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * Chunk format.
 * <p>
 * system properties
 * <ul>
 *  <li>vavi.util.win32.Chunk.strict ... allows undefined chunk in a chunk class, default false</li>
 * </ul>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020507 nsano initial version <br>
 *          0.10 020707 nsano refine <br>
 *          1.00 030121 nsano refactoring <br>
 *          1.01 030122 nsano add class prefix related <br>
 *          1.02 030122 nsano add toString <br>
 *          1.03 030606 nsano change error trap in #readFrom <br>
 *          1.03 030711 nsano change #readFrom <br>
 */
public class Chunk {

    /** to set true makes this class allows undefined chunk in a chunk class */
    public static final String CHUNK_PARSE_STRICT_KEY = "vavi.util.win32.Chunk.strict";

    /** for cancel parsing */
    protected static final String CHUNK_PARSING_KEY = "vavi.util.win32.Chunk.parse";

    /** key name base for user defined chunk class */
    public static final String CHUNK_SUB_CHUNK_KEY_BASE = "vavi.util.win32.Chunk.class.";

    /** utility for {@link #CHUNK_PARSE_STRICT_KEY} */
    protected static boolean isStrict() {
        return (boolean) Objects.requireNonNullElse(context.get().get(CHUNK_PARSE_STRICT_KEY), false);
    }

    /** not opened */
    protected Chunk() {
    }

    /** foreCC of this chunk */
    private byte[] id;

    /** chunk length */
    private int length;

    /** Sets chunk foreCC */
    public void setId(byte[] id) {
        this.id = id;
    }

    /** Sets chunk length */
    public void setLength(int length) {
        this.length = length;
    }

    /** chunk data */
    private byte[] data;

    /** a chunk which this chunk belongs to */
    private Chunk parent;

    /** Gets the chunk name. */
    public String getName() {
        return new String(id, StandardCharsets.US_ASCII);
    }

    /** Gets the chunk name. */
    public int getLength() {
        return length;
    }

    /** Gets a chunk which this chunk belongs to */
    protected Chunk getParent() {
        return parent;
    }

    /** Sets a chunk which this chunk belongs to */
    protected void setParent(Chunk parent) {
        this.parent = parent;
    }

    /** represents parser is working or not */
    protected static ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    /**
     * A method to serialize stream data.
     * Override to retrieve data within the object.
     * @throws NullPointerException {@link #data} is null if you do not implement {@link #setData(InputStream)}
     */
    public InputStream getData() throws IOException {
        return new ByteArrayInputStream(data);
    }

    /**
     * This method deserializes stream data.
     * Override to fill data in the object.
     * {@link Chunk} class implementation does not fill {@link #data}.
     */
    public void setData(InputStream is) throws IOException {
Debug.println(Level.FINER, getName() + ": " + length + " / " + is.available());
          data = new byte[length];

          LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);
          ledis.readFully(data);
    }

    @Override
    public String toString() {
        return getName() + ": " + getLength();
    }

    /**
     * Creates a Chunk object from a stream.
     * @throws IllegalStateException sub chunk instantiation related
     * @throws IllegalArgumentException input is not wav or there is undefined chunk
     */
    public static <T extends Chunk> T readFrom(InputStream is, Class<T> clazz) throws IOException {
        return Chunk.readFrom(is, clazz, new HashMap<>());
    }

    /** at beginning */
    private static void initContext(Class<? extends Chunk> clazz, Map<String, Object> context) {
        Chunk.context.set(context);

        context.put(CHUNK_PARSING_KEY, true);

        getChunkClasses(clazz).forEach(chunkClass -> {
            String key = CHUNK_SUB_CHUNK_KEY_BASE + chunkClass.getSimpleName();
            if (!context.containsKey(key)) {
                context.put(key, chunkClass);
            }
Debug.println(Level.FINEST, "predefined class: " + key + ", " + chunkClass);
        });
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Chunk> getChunkClass(Class<? extends Chunk> clazz) {
        return  (Class<? extends Chunk>) context.get().get(CHUNK_SUB_CHUNK_KEY_BASE + clazz.getSimpleName());
    }

    /**
     * Creates a Chunk object from a stream.
     * @param context user settings
     * @throws IllegalStateException sub chunk instantiation related
     * @throws IllegalArgumentException input is not wav or there is undefined chunk
     * @throws NoSuchElementException no such a chunk definition
     */
    public static <T extends Chunk> T readFrom(InputStream is, Class<T> clazz, Map<String, Object> context) throws IOException {

        initContext(clazz, context);

        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] fourcc = new byte[4];
        ledis.readFully(fourcc);
Debug.println(Level.FINER, "start chunk: " + new String(fourcc, StandardCharsets.US_ASCII));

        int length = ledis.readInt();
        int p1 = is.available();
Debug.println(Level.FINER, StringUtil.getDump(fourcc));

        Class<? extends Chunk> chunkClass = getClassOf(clazz.getSimpleName());

        T chunk;
        try {
            chunk = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        chunk.setId(fourcc);
        chunk.setLength(length);
        chunk.setData(is);
        if ((boolean) context.get(CHUNK_PARSING_KEY)) {
            int p2 = is.available();
            if (p1 != length + p2) {
Debug.print(Level.FINEST, "correction: " + (length - (p1 - p2)) + ", 1: " + p1 + ", 2: " + p2 + ", l: " + length);
                ledis.skipBytes(length - (p1 - p2)); // correction
            }
            ledis.skipBytes(length % 2); // padding
Debug.print(Level.FINEST, chunk);
        } else {
Debug.print(Level.FINEST, "parse stop because strict is set");
        }
        return chunk;
    }

    /**
     * Create a chunk from <code>is</code>.
     * @throws IllegalArgumentException input is not wav or there is undefined chunk
     */
    protected static Chunk readFrom(InputStream is, Chunk parent) throws IOException {

        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] fourcc = new byte[4];
        ledis.readFully(fourcc);
        String name = new String(fourcc, StandardCharsets.US_ASCII);

        int length = ledis.readInt();
        int p1 = is.available();
Debug.println(Level.FINER, StringUtil.getDump(fourcc) + ", " + length);

        Chunk chunk = null;

        try {
            Class<? extends Chunk> chunkClass = getClassOf(name);
            chunk = chunkClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchElementException e) {
            if (isStrict()) {
Debug.print(Level.FINEST, "exception because strict is set");
                throw new IllegalArgumentException("undefined chunk: " + name);
            }
Debug.println(Level.FINER, e);
            chunk = new Chunk();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        chunk.id = fourcc;
        chunk.length = length;
        chunk.setParent(parent);
        try {
            chunk.setData(is);
            int p2 = is.available();
            if (p1 != length + p2) {
Debug.print(Level.FINEST, "correction: " + (length - (p1 - p2)) + ", 1: " + p1 + ", 2: " + p2 + ", l: " + length);
                ledis.skipBytes(length - (p1 - p2)); // correction
            }
            ledis.skipBytes(length % 2); // padding
        } catch (ChunkParseStopException e) {
Debug.print(Level.FINER, "chunk parsing canceled: " + chunk.getClass().getSimpleName());
            context.get().put(CHUNK_PARSING_KEY, false);
        }
Debug.print(Level.FINEST, chunk);
        return chunk;
    }

    /** Write this chunk into <code>os</code>. */
    public void writeTo(OutputStream os) throws IOException {
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(os);
        ledos.write(id);
        ledos.writeInt(length);
        ledos.write(data);
    }

    /** Gets all chunk classes defined in this chunk class and super classes. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<Class<? extends Chunk>> getChunkClasses(Class<? extends Chunk> clazz) {
        List<Class<? extends Chunk>> results = new ArrayList<>();
        while (clazz != null) {
            if (Chunk.class.isAssignableFrom(clazz)) {
                results.add(clazz);
            }
            results.addAll(getChildChunkClasses(clazz));
            clazz = (Class) clazz.getSuperclass();
        }
        return results;
    }

    /** Gets all chunk classes defined in this chunk class. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<Class<? extends Chunk>> getChildChunkClasses(Class<? extends Chunk> clazz) {
        List<Class<? extends Chunk>> results = new ArrayList<>();
        for (Class<?> childClass : clazz.getDeclaredClasses()) {
//System.err.println(childClass);
            if (Chunk.class.isAssignableFrom(childClass)) {
                results.add((Class) childClass);
                results.addAll(getChildChunkClasses((Class) childClass));
            }
        }
        return results;
    }

    /**
     * chunk class name rule
     * <ul>
     * <li>white spaces are trimmed</li>
     * <li>not alphabet characters are replaced to '_'</li>
     * </ul>
     */
    protected static String normalize(String name) {
        String trimmed = name.trim();
        return trimmed.matches("^[^\\p{Alpha}].*$") ? "_" + trimmed : trimmed;
    }

    /**
     * Get a chunk class defined in this chunk class specified by the name.
     * @throws NoSuchElementException no such a class of the name
     */
    @SuppressWarnings("unchecked")
    protected static Class<? extends Chunk> getClassOf(String name) {
        String key = CHUNK_SUB_CHUNK_KEY_BASE + normalize(name);
        Class<? extends Chunk> clazz = (Class<? extends Chunk>) context.get().get(key);
        if (clazz != null) {
            return clazz;
        } else {
Debug.println(Level.FINER, "no chunk class for: " + name);
            throw new NoSuchElementException("no chunk class for: " + name);
        }
    }

    /** Using to stop parsing. */
    static class ChunkParseStopException extends RuntimeException {}
}
