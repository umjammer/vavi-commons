/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * Assign a value to a field.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 200227 nsano initial version <br>
 */
public class InstanciationBinder extends DefaultBinder {

    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        try {
            Class<?> clazz = Class.forName(value);
            BeanUtil.setFieldValue(field, destBean, clazz.getDeclaredConstructor().newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}
