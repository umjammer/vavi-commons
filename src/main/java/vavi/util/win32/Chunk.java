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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;

import vavi.io.LittleEndianDataInputStream;
import vavi.io.LittleEndianDataOutputStream;
import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * Chunk format.
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

    /** */
    protected Chunk() {
    }

    /** */
    private byte[] id;

    /** chunk length */
    private int length;

    /** */
    public void setId(byte[] id) {
        this.id = id;
    }

    /** */
    public void setLength(int length) {
        this.length = length;
    }

    /** */
    private byte[] data;

    /** */
    private Chunk parent;

    /** Gets the chunk name. */
    public String getName() {
        return new String(id, StandardCharsets.US_ASCII);
    }

    /** Gets the chunk name. */
    public int getLength() {
        return length;
    }

    protected Chunk getParent() {
        return parent;
    }

    protected void setParent(Chunk parent) {
        this.parent = parent;
    }

    /**
     * ストリームデータをシリアライズするメソッドです。
     * オブジェクト内のデータを取得するためにオーバーライドしてください。
     * @throws NullPointerException {@link #setData(InputStream)} を実装していない場合 {@link #data} が null
     */
    public InputStream getData() throws IOException {
        return new ByteArrayInputStream(data);
    }

    /**
     * ストリームデータをデシリアライズするメソッドです。
     * オブジェクト内のデータを埋めるためにオーバーライドしてください。
     * {@link Chunk} クラスの実装では {@link #data} は fill されません。
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
     */
    public static <T extends Chunk> T readFrom(InputStream is, Class<T> clazz)
        throws IOException {

        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] tmp = new byte[4];
        ledis.readFully(tmp);

        int length = ledis.readInt();
Debug.println(Level.FINER, StringUtil.getDump(tmp));

        T chunk = null;

        try {
            if (chunkClasses == null) {
                chunkClasses = getChunkClasses(clazz);
//chunkClasses.forEach(Debug::println);
            }
            chunk = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        chunk.setId(tmp);
        chunk.setLength(length);
        chunk.setData(is);
        ledis.skipBytes(length % 2); // padding
Debug.print(Level.FINEST, chunk);
        return chunk;
    }

    protected static Chunk readFrom(InputStream is, Chunk parent)
        throws IOException {

        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] tmp = new byte[4];
        ledis.readFully(tmp);
        String name = new String(tmp, StandardCharsets.US_ASCII);

        int length = ledis.readInt();
Debug.println(Level.FINER, StringUtil.getDump(tmp) + ", " + length);

        Chunk chunk = null;

        try {
            Class<? extends Chunk> chunkClass = getClassOf(name);
            chunk = chunkClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchElementException e) {
Debug.println(Level.FINER, e);
            chunk = new Chunk();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        chunk.id = tmp;
        chunk.length = length;
        chunk.setParent(parent);
        chunk.setData(is);
        ledis.skipBytes(length % 2); // padding
Debug.print(Level.FINEST, chunk);
        return chunk;
    }

    public void writeTo(OutputStream os) throws IOException {
        LittleEndianDataOutputStream ledos = new LittleEndianDataOutputStream(os);
        ledos.write(id);
        ledos.writeInt(length);
        ledos.write(data);
    }

    protected static List<Class<? extends Chunk>> chunkClasses; 

    /** */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static List<Class<? extends Chunk>> getChunkClasses(Class<? extends Chunk> clazz) {
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

    /** */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static List<Class<? extends Chunk>> getChildChunkClasses(Class<? extends Chunk> clazz) {
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

    /** */
    protected static String normalize(String name) {
        String trimmed = name.trim();
        return trimmed.matches("^[^\\p{Alpha}].*$") ? "_" + trimmed : trimmed;
    }

    /** */
    protected static Class<? extends Chunk> getClassOf(String name) {
        Optional<Class<? extends Chunk>> o = chunkClasses.stream().filter(c -> normalize(name).equals(c.getSimpleName())).findFirst();
        if (o.isPresent()) {
            return o.get();
        } else {
            throw new NoSuchElementException("value for " + name);
        }
    }
}
