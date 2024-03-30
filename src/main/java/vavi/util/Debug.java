/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is a debugging utility class.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010821 nsano initial version <br>
 *          0.01 010827 nsano deplecete #assert <br>
 *          0.02 010827 nsano add #getCallerMethod <br>
 *          0.03 010827 nsano add "debug.level" property <br>
 *          0.04 010829 nsano add #getTopCallerMethod <br>
 *          0.05 010906 nsano be public #out <br>
 *          0.06 010918 nsano avoid SecurityException at #init <br>
 *          0.07 020423 nsano add generic #toBits <br>
 *          0.08 020927 nsano add #setOut <br>
 *          0.09 020927 nsano add #dump(byte) <br>
 *          0.10 020927 nsano add #dump(String) <br>
 *          1.00 021027 nsano use logging package <br>
 *          1.01 021027 nsano delete logging unrelated <br>
 *          1.02 021027 nsano why logger has been public ??? <br>
 *          1.03 030322 nsano fix print without \n <br>
 *          1.04 030825 nsano dump length supported <br>
 *          1.05 040102 nsano add #println(char) <br>
 *          1.06 040118 nsano add #dump(byte[], int, int) <br>
 */
public final class Debug {

    /** Debug information output stream */
    private static final Logger logger = Logger.getLogger(Debug.class.getName()); // TODO fixed namespace

    /**
     * can not access.
     */
    private Debug() {}

    /** */
    public static boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    /**
     * Outputs the message with a newline.
     *
     * @param level display level of this message
     * @param message display message
     */
    public static void println(Level level, Object message) {
        print(level, message + "\n");
    }

    /**
     * Outputs the message with a newline.
     *
     * @param level display level of this message
     * @param message display message
     * @see #print(Level, Object)
     */
    public static void println(Level level, boolean message) {
        println(level, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param level display level of this message
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(Level level, int message) {
        println(level, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(Object message) {
        println(Level.INFO, message);
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(int message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(char message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(boolean message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(double message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(float message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(long message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs the message with a newline.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void println(byte message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * Outputs new line.
     *
     * @see #println(Level, Object)
     */
    public static void println() {
        println(Level.INFO);
    }

    /**
     * Formats and output the message.
     *
     * @param message display message
     */
    public static void printf(String message, Object... args) {
        printf(Level.INFO, message, args);
    }

    /**
     * Formats and output the message.
     *
     * @param message display message
     */
    public static void printf(Level level, String message, Object... args) {
        print(level, String.format(message, args));
    }

    /**
     * Outputs the message.
     *
     * @param level display level of this message
     * @param message display message
     */
    public static void print(Level level, Object message) {
        StackTraceElement ste = getStackTraceElement(0);
        logger.logp(level,
                    StringUtil.getClassName(ste.getClassName()),
                    ste.getMethodName(),
                    String.valueOf(message));
    }

    /**
     * Outputs the message.
     *
     * @param message display message
     * @see #println(Level, Object)
     */
    public static void print(Object message) {
        print(Level.INFO, message);
    }

    /**
     * In debug mode, a stack trace will be output.
     * @param e exception
     */
    public static void printStackTrace(Throwable e) {
        printStackTrace(Level.INFO, e);
    }

    /**
     * In debug mode, a stack trace will be output.
     * @param level display level of this stack trace
     * @param e exception
     */
    public static void printStackTrace(Level level, Throwable e) {
        logger.log(level, e.getMessage(), e);
    }

    /**
     * Dumps a byte array in hexadecimal.
     */
    @Deprecated
    public static void dump(byte[] buf) {
        dump(new ByteArrayInputStream(buf));
    }

    /**
     * Dumps a byte array in hexadecimal.
     */
    @Deprecated
    public static void dump(byte[] buf, int length) {
        dump(buf, 0, length);
    }

    /**
     * Dumps a byte array in hexadecimal.
     */
    @Deprecated
    public static void dump(byte[] buf, int offset, int length) {
        dump(new ByteArrayInputStream(buf, offset, length));
    }

    /**
     * Dump the stream in hexadecimal.
     */
    @Deprecated
    public static void dump(InputStream is) {
        print("\n" + StringUtil.getDump(is));
    }

    /**
     * Dumps the stream in hexadecimal with limitations.
     * @param length limiting length
     */
    @Deprecated
    public static void dump(InputStream is, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("dumped ");
        try {
            int available = is.available();
            sb.append(Math.min(length, available));
            sb.append("/");
            sb.append(available);
        }
        catch (IOException e) {
            sb.append(length);
        }
        sb.append(" bytes limied...\n");
        sb.append(StringUtil.getDump(is, 0, length));
        print(sb.toString());
    }

    /**
     * Gets information about the currently running program.
     * <p>
     * Example of use:
     * <pre>
     * <tt>
     * Debug.println("The calling method is " + Debug.getCallerMethod())
     * </tt>
     * </pre>
     * <p>
     */
    public static String getCallerMethod() {
        return getCallerMethod(0);
    }

    /**
     * Gets information about the currently running program.
     * @param depth specify the depth of the caller, 0 or more.
     *              specify as follows,
     *              0 if calling the target method directly,
     *              1 to call a method that calls the target method,
     */
    public static String getCallerMethod(int depth) {
        StackTraceElement ste = getStackTraceElement(depth);
        return format(ste);
    }

    /**
     * Returns a formatted string of StackTraceElement.
     * @param ste StackTraceElement
     */
    private static String format(StackTraceElement ste) {
        String sb =
//                StringUtil.getClassName(ste.getClassName()) +
//                "::" +
//                ste.getMethodName() +
//                "(" +
//                ste.getLineNumber() +
//                ")" +
                ste.getClassName() +
                "." +
                ste.getMethodName() +
                "(" +
                ste.getFileName() +
                ":" +
                ste.getLineNumber() +
                ")";
        return sb;
    }

    /**
     * Returns the calling method of this class.
     */
    private static StackTraceElement getStackTraceElement(int depth) {
        Throwable t = new Throwable();
        StackTraceElement[] stes = t.getStackTrace();
//  System.err.println("----");
//  for (int i = 0; i < stes.length; i++) {
//  System.err.println(stes[i]);
//  }
//  System.err.println("----");
        for (int i = stes.length - 2; i >= 0 ; i--) {
            if (stes[i].getClassName().startsWith(Debug.class.getName())) {
                return stes[i + depth + 1];
            }
        }

        return stes[stes.length - 1];
    }

    /**
     * Returns the top-level string of the calling method with the specified package name.
     * Using the <code>vavi.xxx</code> package as an example,
     * specify <code>getTopCallerMethod("vavi")</code>.
     */
    public static String getTopCallerMethod(String packageName) {

        Throwable t = new Throwable();
        StackTraceElement[] stes = t.getStackTrace();

        for (StackTraceElement ste : stes) {
            if (ste.getClassName().startsWith(packageName)) {
                return format(ste);
            }
        }

        return "no such package name: " + packageName;
    }
}
