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
public abstract class PropertiesFactoryBase<K, V, Args> {

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
    protected abstract String getRestoreKey(K key);

    /**
     * @param value property value
     */
    protected abstract V getStoreValue(String value);

    /**
     * {@link V} オブジェクトのインスタンス集。
     * インスタンスを使いまわすのでステートレスでなければならない。
     * TODO protected ぐぬぬ...
     */
    protected Map<String, V> instances = new HashMap<>();

    /** */
    protected abstract void preInit(@SuppressWarnings("unchecked") Args... args);

    /**
     * @param path should be full path
     */
    public PropertiesFactoryBase(String path, @SuppressWarnings("unchecked") Args... args) {
        preInit(args);

        try {
            // props
            Properties props = new Properties();
//Debug.println("path: " + path);
            props.load(PropertiesFactoryBase.class.getResourceAsStream(path));

            //
            Iterator<?> i = props.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (match(key)) {
//Debug.println("matched: " + key + "=" + props.getProperty(key));
                    instances.put(getStoreKey(key), getStoreValue(props.getProperty(key)));
                }
            }
        } catch (IOException e) {
Debug.print("path: " + path);
Arrays.asList(args).forEach(Debug::print);
Debug.printStackTrace(e);
            throw new IllegalStateException(e);
        } catch (Throwable e) {
Debug.print("path: " + path);
Arrays.asList(args).forEach(Debug::print);
Debug.printStackTrace(e);
            throw e;
        }
    }

    /**
     * @param key {@link #getPrefix()} and name consist key
     * @return same instance for each name
     * @throws IllegalArgumentException does not contains key
     */
    public V get(K key) {
        if (instances.containsKey(getRestoreKey(key))) {
            return instances.get(getRestoreKey(key));
        } else {
            throw new IllegalArgumentException(key.toString());
        }
    }
}
