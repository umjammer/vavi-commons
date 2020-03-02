/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;


/**
 * Binder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/03/01 umjammer initial version <br>
 */
public interface Binder {

    /**
     * Sets the value to the destBean's field.
     */
    void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue);
}

/* */
