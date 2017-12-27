/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
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

    /**
     * 完全にデバッグコードを取り除く場合は以下を false に
     * してすべてを再コンパイルしてください．
     */
    private static final boolean isDebug = true;
//    private static final boolean isDebug = false;

    //-------------------------------------------------------------------------

    /** デバッグ情報の出力先ストリーム */
    private static Logger logger = Logger.getLogger(Debug.class.getName());

    /** */
    private static final String LOGGING_CONFIG_CLASS = "java.util.logging.config.class";

    /** */
    private static final String LOGGING_CONFIG_FILE = "java.util.logging.config.file";

    /** */
    static {
        String configClass = System.getProperty(LOGGING_CONFIG_CLASS);
        String configFile  = System.getProperty(LOGGING_CONFIG_FILE);
        if (configClass == null && configFile == null) { // TODO configFile 
//System.err.println("Debug::<clinit>: no configuration specified, use default");
            try {
                Properties props = new Properties();
                props.load(Debug.class.getResourceAsStream("/vavi/util/logging/logging.properties"));
                configClass = props.getProperty("vavi.util.debug.config.class");
                Class.forName(configClass).newInstance();
            } catch (Exception e) {
e.printStackTrace(System.err);
            }
        }
    }

    /**
     * アクセスできません．
     */
    private Debug() {}

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    level    このメッセージの表示レベル
     * @param    message    表示メッセージ
     */
    public static final void println(Level level, Object message) {
        if (isDebug) {
            StackTraceElement ste = getStackTraceElement(0);
            logger.logp(level,
                        StringUtil.getClassName(ste.getClassName()),
                        ste.getMethodName(),
                        message + "\n");
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    level    このメッセージの表示レベル
     * @param    message    表示メッセージ
     * @see    #print(Level, Object)
     */
    public static final void println(Level level, boolean message) {
        if (isDebug) {
            println(level, String.valueOf(message));
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    level    このメッセージの表示レベル
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(Level level, int message) {
        if (isDebug) {
            println(level, String.valueOf(message));
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(Object message) {
        if (isDebug) {
            println(Level.INFO, message);
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(int message) {
        if (isDebug) {
            println(Level.INFO, String.valueOf(message));
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(char message) {
        if (isDebug) {
            println(Level.INFO, String.valueOf(message));
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(boolean message) {
        if (isDebug) {
            println(Level.INFO, String.valueOf(message));
        }
    }

    /**
     * 改行付きでメッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void println(double message) {
        if (isDebug) {
            println(Level.INFO, String.valueOf(message));
        }
    }

    /**
     * メッセージを出力します.
     *
     * @param    level    このメッセージの表示レベル
     * @param    message    表示メッセージ
     */
    public static final void print(Level level, Object message) {
        if (isDebug) {
            StackTraceElement ste = getStackTraceElement(0);
            logger.logp(level,
                        StringUtil.getClassName(ste.getClassName()),
                        ste.getMethodName(),
                        String.valueOf(message));
        }
    }

    /**
     * メッセージを出力します．
     *
     * @param    message    表示メッセージ
     * @see    #println(Level, Object)
     */
    public static final void print(Object message) {
        if (isDebug) {
            print(Level.INFO, message);
        }
    }

    /**
     * デバッグモードならスタックトレースを出力します．
     * @param    e    exception
     */
    public static final void printStackTrace(Throwable e) {
        if (isDebug) {
            logger.log(Level.INFO, "Stack Trace", e);
        }
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    public static final void dump(byte[] buf) {
        dump(new ByteArrayInputStream(buf));
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    public static final void dump(byte[] buf, int length) {
        dump(new ByteArrayInputStream(buf), length);
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    public static final void dump(byte[] buf, int offset, int length) {
        dump(new ByteArrayInputStream(buf, offset, length));
    }

    /**
     * ストリームを 16 進数でダンプします．
     */
    public static final void dump(InputStream is) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(StringUtil.getDump(is));
        print(sb.toString());
    }

    /**
     * 制限付でストリームを 16 進数でダンプします．
     * @param length 制限する長さ
     */
    public static final void dump(InputStream is, int length) {
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
        sb.append(StringUtil.getDump(is, length));
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
    public static final String getCallerMethod() {
        return getCallerMethod(0);
    }

    /**
     * 現在実行中のプログラムの情報を取得します．
     * @param    depth    呼び出し元の深さ，0 以上を指定する．
     * 対象メソッドを直接呼ぶ場合は 0，
     * 対象メソッドを呼ぶメソッドを呼ぶ場合は 1，
     * のように指定する。
     */
    public static final String getCallerMethod(int depth) {
        StackTraceElement ste = getStackTraceElement(depth);
        return format(ste);
    }

    /**
     * フォーマットした StackTraceElement の文字列を返します．
     * @param    ste    StackTraceElement
     */
    private static final String format(StackTraceElement ste) {
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
    private static final StackTraceElement getStackTraceElement(int depth) {
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
    public static final String getTopCallerMethod(String packageName) {

        Throwable t = new Throwable();
        StackTraceElement[] stes = t.getStackTrace();

        for (int i = 0; i < stes.length; i++) {
            if (stes[i].getClassName().startsWith(packageName)) {
                return format(stes[i]);
            }
        }

        return "no such package name: " + packageName;
    }
}

/* */
