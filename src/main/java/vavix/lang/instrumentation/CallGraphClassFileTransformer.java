/*
 * Copyright (c) 2011 by Naohide Sano, All rights reserved.
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
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * CallGraphClassFileTransformer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 110829 nsano initial version <br>
 */
public class CallGraphClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private Pattern pattern;

    /** */
    private static final String prefix = CallGraphClassFileTransformer.class.getName();

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
     * <pre>
     * system environment
     *
     * vavix.lang.instrumentation.CallGraphClassFileTransformer.${id}.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (pattern == null) {
            Properties props = System.getProperties();
            try {
                pattern = Pattern.compile(props.getProperty(prefix + "." + id + "." + "pattern"));
            } catch (Exception e) {
System.err.println("CallGraphClassFileTransformer::transform: bad pattern: " + prefix + "." + id + "." + "pattern");
            }
        }

        ClassPool classPool = ClassPool.getDefault();

        Matcher matcher = pattern.matcher(className);
        String key = null;
        if (matcher.matches()) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (int i = 0; i < ctMethods.length; i++) {
                    key = getKey(ctClass, ctMethods[i]);
                    ctMethods[i].insertBefore("{" +
                                              "    vavix.lang.instrumentation.CallGraphClassFileTransformer.CallLogger.INSTANCE.pushMethod(\"" + key + "\");" +
                                              "    vavix.lang.instrumentation.CallGraphClassFileTransformer.CallLogger.INSTANCE.logCall();" +
                                              "}");
                    ctMethods[i].insertAfter("{" +
                                             "    vavix.lang.instrumentation.CallGraphClassFileTransformer.CallLogger.INSTANCE.popMethod();" +
                                             "}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
System.err.println("CallGraphClassFileTransformer::transform: " + key + ": " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    /** */
    public static Set<String> signatures = new HashSet<>();

    /** */
    public static String getKey(CtClass ctClass, CtMethod ctMethod) {
        return ctClass.getName() + "#" + ctMethod.getName() + ctMethod.getSignature();
    }

    public static class CallLogger {

        public static CallLogger INSTANCE = new CallLogger();

        private Stack<String> callStack = new Stack<>();
        private Set<String> callLog = new HashSet<>();

        public void pushMethod(String s) {
            callStack.push(s);
        }

        public void popMethod() {
            callStack.pop();
        }

        public void logCall() {
            if (callStack.size() < 2) {
                return;
            }
            String call = "\"" + top(1) + "\" -> \"" + top(0) + "\"";
            if (!callLog.contains(call)) {
                write(call);
                callLog.add(call);
            }
        }

        private String top(int i) {
            return callStack.get(callStack.size() - (i + 1));
        }

        private void write(String line) {
            System.out.println(line);
            System.out.flush();
        }
    }
}

/* */
