/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.System.getLogger;


/**
 * BidirectionalMap.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 240322 nsano initial version <br>
 */
public class BidirectionalMap<K, V> implements Map<K, V> {

    private static final Logger logger = getLogger(BidirectionalMap.class.getName());

    private final Map<K, V> keyToValue;
    private final Map<V, K> valueToKey;

    public BidirectionalMap() {
        this.keyToValue = new HashMap<>();
        this.valueToKey = new HashMap<>();
    }

    @Override
    public int size() {
        return keyToValue.size();
    }

    @Override
    public boolean isEmpty() {
        return keyToValue.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keyToValue.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueToKey.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return keyToValue.get(key);
    }

    public K getKey(V value) {
        return valueToKey.get(value);
    }

    @Override
    public V put(K key, V value) {
        if (keyToValue.containsKey(key)) {
            V oldValue = keyToValue.get(key);
            valueToKey.remove(oldValue);
        }
        if (valueToKey.containsKey(value)) {
            K oldKey = valueToKey.get(value);
            keyToValue.remove(oldKey);
        }
        keyToValue.put(key, value);
        valueToKey.put(value, key);
logger.log(Level.TRACE, "PUT: " + key + ", " + value);
logger.log(Level.TRACE, "ktv: " + keyToValue);
logger.log(Level.TRACE, "vtk: " + valueToKey);
        return value;
    }

    @Override
    public V remove(Object key) {
        if (containsKey(key)) {
            V value = keyToValue.remove(key);
            valueToKey.remove(value);
            return value;
        }
        return null;
    }

    public K removeValue(V value) {
        if (containsValue(value)) {
            K key = valueToKey.remove(value);
            keyToValue.remove(key);
            return key;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keyToValue.clear();
        valueToKey.clear();
    }

    @Override
    public Set<K> keySet() {
        return keyToValue.keySet();
    }

    @Override
    public Collection<V> values() {
        return keyToValue.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("entrySet not supported for BidirectionalMap");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidirectionalMap<?, ?> that = (BidirectionalMap<?, ?>) o;

        if (!keyToValue.equals(that.keyToValue)) return false;
        return valueToKey.equals(that.valueToKey);
    }

    @Override
    public int hashCode() {
        int result = keyToValue.hashCode();
        result = 31 * result + valueToKey.hashCode();
        return result;
    }
}
