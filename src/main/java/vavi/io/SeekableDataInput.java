/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataInput;
import java.nio.channels.SeekableByteChannel;


/**
 * SeekableDataInput.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/11 umjammer initial version <br>
 */
public interface SeekableDataInput<T> extends Seekable, DataInput {

    T origin();
}

/* */
