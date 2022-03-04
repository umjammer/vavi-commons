/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;


/**
 * PrefixedPropertiesFactory.
 * <p>
 * don't include prefix in {@link #get(Object)} method's argument.
 * </p>
 * @param <V> stored value type
 * @param <K> part of stored key type
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/05 umjammer initial version <br>
 */
public abstract class PrefixedPropertiesFactory<K, V> extends PropertiesFactoryBase<K, V, String> {

    /** */
    private String prefix;

    /**
     * @param path properties file
     * @param prefix if target is "foo.bar.1", "foo.bar.2"... then "foo.bar"
     */
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
    protected String getRestoreKey(K partOfAKey) {
        return prefix + partOfAKey;
    }

    @Override
    protected String getStoreKey(String key) {
        return key;
    }

    @Override
    protected abstract V getStoreValue(String value);
}

/* */
