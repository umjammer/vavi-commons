/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * LinkFileTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class LinkFileTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    /** */
    public static void main(String[] args) throws IOException {
        LinkFile lf = LinkFile.readFrom(new FileInputStream(args[0]));
        System.err.println(lf.getPath());
    }
}

/* */
