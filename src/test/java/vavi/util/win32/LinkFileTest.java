/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * LinkFileTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class LinkFileTest {

    @Test
    public void test() throws Exception {
        LinkFile lf = LinkFile.readFrom(LinkFileTest.class.getResourceAsStream("/test.lnk"));
        assertEquals("C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE", lf.getPath());
    }

    //----

    /** */
    public static void main(String[] args) throws IOException {
        LinkFile lf = LinkFile.readFrom(Files.newInputStream(Paths.get(args[0])));
        System.err.println(lf.getPath());
    }
}
