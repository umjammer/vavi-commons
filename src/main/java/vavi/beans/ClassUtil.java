/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.beans;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;


/**
 * ClassUtil.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-10 nsano initial version <br>
 */
public class ClassUtil {

    /** */
    private ClassUtil() {}

    /**
     * @see "https://docs.oracle.com/javase/10/docs/api/com/sun/jdi/doc-files/signature.html"
     */
    public static String typeSignatures(Class<?> c) {
        if (c.isArray()) {
            return "[" + typeSignatures(c.getComponentType());
        } else if (c == Void.TYPE) {
            return "V";
        } else if (c == Boolean.TYPE) {
            return "Z";
        } else if (c == Byte.TYPE) {
            return "B";
        } else if (c == Character.TYPE) {
            return "C";
        } else if (c == Integer.TYPE) {
            return "I";
        } else if (c == Short.TYPE) {
            return "S";
        } else if (c == Long.TYPE) {
            return "J";
        } else if (c == Float.TYPE) {
            return "F";
        } else if (c == Double.TYPE) {
            return "D";
        } else {
            return "L" + c.getName().replace('.', '/') + ";";
        }
    }

    /**
     * @return name + JDI signature
     */
    public static String signatureWithName(Executable executable) {
        String name;
        if (executable instanceof Method) {
            name = executable.getName();
        } else {
            name = "<init>";
        }
        return name + signature(executable);
    }

    /**
     * @return JDI signature
     */
    public static String signature(Executable executable) {
        StringBuilder signature = new StringBuilder();
        signature.append("(");
        for (Class<?> c : executable.getParameterTypes()) {
            signature.append(typeSignatures(c));
        }
        signature.append(")");

        if (executable instanceof Method) {
            signature.append(typeSignatures(((Method) executable).getReturnType()));
        }

        return signature.toString();
    }
}
