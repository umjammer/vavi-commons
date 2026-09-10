/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * AdvancedBinderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-10 nsano initial version <br>
 */
class AdvancedBinderTest {

    enum Enum1 {
        enumA,
        enumB;
    }

    static class Test1 {
        File file1;
        Path path2;
        InputStream is3;
        Enum1 enum4;
    }

    @Test
    void test1() throws Exception {
        AdvancedBinder binder = new AdvancedBinder();
        String fileName = "src/test/resources/logging.properties";
        Test1 bean = new Test1();

        Field field1 = Test1.class.getDeclaredField("file1");
        binder.bind(bean, field1, File.class, fileName, null);
        assertEquals(fileName, bean.file1.toString());

        Field field2 = Test1.class.getDeclaredField("path2");
        binder.bind(bean, field2, Path.class, fileName, null);
        assertEquals(fileName, bean.path2.toString());

        Field field3 = Test1.class.getDeclaredField("is3");
        binder.bind(bean, field3, InputStream.class, "/logging.properties", null);
        assertNotNull(bean.is3);

        Field field4 = Test1.class.getDeclaredField("enum4");
        binder.bind(bean, field4, Enum1.class, "enumB", null);
        assertEquals(Enum1.enumB, bean.enum4);
    }
}
