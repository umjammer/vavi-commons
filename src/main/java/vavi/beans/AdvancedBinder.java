/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;


/**
 * Advance class for setting a value into a field easily by {@link String}.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 200227 nsano initial version <br>
 */
public class AdvancedBinder extends DefaultBinder {

    /**
     * This method is able to set a {@link java.io.File}, {@link java.io.InputStream} and {@code enum} type field.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        if (fieldClass.equals(File.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : new File(value));
        } else if (fieldClass.equals(InputStream.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : AdvancedBinder.class.getResourceAsStream(value));
        } else if (fieldClass.isEnum()) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Enum.valueOf((Class) fieldClass, value));
        } else {
            super.bind(destBean, field, fieldClass, value, elseValue);
        }
    }
}
