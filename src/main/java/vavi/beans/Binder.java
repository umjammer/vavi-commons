/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;


/**
 * Set a value into a field easily by {@link String}.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/03/01 umjammer initial version <br>
 */
public interface Binder {

    /**
     * Set values for primitive type and its wrapper type fields.
     * If value is null and length is 0, null is set.
     * For other types, elseValue is set. If the types do not match, an exception will be thrown.
     */
    void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue);
}

/* */
