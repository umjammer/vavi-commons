/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package vavi.io;

import java.io.IOException;
import java.io.OutputStream;


/**
 * An incremental data source that writes data to an OutputStream.
 *
 * @author Copyright (c) 2002 Merlin Hughes <merlin@merlin.org>
 */
public interface OutputEngine {
    /** */
    void initialize(OutputStream out) throws IOException;

    /** */
    void execute() throws IOException;

    /** */
    void finish() throws IOException;
}

/* */
