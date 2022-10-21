/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JdbcMapTest {

    static {
        System.setProperty("jdbc.drivers", "org.sqlite.JDBC");
        System.setProperty("vavix.util.JdbcMap.url", "jdbc:sqlite:file:tmp/myDb");
        System.setProperty("vavix.util.JdbcMap.username", "sa");
        System.setProperty("vavix.util.JdbcMap.password", "sa");
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

    @BeforeAll
    static void setup() throws Exception {
        Files.createDirectories(Paths.get("tmp"));
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

    @Test
    void test2() throws Exception {
        Map<String, Map<String, Map<String, Integer>>> map = new JdbcMap<>();
        Map<String, Integer> map21 = new HashMap<>();
        map21.put("nsano", 100);
        map21.put("vavi", 80);
        map21.put("umjammer", 60);
        Map<String, Integer> map22 = new HashMap<>();
        map22.put("java", 1);
        map22.put("scala", 2);
        map22.put("clojure", 3);
        Map<String, Map<String, Integer>> map3 = new HashMap<>();
        map3.put("alias", map21);
        map3.put("cl", map22);
        map.put("github", map3);
        map.put("gitlab", new HashMap<>());
        map.get("gitlab").put("alias", new HashMap<>());

Debug.println("github: " + map.get("github"));
Debug.println("github.alias: " + map.get("github").get("alias"));
        assertEquals(100, map.get("github").get("alias").get("nsano"));
        assertEquals(3, map.get("github").get("cl").get("clojure"));
Debug.println("gitlab: " + map.get("gitlab"));
Debug.println("gitlab.alias: " + map.get("gitlab").get("alias")); // null because put after serialization
        assertNull(map.get("gitlab").get("alias"));
    }
}

/* */
