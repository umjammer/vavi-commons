/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import org.junit.jupiter.api.Test;


/**
 * DebugTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/05/19 umjammer initial version <br>
 */
class DebugTest {

    @Test
    void test() {
        Debug.printf("%s\n", "Not yet implemented");
        Debug.printf("%d\n", 1);
        Debug.printf("%d, %08x\n", 100, 257);
    }
}
