/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.lang.instrument.Instrumentation;
import java.util.Enumeration;
import java.util.Properties;


/**
 * VaviInstrumentation.
 *
 * <pre>
 * java \
 * -cp target/classes \
 * -Xbootclasspath/a:${HOME}/.m2/repository/javassist/javassist/3.8.0.GA/javassist-3.8.0.GA.jar \
 * -javaagent:vavi-instrumentation.jar \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.1=vavix.lang.instrumentation.PassClassFileTransformer \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.2=vavix.lang.instrumentation.EnterExitClassFileTransformer \
 * -Dvavix.lang.instrumentation.VaviInstrumentation.3=vavix.lang.instrumentation.ToStringClassFileTransformer \
 * -Dvavix.lang.instrumentation.PassClassFileTransformer.1.pattern=your\\/package\\/.+ \
 * your.package.YourMainClass
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class VaviInstrumentation {

    /** */
    private static final String prefix = VaviInstrumentation.class.getName();

    /**
     * Specify a class that implements {@link VaviClassFileTransformer} for properties with names
     * starting with "vavix.lang.instrumentation.VaviInstrumentation." in System Properties.
     * Anything after "." is used as an identifier.
     * <pre>
     *  vavix.lang.instrumentation.VaviInstrumentation.0=vavix.lang.instrumentation.PassClassFileTransformer
     * </pre>
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        Properties props = System.getProperties();
        Enumeration<?> e = props.propertyNames();
        while (e.hasMoreElements()) {
            try {
                String key = (String) e.nextElement();
//System.err.println("VaviInstrumentation::premain: key: " + key);
                if (key.startsWith(prefix + ".")) {
                    String id = key.substring(prefix.length() + 1);
                    String cftClassName = props.getProperty(key);
//System.err.println("VaviInstrumentation::premain: cftClassName: " + cftClassName);
                    Class<?> cftClass = Class.forName(cftClassName);
                    VaviClassFileTransformer vcft = (VaviClassFileTransformer) cftClass.getDeclaredConstructor().newInstance();
                    vcft.setId(id);
                    instrumentation.addTransformer(vcft);
                }
            } catch (Exception f) {
                f.printStackTrace(System.err);
            }
        }
    }
}
