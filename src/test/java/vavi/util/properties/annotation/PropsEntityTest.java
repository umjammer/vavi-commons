/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * PropsEntityTest. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@PropsEntity(url = "/vavi/util/properties/annotation/propsEntityTest.properties", resource = true)
public class PropsEntityTest {

    @Property(name = "data1")
    private String data1;

    @Property(name = "data2")
    private int data2;

    @Property
    private String data3;

    public String toString() {
        return "data1: " + data1 +
               ", data2: " + data2 + 
               ", data3: " + data3; 
    }

    public static void main(String[] args) throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        System.err.println(bean);
    }

    @Test
    public void test01() throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        assertEquals("Sano Naohide", bean.data1);
        assertEquals(40, bean.data2);
        assertEquals("Umjammer", bean.data3);
    }
}

/* */
