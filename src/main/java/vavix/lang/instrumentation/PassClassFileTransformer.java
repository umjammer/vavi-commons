/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * Traces a method has been passed or not.
 *
 * <pre>
 * java \
 * -cp target/classes \
 * -javaagent:vavi-instrumentation.jar \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.1=vavix.lang.instrumentation.PassClassFileTransformer \
 * -Dvavix.lang.instrumentation.PassClassFileTransformer.1.pattern=your\\/package\\/.+ \
 * your.package.YourMainClass
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 110823 nsano initial version <br>
 */
public class PassClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private Pattern pattern;

    /** */
    private static boolean normalize;

    /** */
    private static boolean cleaning;

    /** */
    private boolean duplication;

    /** */
    private static final String prefix = PassClassFileTransformer.class.getName();

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

    /**
     * system environment
     * <pre>
     * vavix.lang.instrumentation.PassClassFileTransformer.cleaning ... boolean value ignore synthetic method, switch table or not (true)
     * vavix.lang.instrumentation.PassClassFileTransformer.normalize ... boolean value normalizing method name or not (false)
     * vavix.lang.instrumentation.PassClassFileTransformer.${id}.duplication ... show duplication or not (false)
     * vavix.lang.instrumentation.PassClassFileTransformer.${id}.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (pattern == null) {
            Properties props = System.getProperties();
            try {
                pattern = Pattern.compile(props.getProperty(prefix + "." + id + "." + "pattern"));
            } catch (Exception e) {
System.err.println("PassClassFileTransformer::transform: bad pattern: " + prefix + "." + id + "." + "pattern");
            }
            normalize = Boolean.parseBoolean(props.getProperty(prefix + "." + "normalize", "false"));
            cleaning = Boolean.parseBoolean(props.getProperty(prefix + "." + "cleaning", "true"));
            duplication = Boolean.parseBoolean(props.getProperty(prefix + "." + "duplication", "false"));
        }

        ClassPool classPool = ClassPool.getDefault();

        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();

                for (CtMethod ctMethod : ctMethods) {
                    if (cleaning && !available(ctMethod.getName())) {
                        continue;
                    }
                    String key = getKey(ctClass, ctMethod);
                    try {
                        if (duplication) {
                            ctMethod.insertBefore(
                                    "{" +
                                            "    System.err.println(\"" + key + "\");" +
                                            "}");
                        } else {
                            ctMethod.insertBefore(
                                    "{" +
                                            "    if (!vavix.lang.instrumentation.PassClassFileTransformer.signatures.contains(\"" + key + "\")) {" +
                                            "        System.err.println(\"" + key + "\");" +
                                            "        vavix.lang.instrumentation.PassClassFileTransformer.signatures.add(\"" + key + "\");" +
                                            "    }" +
                                            "}");
                        }
                    } catch (Exception e) {
                        System.err.println("PassClassFileTransformer::transform: " + key + ": " + e.getMessage());
                    }
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
System.err.println("PassClassFileTransformer::transform: " + className + ": " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    /** @see #cleaning */
    private static boolean available(String methodName) {
        if (methodName.startsWith("access$") ||
            methodName.startsWith("$SWITCH_TABLE$")
        ) {
            return false;
        } else {
            return true;
        }
    }

    /** */
    public static Set<String> signatures = new HashSet<>();

    /** */
    public static String getKey(CtClass ctClass, CtMethod ctMethod) {
        if (normalize) {
            return normalize(ctClass.getName()) + "#" + ctMethod.getName() + ctMethod.getSignature();
        } else {
            return ctClass.getName() + "#" + ctMethod.getName() + ctMethod.getSignature();
        }
    }

    static String normalize(String name) {
        if (name.indexOf('.') > 0 && name.lastIndexOf('$') > 0) {
            String packageName = name.substring(0, name.lastIndexOf('.'));
            String simpleName = name.substring(name.lastIndexOf('$') + 1);
            return packageName + '.' + simpleName;
        } else {
            return name;
        }
    }
}

/* */
