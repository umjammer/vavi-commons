/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * properties -> class instance (V).
 *
 * @param <V> stored value type
 * @param <K> part of stored key type
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/05 umjammer initial version <br>
 */
public class PrefixedClassPropertiesFactory<K, V> extends PrefixedPropertiesFactory<K, V> {

    private static final Logger logger = getLogger(PrefixedClassPropertiesFactory.class.getName());

    /**
     * @param path properties file
     * @param prefix if target is "foo.bar.1", "foo.bar.2"... then "foo.bar"
     * @throws IllegalStateException at {@link #getStoreValue(String)}
     */
    public PrefixedClassPropertiesFactory(String path, String prefix) {
        super(path, prefix);
    }

    /**
     * @throws IllegalStateException
     */
    @Override
    protected V getStoreValue(String value) {
        try {
            @SuppressWarnings("unchecked")
            Class<V> clazz = (Class<V>) Class.forName(value);
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
logger.log(Level.ERROR, value, e);
            throw new IllegalStateException(e);
        } catch (Error e) {
logger.log(Level.ERROR, value, e);
            throw e;
        }
    }
}
