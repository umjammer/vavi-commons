/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * StringUtilTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
class StringUtilTest {

    @Test
    void test() {
        assertEquals("76F4 4F50 79C0 91CE", StringUtil.getDump("直佐秀野"));
        assertEquals("*....*....*....*.***..****...**.", StringUtil.toBits(0x842173c6, 32));
        assertEquals(".***..****...**.", StringUtil.toBits(0x73c6, 16));
        assertEquals("**...**.", StringUtil.toBits(0xc6, 8));
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
