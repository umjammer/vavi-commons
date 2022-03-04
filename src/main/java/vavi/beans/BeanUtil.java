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
import java.util.Arrays;
import java.util.logging.Logger;


/**
 * Set and get a field value easily using the Java reflection API.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public abstract class BeanUtil {

    private static Logger logger = Logger.getLogger(BeanUtil.class.getName());

    /**
     * First, try to get by a normal field.
     * Second, try to get by a getter method using Bean naming method, for boolean isFoo also.
     * Finally force to get a private field.
     *
     * TODO use {@link java.beans.Introspector} ??? or "org.apache.commons.beanutils.BeanUtils"
     *
     * @param name a target field definition
     * @param bean a object that is a target field owner 
     */
    public static Object getFieldValue(Field field, Object bean) {
        String name = field.getName();

        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
logger.fine("no field: " + field.getName());
        }

        try {
            return getByMethod(bean, name);
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + name);
        }

        try {
            return getByMethod(bean, getGetterName(name));
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + getGetterName(name));
        }

        if (Boolean.TYPE.equals(field.getType())) {
            try {
                return getByMethod(bean, getBooleanGetterName(name));
            } catch (NoSuchMethodException e) {
logger.fine("no method: " + getBooleanGetterName(name));
            }
        }

        return getPrivateFieldValue(field, bean);
    }

    /** */
    private static String getGetterName(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /** */
    private static String getBooleanGetterName(String name) {
        return "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /** Recurse super classes. */
    private static Object getByMethod(Object bean, String name) throws NoSuchMethodException {
        try {
            Method method = getMethodByNameOf(bean.getClass(), name);
            return method.invoke(bean);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * First, try to set by a normal field.
     * Second, try to set by a setter method.
     * Finally force to set a private field.
     *
     * TODO use {@link java.beans.Introspector} ??? or "org.apache.commons.beanutils.BeanUtils"
     *
     * @param field a target field definition
     * @param bean an object to be set
     * @param value value to be set 
     */
    public static void setFieldValue(Field field, Object bean, Object value) {
        Class<?> valueClass = field.getType();
        String name = field.getName();

        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
logger.fine("no field: " + name);
        }

        try {
            setByMethod(bean, name, valueClass, value);
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + name);
        }

        try {
            setByMethod(bean,  getSetterName(name), valueClass, value);
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + getSetterName(name));
        }

        setPrivateFieldValue(field, bean, value);
    }

    /** */
    private static String getSetterName(String name) {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Recurse super classes.
     * @throws IllegalStateException do you have the same name private static method in your object?
     */
    private static void setByMethod(Object bean, String name, Class<?> valueClass, Object value) throws NoSuchMethodException {
        try {
            Method method = getMethodByNameOf(bean.getClass(), name, valueClass);
            method.invoke(bean, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(bean.getClass().getName() + "#" + name, e);
        }
    }

    /** Recurse super classes. */
    public static Field getPrivateField(Class<?> clazz, String name) throws PrivilegedActionException {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() {
            /** @throws NoSuchFieldException */
            public Field run() throws Exception {
                Field field = getFieldByNameOf(clazz, name);
                field.setAccessible(true);
                return field;
            }
        });
    }

    /**
     * Recurse super classes.
     */
    public static Method getPrivateMethod(Class<?> clazz, String name, Class<?>... argTypes) throws PrivilegedActionException {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() {
            /** @throws NoSuchMethodException */
            public Method run() throws Exception {
                Method method = getMethodByNameOf(clazz, name, argTypes);
                method.setAccessible(true);
                return method;
            }
        });
    }

    /** Recurse super classes. */
    public static Object invoke(Method method, Object bean, Object... args) {
        try {
            return method.invoke(bean, args);
        } catch (IllegalAccessException e) {
logger.fine("method access exception: " + method.getName());
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }

        try {
            method = BeanUtil.getPrivateMethod(bean.getClass(), method.getName(), method.getParameterTypes());
            return method.invoke(bean, args);
        } catch (PrivilegedActionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Getter */
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

    /** Setter */
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

    /** Recurse super classes. */
    public static Field getFieldByNameOf(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = null;
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException();
        }
        return field;
    }

    /** Recurse super classes. */
    public static Method getMethodByNameOf(Class<?> clazz, String name, Class<?>... argTypes) throws NoSuchMethodException {
        Method method = null;
        while (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(name, argTypes);
                break;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (method == null) {
            throw new NoSuchMethodException(name + "(" + Arrays.toString(argTypes) + ")");
        }
        return method;
    }

    /**
     * First, try to get by a normal field.
     * Second, try to get by a getter method using Bean naming method, for boolean isFoo also.
     * Finally force to get a private field.
     *
     * TODO use {@link java.beans.Introspector} ??? or "org.apache.commons.beanutils.BeanUtils"
     *
     * @param name a target field or method name
     * @param bean a object that is a target field owner 
     */
    public static Object getValue(String name, Object bean) {
        try {
            Field field = getFieldByNameOf(bean.getClass(), name);
            return getFieldValue(field, bean);
        } catch (NoSuchFieldException e) {
logger.fine("no field: " + name);
        }

        try {
            return getByMethod(bean, name);
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + name);
        }

        try {
            return getByMethod(bean, getGetterName(name));
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + getGetterName(name));
        }

        try {
            return getByMethod(bean, getBooleanGetterName(name));
        } catch (NoSuchMethodException e) {
logger.fine("no method: " + getBooleanGetterName(name));
        }

        try {
            Field field = getFieldByNameOf(bean.getClass(), name);
            return getPrivateFieldValue(field, bean);
        } catch (NoSuchFieldException e) {
logger.fine("no private field: " + name);
            throw new IllegalStateException(e);
        }
    }
}

/* */
