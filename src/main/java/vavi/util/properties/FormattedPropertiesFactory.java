/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties;

import java.util.regex.Pattern;


/**
 * FormattedPropertiesFactory.
 *
 * @param <V> stored value type
 * @param <K> part of stored key type
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/05 umjammer initial version <br>
 */
public abstract class FormattedPropertiesFactory<K, V> extends PropertiesFactoryBase<K, V, String> {

    /** */
    private String format;

    /**
     * @param path properties file
     * @param format "foo.%s.bar", only %s, %d supported
     * @throws IllegalStateException at {@link #getStoreValue(String)}
     */
    public FormattedPropertiesFactory(String path, String format) {
        super(path, format);
    }

    @Override
    protected void preInit(String... args) {
        this.format = args[0];
    }

    @Override
    protected boolean match(String key) {
        return Pattern.compile(format.replace(".", "\\.").replaceFirst("%[sd]", ".+")).matcher(key).matches();
    }

    @Override
    protected String getRestoreKey(K partOfAKey) {
        return String.format(format, partOfAKey);
    }

    @Override
    protected String getStoreKey(String key) {
        return key;
    }

    @Override
    protected abstract V getStoreValue(String value);

    /** */
    public static class Basic extends FormattedPropertiesFactory<String, String> {
        /**
         * @param path properties file
         */
        public Basic(String path, String format) {
            super(path, format);
        }

        @Override
        protected String getStoreValue(String value) {
            return value;
        }
    }
}

/* */
