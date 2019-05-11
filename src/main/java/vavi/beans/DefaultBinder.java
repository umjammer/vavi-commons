/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;


/**
 * フィールドへの代入を行う基本クラスです。
 *
 * TODO naming
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public class DefaultBinder {

    /**
     * プリミティブ型、及びそのラッパー型のフィールドに値を設定します。
     * value が null 及び長さが 0 の場合 null が設定されます。
     * その他の型は elseValue が設定されます。型が合わない場合は例外が発生します。
     */
    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        if (fieldClass.equals(Boolean.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(Boolean.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? false : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(Integer.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Integer.parseInt(value));
        } else if (fieldClass.equals(Integer.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Integer.parseInt(value));
        } else if (fieldClass.equals(Short.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Short.parseShort(value));
        } else if (fieldClass.equals(Short.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Short.parseShort(value));
        } else if (fieldClass.equals(Byte.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Byte.parseByte(value));
        } else if (fieldClass.equals(Byte.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Byte.parseByte(value));
        } else if (fieldClass.equals(Long.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Long.parseLong(value));
        } else if (fieldClass.equals(Long.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Long.parseLong(value));
        } else if (fieldClass.equals(Float.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Float.parseFloat(value));
        } else if (fieldClass.equals(Float.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Float.parseFloat(value));
        } else if (fieldClass.equals(Double.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Double.parseDouble(value));
        } else if (fieldClass.equals(Double.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Double.parseDouble(value));
        } else if (fieldClass.equals(Character.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Character.valueOf(value.charAt(0))); // TODO ???
        } else if (fieldClass.equals(Character.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? 0 : Character.valueOf(value.charAt(0))); // TODO ???
        } else {
            BeanUtil.setFieldValue(field, destBean, elseValue);
        }
    }
}

/* */
