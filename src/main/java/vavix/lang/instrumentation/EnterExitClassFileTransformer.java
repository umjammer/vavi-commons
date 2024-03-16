/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * EnterExitClassFileTransformer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class EnterExitClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private Pattern pattern;

    /** */
    private static final String prefix = EnterExitClassFileTransformer.class.getName();

    /** never use before call #transform() */
    private String id;

    /* */
    public String getId() {
        return id;
    }

    /* */
    public void setId(String id) {
        this.id = id;
//System.err.println("EnterExitClassFileTransformer::setId: id: " + id);
    }

    /**
     * system environment
     * <pre>
     * vavix.lang.instrumentation.EnterExitClassFileTransformer.${id}.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (pattern == null) {
            Properties props = System.getProperties();
            try {
                pattern = Pattern.compile(props.getProperty(prefix + "." + id + "." + "pattern"));
            } catch (Exception e) {
System.err.println("EnterExitClassFileTransformer::transform: bad pattern: " + prefix + "." + id + "." + "pattern");
            }
        }

        ClassPool classPool = ClassPool.getDefault();

//System.err.println("EnterExitClassFileTransformer::transform: class: " + className);
        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
//System.err.println("EnterExitClassFileTransformer::transform: matched: " + className);
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (CtMethod ctMethod : ctMethods) {
                    ctMethod.insertBefore("{System.err.println(\"Enter " + ctClass.getName() + "#" + ctMethod.getName() + ctMethod.getSignature() + "\");}");
                    ctMethod.insertAfter("{System.err.println(\"Exit " + ctClass.getName() + "#" + ctMethod.getName() + ctMethod.getSignature() + "\");}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
//e.printStackTrace(System.err);
System.err.println("EnterExitClassFileTransformer::transform: " + className + ": " + e);
//                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
                return null;
            }
        } else {
//System.err.println("ignore: " + className);
            return null;
        }
    }
}
