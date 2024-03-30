/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


/**
 * PropertiesFactoryTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/06/09 umjammer initial version <br>
 */
class PropertiesFactoryTest {

    @Test
    void testPrefixedClassPropertiesFactory() {
        PrefixedPropertiesFactory<Integer, Number> ppf = new PrefixedClassPropertiesFactory<>("test.properties", "prefix.");
        assertInstanceOf(String.class, ppf.get(1));
        assertInstanceOf(StringBuilder.class, ppf.get(2));
        assertInstanceOf(StringBuffer.class, ppf.get(3));
    }

    @Test
    void testFormattedPropertiesFactory() {
        FormattedPropertiesFactory<String, String> fpf = new FormattedPropertiesFactory<>("test.properties", "formatted.%s") {
            @Override
            protected String getStoreValue(String value) {
                return value;
            }
        };
        assertEquals(fpf.get(1 + ".icon"), "icon1");
        assertEquals(fpf.get(1 + ".name"), "name1");
        assertEquals(fpf.get(2 + ".icon"), "icon2");
        assertEquals(fpf.get(2 + ".name"), "name2");
        assertEquals(fpf.get(3 + ".icon"), "icon3");
        assertEquals(fpf.get(3 + ".name"), "name3");
    }

    @Test
    void testBasic() {
        FormattedPropertiesFactory.Basic fpf = new FormattedPropertiesFactory.Basic("test.properties", "formatted.%s");
        int i = 0;
        while (fpf.get(i + ".icon") != null) {
            assertEquals(fpf.get(i + ".icon"), "icon" + i);
            assertEquals(fpf.get(i + ".name"), "name" + i);
            i++;
        }
    }
}
