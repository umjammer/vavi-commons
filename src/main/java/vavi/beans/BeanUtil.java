/*
 * Copyright (c) 2008 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;


/**
 * BeanUtil.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public abstract class BeanUtil {

//  private static Log logger = LogFactory.getLog(CsvUtil.class);

    /**
     * 初めに通常に読めるフィールドで取得します。
     * 次に Getter メソッド(Bean命名規則, booleanの場合isFooあり)で取得します。
     * 最後に private フィールドを強制的に取得します。
     *
     * TODO use {@link java.beans.Introspector} ??? or "org.apache.commons.beanutils.BeanUtils"
     *
     * @param field 対象となるフィールド定義
     * @param bean 取得対象のオブジェクト
     */
    public static Object getFieldValue(Field field, Object bean) {
        Class<?> beanClass = bean.getClass();
        Class<?> fieldClass = field.getType();
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            String fieldName = field.getName();
            try {
//logger.debug("method: " + "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
                Method method = beanClass.getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
                return method.invoke(bean);
            } catch (NoSuchMethodException e2) {
                if (Boolean.TYPE.equals(fieldClass)) {
                    try {
                        Method method = beanClass.getMethod("is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
                        return method.invoke(bean);
                    } catch (InvocationTargetException e3) {
                        throw new IllegalStateException(e3);
                    } catch (NoSuchMethodException e3) {
                        return getPrivateFieldValue(field, bean);
//                      throw new IllegalStateException("no access method for: " + fieldName);
                    } catch (IllegalAccessException e3) {
                        throw new IllegalStateException(e3);
                    }
                } else {
                    return getPrivateFieldValue(field, bean);
//                  throw new IllegalStateException("no access method for: " + fieldName);
                }
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException(e2);
            } catch (IllegalAccessException e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    /**
     * 初めに通常に読めるフィールドで設定します。
     * 次に Setter メソッドで設定します。
     * 最後に private フィールドを強制的に設定します。
     *
     * TODO use {@link java.beans.Introspector} ??? or "org.apache.commons.beanutils.BeanUtils"
     *
     * @param field 対象となるフィールド定義
     * @param bean 設定対象のオブジェクト
     * @param value 設定する値
     */
    public static void setFieldValue(Field field, Object bean, Object value) {
        Class<?> beanClass = bean.getClass();
        Class<?> fieldClass = field.getType();
        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            String fieldName = field.getName();
            try {
//logger.debug("method: " + "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
                Method method = beanClass.getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), fieldClass);
                method.invoke(bean, value);
            } catch (NoSuchMethodException e2) {
                setPrivateFieldValue(field, bean, value);
//              throw new IllegalStateException("no access method for: " + fieldName);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException(e2);
            } catch (IllegalAccessException e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    /** */
    private static Field getPrivateField(final Class<?> clazz, final String name) throws PrivilegedActionException {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() {
            /** @throws NoSuchFieldException */
            public Field run() throws Exception {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            }
        });
    }

    /** */
    private static Object getPrivateFieldValue(Field field, Object bean) {
        try {
            Field accessibleField = getPrivateField(field.getDeclaringClass(), field.getName());
            return accessibleField.get(bean);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (PrivilegedActionException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private static void setPrivateFieldValue(Field field, Object bean, Object value) {
        try {
            Field accessibleField = getPrivateField(field.getDeclaringClass(), field.getName());
            accessibleField.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (PrivilegedActionException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
