/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.DataOutput;


/**
 * SeekableDataOutput.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/10/29 umjammer initial version <br>
 */
public interface SeekableDataOutput<T> extends Seekable, DataOutput {

    T origin();
}

/* */
