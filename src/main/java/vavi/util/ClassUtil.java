/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;


/**
 * クラス関連のユーティリティクラスです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020517 nsano initial version <br>
 *          0.01 040627 nsano add getWrapperClass <br>
 */
public final class ClassUtil {

    /** Cannot access. */
    private ClassUtil() {}

    /**
     * 文字列からクラスを取得します．
     * TODO なんかどっかにありそう．
     * 
     * @param className プリミティブ型もそのまま指定できます．
     *                  逆に java.lang は認識しないので書いてください．
     */
    public static Class<?> forName(String className)
        throws ClassNotFoundException {

        Class<?> clazz;

        if ("boolean".equals(className)) {
            clazz = Boolean.TYPE;
        } else if ("byte".equals(className)) {
            clazz = Byte.TYPE;
    	} else if ("char".equals(className)) {
            clazz = Character.TYPE;
    	} else if ("double".equals(className)) {
            clazz = Double.TYPE;
    	} else if ("float".equals(className)) {
            clazz = Float.TYPE;
    	} else if ("int".equals(className)) {
            clazz = Integer.TYPE;
        } else if ("long".equals(className)) {
            clazz = Long.TYPE;
        } else if ("short".equals(className)) {
            clazz = Short.TYPE;
        } else if ("void".equals(className)) {
            clazz = Void.TYPE;
        } else {
            clazz = Class.forName(className);
        }
//Debug.println(clazz);
        return clazz;
    }

    /**
     * 文字列からコンストラクタ用の引数型のクラスのリストを取得します．
     * @param	line	デリミタは { ',', '\t', ' ' }
     *			プリミティブ型はそのまま書く． int, long ...
     */
    public static Class<?>[] getArgumentTypes(String line)
        throws ClassNotFoundException {

        StringTokenizer st = new StringTokenizer(line, "\t ,");
        Class<?>[] argTypes = new Class[st.countTokens()];
        for (int j = 0; j < argTypes.length; j++) {
            argTypes[j] = forName(st.nextToken());
        }

        return argTypes;
    }

    /**
     * 文字列からコンストラクタ用の引数のオブジェクトのリストを取得します．
     * @param	line	デリミタは { ',', '\t', ' ' }
     *			null はそのまま書く． null
     */
    static Object[] getArguments(String line, Class<?>[] argTypes)
        throws InstantiationException,
               IllegalAccessException {

        StringTokenizer st = new StringTokenizer(line, "\t ,");
        Object[] args = new Object[st.countTokens()];
        for (int j = 0; j < args.length; j++) {
            String arg = st.nextToken();
            if ("null".equals(arg)) {
                args[j] = null;
            } else if (argTypes[j] == Boolean.TYPE) {
                args[j] = new Boolean(arg);
            } else if (argTypes[j] == Byte.TYPE) {
                args[j] = new Byte(arg);
            } else if (argTypes[j] == Character.TYPE) {
                if (arg.length() > 1) {
                    throw new IllegalArgumentException(arg + " for char");
                }
                args[j] = new Character(arg.charAt(0));
            } else if (argTypes[j] == Double.TYPE) {
                args[j] = new Double(arg);
            } else if (argTypes[j] == Float.TYPE) {
                args[j] = new Float(arg);
            } else if (argTypes[j] == Integer.TYPE) {
                args[j] = new IntegerInstantiator().newInstance(arg);
            } else if (argTypes[j] == Long.TYPE) {
                args[j] = new Long(arg);
            } else if (argTypes[j] == Short.TYPE) {
                args[j] = new Short(arg);
            } else if (argTypes[j] == Void.TYPE) {
                throw new IllegalArgumentException(arg + " for void");
            } else if (argTypes[j] == String.class) {           // 特別
                args[j] = new StringInstantiator().newInstance(arg);
            } else if (argTypes[j] == java.awt.Color.class) {   // 特別
                args[j] = new ColorInstantiator().newInstance(arg);
            } else {    // TODO 再帰の文法でもよさそう．
                args[j] = argTypes[j].newInstance();
            }
        }

        return args;
    }

    /**
     * 新しいインスタンスを取得します．
     * @param	className	プリミティブ型もそのまま指定できます．
     * @param	argTypes	デリミタは { ',', '\t', ' ' }
     *				プリミティブ型はそのまま書く． int, long ...
     * @param	args		デリミタは { ',', '\t', ' ' }
     *				null はそのまま書く． null
     */
    public static Object newInstance(String className,
                                     String argTypes,
                                     String args)
        throws ClassNotFoundException,
               InstantiationException,
               IllegalAccessException,
               NoSuchMethodException,
               InvocationTargetException {

        Class<?> clazz = Class.forName(className);

        Class<?>[] ats = getArgumentTypes(argTypes);
        Object[] as = getArguments(args, ats);

        Constructor<?> constructor = clazz.getConstructor(ats);
        return constructor.newInstance(as);
    }

    /**
     */
    static Field getField(String arg)
        throws NoSuchFieldException,
               ClassNotFoundException,
               IllegalAccessException {

        int p = arg.lastIndexOf('.');
        String className = arg.substring(0, p);
        String enumName = arg.substring(p + 1, arg.length());
//Debug.println(className + "#" + enumName);

        Class<?> clazz = Class.forName(className);

        return clazz.getDeclaredField(enumName);
    }

    /**
     * プリミティブ型からラッパークラスを取得します。
     * @param primitiveClass int.class 等
     */
    public Class<?> getWrapperClass(Class<?> primitiveClass) {
        Object array = Array.newInstance(primitiveClass, 1);
        Object wrapper = Array.get(array, 0);
        return wrapper.getClass();
    }
}

/**
 */
interface Instantiator {
    Object newInstance(String arg) throws InstantiationException;
}

/**
 */
class IntegerInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {

        try {
            // integer
            return new Integer(arg);
        } catch (NumberFormatException e) {
            // enumration class.enum
            try {
                Field field = ClassUtil.getField(arg);

                if (field.getType() != Integer.TYPE)
                    throw new IllegalArgumentException(arg + " for int");

                return new Integer(field.getInt(null));
            } catch (Exception f) {
                throw (InstantiationException)
                    new InstantiationException().initCause(f);
            }
        }
    }
}

/**
 */
class StringInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {
        return arg;
    }
}

/**
 */
class ColorInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {
        try {
            Field field = ClassUtil.getField(arg);

            if (field.getType() != java.awt.Color.class) {
                throw new IllegalArgumentException(arg + " for Color");
            }

            return field.get(null);
        } catch (Exception f) {
            throw (InstantiationException) new InstantiationException().initCause(f);
        }
    }
}

/* */
