/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


/**
 * InstantiationBinderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-10 nsano initial version <br>
 */
class InstantiationBinderTest {

    static class Test1 {
        AdvancedBinder bider;
    }

    @Test
    void test1() throws Exception {
        InstantiationBinder binder = new InstantiationBinder();
        Test1 bean = new Test1();

        Field field1 = Test1.class.getDeclaredField("bider");
        binder.bind(bean, field1, Test1.class, "vavi.beans.AdvancedBinder", null);
        assertInstanceOf(vavi.beans.AdvancedBinder.class, bean.bider);
    }
}
