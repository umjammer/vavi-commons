/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * ExceptionClassFileTransformer.
 *
 * TODO "https://stackoverflow.com/questions/36790578/java-agent-cannot-transform-all-the-classes-in-my-project" answer 2.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/05/09 umjammer initial version <br>
 */
public class ExceptionClassFileTransformer implements VaviClassFileTransformer {

    /** never use before call #transform() */
    private String id;

    /* */
    public String getId() {
        return id;
    }

    /* */
    public void setId(String id) {
        this.id = id;
    }

    /** */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (className.equals("java/lang/Exception")) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (int i = 0; i < ctMethods.length; i++) {
                    ctMethods[i].insertBefore("{new Exception(\"*** DUMMY ***\").printStackTrace();}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
System.err.println("ExceptionClassFileTransformer::transform: " + className + ": " + e);
                return null;
            }
        } else {
            return null;
        }
    }
}

/* */
