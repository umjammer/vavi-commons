/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * StringUtilTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class StringUtilTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    //----

    /** */
    public static void main(String[] args) throws IOException {
/*
        System.err.println("---- getDump(String) ----");
        System.err.println(getDump("直佐秀野"));
        System.err.println("---- getDump(InputStream) ----");
        System.err.println(getDump(new FileInputStream("StringUtil.class")));
*/
        System.err.println("---- toBits(int,int) ----");
        System.err.println(StringUtil.toBits(0x842173c6, 32));
        System.err.println("---- toBits(int,int) ----");
        System.err.println(StringUtil.toBits(0x73c6, 16));
        System.err.println("---- toBits(int,int) ----");
        System.err.println(StringUtil.toBits(0xc6, 8));
    }
}

/* */
