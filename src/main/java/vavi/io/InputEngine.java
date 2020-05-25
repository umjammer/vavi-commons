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
     * in が -1 を返すまで読み込む必要がある
     */
    void execute() throws IOException;

    /** */
    void finish() throws IOException;
}

/* */
