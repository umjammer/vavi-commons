/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;


/**
 * Seekable.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/11 umjammer initial version <br>
 */
public interface Seekable {

    /** @see java.nio.channels.SeekableByteChannel */
    void position(long newPosition) throws IOException;

    /** @see java.nio.channels.ReadableByteChannel */
    long position() throws IOException;
}

/* */
