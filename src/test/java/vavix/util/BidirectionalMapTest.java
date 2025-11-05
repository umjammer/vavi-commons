/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * BidirectionalMap.
 *
 * TODO check other impls.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 240322 nsano initial version <br>
 */
class BidirectionalMapTest {

    @Test
    void testBasicPutAndGet() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);

        assertEquals(10, map.get("name"));
        assertEquals("name", map.getKey(10));
    }

    @Test
    void testDuplicateKey() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);
        map.put("name", 20);

        assertEquals(20, map.get("name"));
        assertEquals("name", map.getKey(20));
    }

    @Test
    void testDuplicateValue() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);
        // ktv: {"name", 10}
        // vtk: {10, "name"}

        map.put("age", 10);
        // ktv: {"age, 10}
        // vtk: {10, "age"}

        assertEquals(10, map.get("age"));
        assertEquals("age", map.getKey(10));
    }

    @Test
    void testRemoveByKey() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);

        int removedValue = map.remove("name");

        assertNull(map.get("name"));
        assertNull(map.getKey(10));
        assertEquals(10, removedValue);
    }

    @Test
    void testRemoveByValue() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);

        String removedKey = map.removeValue(10);

        assertNull(map.get("name"));
        assertNull(map.getKey(10));
        assertEquals("name", removedKey);
    }

    @Test
    void testContainsKey() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);

        assertTrue(map.containsKey("name"));
        assertFalse(map.containsKey("age"));
    }

    @Test
    void testContainsValue() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);

        assertTrue(map.containsValue(10));
        assertFalse(map.containsValue(20));
    }

    @Test
    void testSize() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);
        map.put("age", 20);

        assertEquals(2, map.size());
    }

    @Test
    void testEmptyMap() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        assertTrue(map.isEmpty());
    }

    @Test
    void testPutAll() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();
        Map<String, Integer> sourceMap = new HashMap<>();

        sourceMap.put("name", 10);
        sourceMap.put("age", 20);

        map.putAll(sourceMap);

        assertEquals(2, map.size());
        assertEquals(10, map.get("name"));
        assertEquals(20, map.get("age"));
    }

    @Test
    void testClear() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        map.put("name", 10);
        map.put("age", 20);

        map.clear();

        assertTrue(true);
    }

    @Test
    void testUnsupportedEntrySet() {
        BidirectionalMap<String, Integer> map = new BidirectionalMap<>();

        assertThrows(UnsupportedOperationException.class, map::entrySet);
    }
}
