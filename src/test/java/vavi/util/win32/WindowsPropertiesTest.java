/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * WindowsPropertiesTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
@Disabled
public class WindowsPropertiesTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    /**
     * Tests this class.
     */
    public static void main(String[] args) throws IOException {
        WindowsProperties ini = new WindowsProperties();
        ini.load(new FileInputStream(args[0]));
        ini.list(System.out);
        ini.removePropertiesOfSection(args[2]);
        ini.store(new FileOutputStream(args[1]), args[1]);
    }
}

/* */
