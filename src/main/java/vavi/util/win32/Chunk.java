/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;

import vavi.io.LittleEndianDataInputStream;
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
public abstract class Chunk {
    /** */
    private String name;
    /** chunk length */
    private long length;
    /** */
    private byte[] data;

    /** */
    protected Chunk() {
    }

    /** Gets the chunk name. */
    public String getName() {
        return name;
    }

    /** Gets the chunk name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Gets the chunk name. */
    public long getLength() {
        return length;
    }

    /** Gets the chunk name. */
    public void setLength(long length) {
        this.length = length;
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

        skip(is, length);

//      data = new byte[length];

//      int l = 0;
//      while (l < length) {
//          l += is.read(data, l, length - l);
//      }
    }

    /** Special skip */
    protected static void skip(InputStream is, long n) throws IOException {
        long l = 0;
        while (l < n) {
            long r = is.skip(n - l);
            if (r < 0) {
                throw new EOFException();
            }
            l += r;
        }
    }

    /** */
    public String toString() {
        return StringUtil.paramStringDeep(this, 1);
    }

    /**
     * Creates a Chunk object from a stream.
     */
    public static Chunk readFrom(InputStream is)
        throws IOException {

        return readFrom(is, null);
    }

    /**
     * Creates a Chunk object from a stream.
     */
    protected static Chunk readFrom(InputStream is, Chunk parent)
        throws IOException {

        @SuppressWarnings("resource")
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        byte[] tmp = new byte[4];
        ledis.readFully(tmp);
        String name = new String(tmp, Charset.forName("ASCII"));
//Debug.println("name: " + name);

        long length = ledis.readInt() & 0xffffffffL;

        Chunk chunk = null;
        String className = null;

        try {
            className = getClassName(name, parent);
            @SuppressWarnings("unchecked")
            Class<? extends Chunk> clazz = (Class<? extends Chunk>) Class.forName(className);

            if (!Modifier.isStatic(clazz.getModifiers())) {
                // inner class
                @SuppressWarnings("unchecked")
                Class<? extends Chunk> outerClass = (Class<? extends Chunk>) Class.forName(getOuterClassName(className));
                Constructor<? extends Chunk> c = clazz.getConstructor(outerClass);
//Debug.println("parent: " + StringUtil.getClassName(parent.getClass()));
                chunk = c.newInstance(parent);
            } else {
                chunk = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (ClassNotFoundException e) {
Debug.println(Level.WARNING, "no such class for " + StringUtil.getClassName(parent.getClass()) + "." + name + ": " + className);
            throw new IllegalStateException(e);
        } catch (NoSuchElementException e) {
Debug.println(Level.WARNING, "no key: " + StringUtil.getClassName(parent.getClass()) + "." + name);
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        chunk.name = name;
        chunk.length = length;
        chunk.setData(is);
Debug.print(Level.FINEST, chunk);
        return chunk;
    }

    /** */
    protected static String getOuterClassName(String name) {
        int p = name.lastIndexOf('$');    // TODO $ は実装依存???
        if (p == -1) {
            throw new IllegalStateException("not inner class: " + name);
        }
//Debug.println(name + ": " + name.substring(0, p));
        return name.substring(0, p);
    }

    /** */
    protected static String getClassName(String name, Chunk parent) {
        String key = name.trim();
        if (parent != null) {
            String prefix = parent.getClass().getName();
            prefix = prefix.substring(prefix.lastIndexOf('.') + 1);
//Debug.println("prefix: " + prefix);

            key = prefix + "." + key;
        }
        String className = null;
try {
        className = props.getProperty(key);
} catch (NullPointerException e) {
 Debug.println("no key for: " + name + ", " + key);
}
//Debug.println(name + ", " + key + ", " + className);
        if (className == null) {
            throw new NoSuchElementException("value for " + key);
        }

        return className;
    }

    /** */
    private static Properties props = new Properties();

    /** */
    static {
        try {
            props.load(Chunk.class.getResourceAsStream("Chunk.properties"));
        } catch (IOException e) {
Debug.println(e);
        }
    }
}

/* */
