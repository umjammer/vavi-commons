/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * DefaultBinderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-10 nsano initial version <br>
 */
class DefaultBinderTest {

    static class Test1 {
        int int1;
        short short2;
        long long3;
        byte byte4;
        char char5;
        float float6;
        double double7;
        boolean boolean8;
        String string9;
    }

    @Test
    void test1() throws Exception {
        DefaultBinder binder = new DefaultBinder();
        Test1 bean = new Test1();

        Field field1 = Test1.class.getDeclaredField("int1");
        binder.bind(bean, field1, Integer.TYPE, "12345678", null);
        assertEquals(12345678, bean.int1);

        Field field2 = Test1.class.getDeclaredField("short2");
        binder.bind(bean, field2, Short.TYPE, "1234", null);
        assertEquals(1234, bean.short2);

        Field field3 = Test1.class.getDeclaredField("long3");
        binder.bind(bean, field3, Long.TYPE, "123456789123", null);
        assertEquals(123456789123L, bean.long3);

        Field field4 = Test1.class.getDeclaredField("byte4");
        binder.bind(bean, field4, Byte.TYPE, "12", null);
        assertEquals(12, bean.byte4);

        Field field5 = Test1.class.getDeclaredField("char5");
        binder.bind(bean, field5, Character.TYPE, "a", null); // only first char is accepted
        assertEquals('a', bean.char5);

        Field field6 = Test1.class.getDeclaredField("float6");
        binder.bind(bean, field6, Float.TYPE, "12.34", null);
        assertEquals(12.34f, bean.float6, 0.001f);

        Field field7 = Test1.class.getDeclaredField("double7");
        binder.bind(bean, field7, Double.TYPE, "1234.56789123456", null);
        assertEquals(1234.56789123456, bean.double7, 0.000000000001);

        Field field8 = Test1.class.getDeclaredField("boolean8");
        binder.bind(bean, field8, Boolean.TYPE, "true", null);
        assertTrue(bean.boolean8);

        Field field9 = Test1.class.getDeclaredField("string9");
        binder.bind(bean, field9, String.class, "umjammer", "umjammer");
        assertEquals("umjammer", bean.string9);
    }
}
