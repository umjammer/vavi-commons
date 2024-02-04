/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Test
    void test2() {
        byte[] dump = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        System.err.println(StringUtil.getDump(dump));
        System.err.println(StringUtil.getDump(dump, 16));
        System.err.println(StringUtil.getDump(dump, 4));
        System.err.println(StringUtil.getDump(dump, 4, 4));
        System.err.println(StringUtil.getDump(dump, 0));
        String expexted1 = "00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F    ................\n" +
                           "10 11                                              ..";
        assertEquals(expexted1, StringUtil.getDump(dump));
        String expexted2 = "00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F    ................";
        assertEquals(expexted2, StringUtil.getDump(dump, 16));
        String expexted3 = "00 01 02 03                                        ....";
        assertEquals(expexted3, StringUtil.getDump(dump, 4));
        String expexted4 = "04 05 06 07                                        ....";
        assertEquals(expexted4, StringUtil.getDump(dump, 4, 4));
        String expexted5 = "";
        assertEquals(expexted5, StringUtil.getDump(dump, 0));
    }

    @Test
    void test3() throws IOException {
        byte[] bytes = new byte[256];
        for (int i = 0; i < 256; i++) {
            bytes[i] = (byte) i;
        }
        Path p = Paths.get("tmp/b.dat");
        Files.write(p, bytes);
        Debug.println("\n" + StringUtil.getDump(Files.newInputStream(p), 0, 128));
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
