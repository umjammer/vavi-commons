/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;

import vavi.util.Debug;


/**
 * PrefixedPropertiesFactory.
 *
 * @param <V> stored value type
 * @param <K> part of stored key type
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/05 umjammer initial version <br>
 */
public class PrefixedPropertiesFactory<K, V> extends PropertiesFactoryBase<K, V, String> {

    /** */
    private String prefix;

    /** */
    public PrefixedPropertiesFactory(String path, String prefix) {
        super(path, prefix);
    }

    @Override
    protected void preInit(String... args) {
        this.prefix = args[0];
    }

    @Override
    protected boolean match(String key) {
        return key.startsWith(prefix);
    }

    @Override
    protected String getRestoreKey(K key) {
        return prefix + key;
    }

    @Override
    protected String getStoreKey(String key) {
        return key;
    }

    @Override
    protected V getStoreValue(String value) {
        try {
            @SuppressWarnings("unchecked")
            Class<V> clazz = (Class<V>) Class.forName(value);
            return clazz.newInstance();
        } catch (Exception e) {
Debug.printStackTrace(e);
            throw new IllegalStateException(e);
        }
    }
}

/* */
