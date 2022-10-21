/*
 * http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
 */

package vavix.io;

import java.io.InputStream;


/**
 * ByteArrayInputStream implementation that does not synchronize methods.
 */
public class FastByteArrayInputStream extends InputStream {
    /**
     * Our byte buffer
     */
    protected byte[] buf = null;

    /**
     * Number of bytes that we can read from the buffer
     */
    protected int count = 0;

    /**
     * Number of bytes that have been read from the buffer
     */
    protected int pos = 0;

    /** */
    public FastByteArrayInputStream(byte[] b) {
        this(b, 0, b.length);
    }

    /** */
    public FastByteArrayInputStream(byte[] b, int off, int len) {
        this.buf = b;
        this.pos = off;
        this.count = off + len;
    }

    /* */
    public final int available() {
        return count - pos;
    }

    /* */
    public final int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    /* */
    public final int read(byte[] b, int off, int len) {
        if (pos >= count)
            return -1;

        if ((pos + len) > count)
            len = (count - pos);

        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    /* */
    public final long skip(long n) {
        if ((pos + n) > count)
            n = count - pos;
        if (n < 0)
            return 0;
        pos += (int) n;
        return n;
    }
}

/* */
