/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * An incremental data source that writes data to an OutputStream.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 */
public interface InputEngine {
    /** */
    void initialize(InputStream in) throws IOException;

    /**
     * must read until <code>in</code> returns -1
     */
    void execute() throws IOException;

    /** */
    void finish() throws IOException;
}
