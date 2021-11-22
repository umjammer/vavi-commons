/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.Serializable;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JdbcMapTest {

    static {
        System.setProperty("jdbc.drivers", "org.sqlite.JDBC");
        System.setProperty("vavix.uti.JdbcMap.url", "jdbc:sqlite:file:tmp/myDb");
        System.setProperty("vavix.uti.JdbcMap.username", "sa");
        System.setProperty("vavix.uti.JdbcMap.password", "sa");
    }

    static class Test1 implements Serializable {
        private static final long serialVersionUID = -3948269119832569132L;
        int i;
        String a;
        byte[] b;
        Test1(int i, String a, byte[] b) {
            this.i = i;
            this.a = a;
            this.b = b;
        }
    }

    @Test
    void test() throws Exception {
        Map<String, Test1> map = new JdbcMap<>();
        map.put("sano", new Test1(1, "sano", "naohide".getBytes()));
        map.put("vavi", new Test1(2, "vavi", "vavi".getBytes()));
        map.put("umjammer", new Test1(1000, "umjammer", "umjammer".getBytes()));

        assertEquals(3, map.size());
        assertEquals("vavi", map.get("vavi").a);
        assertEquals(1000, map.get("umjammer").i);
        assertTrue(map.containsKey("sano"));
        assertFalse(map.containsKey("uniquro"));
    }
}

/* */
