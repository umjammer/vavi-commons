/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * AVITest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
@Disabled
public class AVITest {

    @Test
    public void test() throws Exception {
        InputStream is = new BufferedInputStream(AVITest.class.getResourceAsStream("/test.avi"));
        AVI avi = (AVI) AVI.readFrom(is);
        System.err.println("AVI: " + avi);
    }

    //----

    /** */
    public static void main(String[] args) throws Exception {
        InputStream is = new BufferedInputStream(new FileInputStream(args[0]));
        AVI avi = (AVI) AVI.readFrom(is);
        System.err.println("AVI: " + avi);
    }
}

/* */
