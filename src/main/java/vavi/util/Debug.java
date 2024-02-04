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
 * デバッグのユーティリティクラスです．
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

    //-------------------------------------------------------------------------

    /** デバッグ情報の出力先ストリーム */
    private static Logger logger = Logger.getLogger(Debug.class.getName()); // TODO fixed namespace

    /**
     * アクセスできません．
     */
    private Debug() {}

    /** */
    public static boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param level このメッセージの表示レベル
     * @param message 表示メッセージ
     */
    public static void println(Level level, Object message) {
        print(level, message + "\n");
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param level このメッセージの表示レベル
     * @param message 表示メッセージ
     * @see #print(Level, Object)
     */
    public static void println(Level level, boolean message) {
        println(level, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param level このメッセージの表示レベル
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(Level level, int message) {
        println(level, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(Object message) {
        println(Level.INFO, message);
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(int message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(char message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(boolean message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(double message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(float message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void println(long message) {
        println(Level.INFO, String.valueOf(message));
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param message 表示メッセージ
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
     * フォーマットしてメッセージを出力します．
     *
     * @param message 表示メッセージ
     */
    public static void printf(String message, Object... args) {
        printf(Level.INFO, message, args);
    }

    /**
     * フォーマットしてメッセージを出力します．
     *
     * @param message 表示メッセージ
     */
    public static void printf(Level level, String message, Object... args) {
        print(level, String.format(message, args));
    }

    /**
     * メッセージを出力します.
     *
     * @param level このメッセージの表示レベル
     * @param message 表示メッセージ
     */
    public static void print(Level level, Object message) {
        StackTraceElement ste = getStackTraceElement(0);
        logger.logp(level,
                    StringUtil.getClassName(ste.getClassName()),
                    ste.getMethodName(),
                    String.valueOf(message));
    }

    /**
     * メッセージを出力します．
     *
     * @param message 表示メッセージ
     * @see #println(Level, Object)
     */
    public static void print(Object message) {
        print(Level.INFO, message);
    }

    /**
     * デバッグモードならスタックトレースを出力します．
     * @param e exception
     */
    public static void printStackTrace(Throwable e) {
        printStackTrace(Level.INFO, e);
    }

    /**
     * デバッグモードならスタックトレースを出力します．
     * @param level
     * @param e exception
     */
    public static void printStackTrace(Level level, Throwable e) {
        logger.log(level, e.getMessage(), e);
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    @Deprecated
    public static void dump(byte[] buf) {
        dump(new ByteArrayInputStream(buf));
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    @Deprecated
    public static void dump(byte[] buf, int length) {
        dump(buf, 0, length);
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    @Deprecated
    public static void dump(byte[] buf, int offset, int length) {
        dump(new ByteArrayInputStream(buf, offset, length));
    }

    /**
     * ストリームを 16 進数でダンプします．
     */
    @Deprecated
    public static void dump(InputStream is) {
        print("\n" + StringUtil.getDump(is));
    }

    /**
     * 制限付でストリームを 16 進数でダンプします．
     * @param length 制限する長さ
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
     * 現在実行中のプログラムの情報を取得します．
     * <p>
     * 使用例：
     * <pre>
     * <tt>
     * Debug.println("呼び元のメソッドは" + Debug.getCallerMethod() + "です");
     * </tt>
     * </pre>
     * <p>
     */
    public static String getCallerMethod() {
        return getCallerMethod(0);
    }

    /**
     * 現在実行中のプログラムの情報を取得します．
     * @param depth 呼び出し元の深さ，0 以上を指定する．
     * 対象メソッドを直接呼ぶ場合は 0，
     * 対象メソッドを呼ぶメソッドを呼ぶ場合は 1，
     * のように指定する。
     */
    public static String getCallerMethod(int depth) {
        StackTraceElement ste = getStackTraceElement(depth);
        return format(ste);
    }

    /**
     * フォーマットした StackTraceElement の文字列を返します．
     * @param ste StackTraceElement
     */
    private static String format(StackTraceElement ste) {
        StringBuilder sb = new StringBuilder();
//    sb.append(StringUtil.getClassName(ste.getClassName()));
//    sb.append("::");
//    sb.append(ste.getMethodName());
//    sb.append("(");
//    sb.append(ste.getLineNumber());
//    sb.append(")");
        sb.append(ste.getClassName());
        sb.append(".");
        sb.append(ste.getMethodName());
        sb.append("(");
        sb.append(ste.getFileName());
        sb.append(":");
        sb.append(ste.getLineNumber());
        sb.append(")");
        return sb.toString();
    }

    /**
     * このクラスのの呼び出し元メソッドを返します．
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
     * 指定されたパッケージ名の呼び出し元メソッドの最上位の文字列を返します．
     * <code>vavi.xxx</code> パッケージを例とすれば <code>
     * getTopCallerMethod("vavi")</code> と指定します．
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

/* */
