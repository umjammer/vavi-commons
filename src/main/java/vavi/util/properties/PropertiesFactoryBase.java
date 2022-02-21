/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * PropertiesFactoryBase.
 *
 * TODO to be HashMap sub class?
 * TODO CollectionProperty annotation?
 *
 * @param <V> stored value type
 * @param <K> part of stored key type
 * @param <Args> args type
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/04 umjammer initial version <br>
 */
public abstract class PropertiesFactoryBase<K, V, Args> implements Iterable<Map.Entry<String, V>> {

    /**
     * @param key property key
     */
    protected abstract boolean match(String key);

    /**
     * @param key property key
     */
    protected abstract String getStoreKey(String key);

    /**
     *
     */
    protected abstract String getRestoreKey(K partOfAKey);

    /**
     * @param value property value that should be state less.
     */
    protected abstract V getStoreValue(String value);

    /**
     * <V> オブジェクトのインスタンス集。
     * インスタンスを使いまわすのでステートレスでなければならない。
     * TODO protected ぐぬぬ...
     */
    protected Map<String, V> instances = new HashMap<>();

    /**
     * @param args set by the {@link #PropertiesFactoryBase(String, Object...)}
     */
    protected abstract void preInit(@SuppressWarnings("unchecked") Args... args);

    /**
     * {@link #preInit(Object...)} will be called.
     * @param path should be full path
     */
    public PropertiesFactoryBase(String path, @SuppressWarnings("unchecked") Args... args) {
        preInit(args);

        try {
            // props
            Properties props = new Properties();
Debug.println(Level.FINE, "path: " + path);
            props.load(PropertiesFactoryBase.class.getResourceAsStream(path));

            //
            Iterator<?> i = props.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (match(key)) {
Debug.println(Level.FINE, "matched: " + key + "=" + props.getProperty(key));
                    instances.put(getStoreKey(key), getStoreValue(props.getProperty(key)));
                }
            }
        } catch (IOException e) {
Debug.print(Level.SEVERE, "path: " + path);
Arrays.asList(args).forEach(Debug::print);
Debug.printStackTrace(Level.SEVERE, e);
            throw new IllegalStateException(e);
        } catch (Throwable e) {
Debug.print(Level.SEVERE, "path: " + path);
Arrays.asList(args).forEach(Debug::print);
Debug.printStackTrace(Level.SEVERE, e);
            throw e;
        }
    }

    /**
     * @return same instance for each name
     * @throws IllegalArgumentException does not contains key
     * @param partOfAKey used by {@link // #getRestoreKey(Object)} that returns real key for {@link // #instances}.
     */
    public V get(K partOfAKey) {
        if (instances.containsKey(getRestoreKey(partOfAKey))) {
            return instances.get(getRestoreKey(partOfAKey));
        } else {
            throw new IllegalArgumentException(partOfAKey.toString());
        }
    }

    @Override
    public Iterator<Map.Entry<String, V>> iterator() {
        return instances.entrySet().iterator();
    }
}
