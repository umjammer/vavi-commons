/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;


/**
 * Set a value into a field easily by {@link String}.
 *
 * TODO naming
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public class DefaultBinder implements Binder {

    @Override
    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        if (fieldClass.equals(Boolean.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(Boolean.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? false : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(Integer.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Integer.parseInt(value));
        } else if (fieldClass.equals(Integer.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Integer.parseInt(value));
        } else if (fieldClass.equals(Short.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Short.parseShort(value));
        } else if (fieldClass.equals(Short.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Short.parseShort(value));
        } else if (fieldClass.equals(Byte.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Byte.parseByte(value));
        } else if (fieldClass.equals(Byte.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Byte.parseByte(value));
        } else if (fieldClass.equals(Long.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Long.parseLong(value));
        } else if (fieldClass.equals(Long.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Long.parseLong(value));
        } else if (fieldClass.equals(Float.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Float.parseFloat(value));
        } else if (fieldClass.equals(Float.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Float.parseFloat(value));
        } else if (fieldClass.equals(Double.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : Double.parseDouble(value));
        } else if (fieldClass.equals(Double.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : Double.parseDouble(value));
        } else if (fieldClass.equals(Character.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? null : value.charAt(0)); // TODO ???
        } else if (fieldClass.equals(Character.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.isEmpty() ? 0 : value.charAt(0)); // TODO ???
        } else {
            BeanUtil.setFieldValue(field, destBean, elseValue);
        }
    }
}
