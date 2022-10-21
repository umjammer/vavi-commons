/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ClassUtilTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/10/10 nsano initial version <br>
 */
class ClassUtilTest {

    @Test
    void test1() throws Exception {
        Method m = ClassUtil.class.getMethod("typeSignatures", Class.class);
        assertEquals("(Ljava/lang/Class;)Ljava/lang/String;", ClassUtil.signature(m));
        assertEquals("typeSignatures(Ljava/lang/Class;)Ljava/lang/String;", ClassUtil.signatureWithName(m));
    }

    @Test
    void test2() throws Exception {
        assertEquals("V", ClassUtil.typeSignatures(Void.TYPE));
        assertEquals("Z", ClassUtil.typeSignatures(Boolean.TYPE));
        assertEquals("B", ClassUtil.typeSignatures(Byte.TYPE));
        assertEquals("C", ClassUtil.typeSignatures(Character.TYPE));
        assertEquals("S", ClassUtil.typeSignatures(Short.TYPE));
        assertEquals("I", ClassUtil.typeSignatures(Integer.TYPE));
        assertEquals("J", ClassUtil.typeSignatures(Long.TYPE));
        assertEquals("F", ClassUtil.typeSignatures(Float.TYPE));
        assertEquals("D", ClassUtil.typeSignatures(Double.TYPE));
        assertEquals("Ljava/lang/String;", ClassUtil.typeSignatures(String.class));
        assertEquals("[Ljava/lang/String;", ClassUtil.typeSignatures(String[].class));
        assertEquals("Ljava/lang/Void;", ClassUtil.typeSignatures(Void.class));
        assertEquals("Ljava/lang/Integer;", ClassUtil.typeSignatures(Integer.class));
    }
}