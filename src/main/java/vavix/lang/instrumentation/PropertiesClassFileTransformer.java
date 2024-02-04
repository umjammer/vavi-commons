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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;


/**
 * PropertyClassFileTransformer.
 * <p>
 * <code>
 * src/test/resources/VaviInstrumentation.properties
 * </code>
 * <pre>
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.1.class=java/lang/RuntimeException
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.1.method=*
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.1.insertBefore={ new Exception().printStackTrace(); }
 *
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.2.class=your/package/YourClass
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.2.method=yourMethod
 * vavix.lang.instrumentation.PropertiesClassFileTransformer.2.insertAfter={ System.err.println($_.toString()); }
 * </pre>
 * <ul>
 *  <li><code>vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.class</code> is not regex.
 *  <li><code>vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.methods</code> cannot be conflict.
 * </ul>
 * </p>
 * <p>
 * add <code>VaviInstrumentation.properties</code> location to <code>-cp</code>
 * <pre>
 * java \
 * -cp target/classes:src/test/resources \
 * -javaagent:vavi-instrumentation.jar \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.1=vavix.lang.instrumentation.PropertiesClassFileTransformer \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.2=vavix.lang.instrumentation.PropertiesClassFileTransformer \
 * your.MainClass
 * </pre>
 * </p>
 *
 * TODO 同じクラスを2回書き換えられない isFrozen(), deFrost() ???
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class PropertiesClassFileTransformer implements VaviClassFileTransformer {

    /** "VaviInstrumentation.properties" */
    private static Properties props;

    /** */
    private static final String prefix = PropertiesClassFileTransformer.class.getName();

    /** never use before call #transform() */
    private String id;

    /* */
    public String getId() {
        return id;
    }

    /* */
    public void setId(String key) {
        this.id = key;
    }

    /**
     * "VaviInstrumentation.properties" をクラスパスが通った場所においてください。
     * <pre>
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.class ... package/name/ClassName
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.method ... method name ("*" means for all methods)
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.constructor ... constructor name ("vavix.lang.instrumentation.PropertiesClassFileTransformer.method" の方が優先する)
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.insertBefore ... ex. {System.err.println("args: " + $$);}
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.${id}.insertAfter ... ex. {System.err.println("result: " + $_);}
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (props == null) {
            try {
                props = new Properties();
                props.load(VaviInstrumentation.class.getResourceAsStream("/VaviInstrumentation.properties"));
//System.err.println("class: " + props.getProperty("vavix.lang.instrumentation.PropertiesClassFileTransformer." + key + ".class"));
            } catch (Exception e) {
e.printStackTrace(System.err);
                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
            }
        }

//System.err.println("className: " + className);
        if (className.equals(props.getProperty(prefix + "." + id + ".class"))) {
//System.err.println("modify: " + className);
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                String method = props.getProperty(prefix + "." + id + "." + "method");
                String constructor = props.getProperty(prefix + "." + id + "." + "constructor");
                String insertBefore = props.getProperty(prefix + "." + id + "." + "insertBefore");
                String insertAfter = props.getProperty(prefix + "." + id + "." + "insertAfter");

                // TODO regex match
                if (method != null) {
                    if ("*".equals(method)) {
                        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                        for (CtMethod ctMethod : ctMethods) {
                            if (insertBefore != null) {
                                ctMethod.insertBefore(insertBefore);
                            }
                            if (insertAfter != null) {
                                ctMethod.insertAfter(insertAfter);
                            }
                        }
                    } else {
                        CtMethod ctMethod = ctClass.getDeclaredMethod(method);
                        if (insertBefore != null) {
                            ctMethod.insertBefore(insertBefore);
                        }
                        if (insertAfter != null) {
                            ctMethod.insertAfter(insertAfter);
                        }
                    }
                } else if (constructor != null) {
                    if ("*".equals(constructor)) {
                        CtConstructor[] ctConstructors = ctClass.getConstructors();
                        for (CtConstructor ctConstructor : ctConstructors) {
                            if (insertBefore != null) {
                                ctConstructor.insertBefore(insertBefore);
                            }
                            if (insertAfter != null) {
                                ctConstructor.insertAfter(insertAfter);
                            }
                        }
                    } else {
                        CtConstructor ctConstructor = ctClass.getConstructor(constructor);
                        if (insertBefore != null) {
                            ctConstructor.insertBefore(insertBefore);
                        }
                        if (insertAfter != null) {
                            ctConstructor.insertAfter(insertAfter);
                        }
                    }
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
e.printStackTrace(System.err);
                return null;
            }
        } else {
            return null;
        }
    }
}

/* */
