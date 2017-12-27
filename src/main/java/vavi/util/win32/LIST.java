/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;


/**
 * LIST format.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030122 nsano initial version <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public abstract class LIST extends MultipartChunk {
    /** */
    protected LIST() {
    }

    /** for debug */
    protected void printData() {
        System.err.println("---- list (" + getMultipartName() + ") ----");
    }
}

/* */
