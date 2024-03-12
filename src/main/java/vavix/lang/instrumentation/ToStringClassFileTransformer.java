/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
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
import javassist.CtNewMethod;


/**
 * ToStringClassFileTransformer.
 *
 * <pre>
 * java \
 * -cp target/classes \
 * -Xbootclasspath/a:${HOME}/.m2/repository/org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar \
 * -javaagent:vavi-instrumentation.jar \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.4=vavix.lang.instrumentation.ToStringClassFileTransformer \
 * -Dvavix.lang.instrumentation.ToStringClassFileTransformer.4.pattern=target/package/name/pattern/.+ \
 * -Dvavix.lang.instrumentation.ToStringClassFileTransformer.4.body="{ return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this); }" \
 * your.MainClass
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 190502 nsano initial version <br>
 */
public class ToStringClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private Pattern pattern;

    /** */
    private String body;

    /** */
    private static final String prefix = ToStringClassFileTransformer.class.getName();

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
     * vavix.lang.instrumentation.ToStringClassFileTransformer.${id}.pattern ... class name matcher in regex
     * vavix.lang.instrumentation.ToStringClassFileTransformer.${id}.body ... method body
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (pattern == null) {
            Properties props = System.getProperties();
            try {
                pattern = Pattern.compile(props.getProperty(prefix + "." + id + "." + "pattern"));
            } catch (Exception e) {
System.err.println("ToStringClassFileTransformer::transform: bad pattern: " + prefix + "." + id + "." + "pattern");
            }
            try {
                body = props.getProperty(prefix + "." + id + "." + "body");
            } catch (Exception e) {
System.err.println("ToStringClassFileTransformer::transform: some thing wrong: " + prefix + "." + id + "." + "body");
            }
        }

        ClassPool classPool = ClassPool.getDefault();

        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
            try {
//System.err.println("ToStringClassFileTransformer::transform: matched: " + className);
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod method = CtNewMethod.make("public String toString() " + body, ctClass);
                ctClass.addMethod(method);

                return ctClass.toBytecode();
            } catch (Exception e) {
System.err.println("ToStringClassFileTransformer::transform: " + className + ": " + e);
                return null;
            }
        } else {
            return null;
        }
    }
}
