/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文字列に関するユーティリティのクラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021027 nsano separated from Debug class <br>
 *          0.01 021202 nsano fix getDump(InputStream) <br>
 *          0.02 021206 nsano fix toBits(int,int) <br>
 *          0.03 021206 nsano use properties file <br>
 *          0.04 021207 nsano fix toBits(int,int) when 32bit <br>
 *          0.05 021216 nsano add getCharDump() <br>
 *          0.06 030128 nsano add paramString() <br>
 *          0.07 030211 nsano add toHex16(long) <br>
 *          0.08 030817 nsano add display <br>
 *          0.09 030817 nsano getDump mark supported <br>
 *          0.10 030825 nsano getDump length supported <br>
 *          0.11 031212 nsano add recursive flag, paramString <br>
 *          0.12 031212 nsano rename display to expand<br>
 *          0.13 031228 nsano add when null, paramString <br>
 *          0.14 040118 nsano add splitCommandLine <br>
 *          0.15 040119 nsano add isHexUpperCase property <br>
 *          0.16 040119 nsano add ignoreStatics property <br>
 *          0.17 040212 nsano add ignoreInnerClasses property <br>
 */
public final class StringUtil {

    /** */
    private StringUtil() {}

    /**
     * 指定したオブジェクトのフィールドの文字列を取得します。
     * @param object 表示するオブジェクト
     */
    public static String paramString(Object object) {
        hashes.clear();
        return paramString(object, 0);
    }

    /**
     * 指定したオブジェクトのフィールドの文字列を表示します。
     * フィールドがオブジェクトなら再帰的に取得します。
     * @param object 表示するオブジェクト
     */
    public static String paramStringDeep(Object object, int depth) {
        hashes.clear();
        return paramString(object, depth);
    }

    /**
     * オブジェクトをテキストダンプします。
     * @param object 表示するオブジェクト
     * @param depth 再帰的の深さ、0 の場合は再帰しません。
     */
    private static String paramString(Object object, int depth) {

        // null
        if (object == null) {
            return null;
        }

        Class<?> clazz = object.getClass();
//Debug.println("class: " + clazz);

        return paramString(clazz, object, depth);
    }

    /** {hashCode, loopCount} */
    private static Map<Integer, Integer> hashes = new HashMap<>();

    /**
     * オブジェクトをテキストダンプします。
     * toString() で使用すると便利です。
     * @param clazz オブジェクトを指定したクラスとして扱う
     * @param object 表示するオブジェクト
     * @param depth 再帰的の深さ、0 の場合は再帰しません。
     */
    private static String paramString(Class<?> clazz,
                                      Object object,
                                      int depth) {

        // 表示しないもの
        if (isIgnored(clazz)) {
            return "";
        }

        // 展開しないものはそのまま
        if (isNotExpanded(clazz)) {
            return String.valueOf(object);
        }

        // TODO check
        if (hashes.containsKey(object.hashCode())) {
            if (hashes.get(object.hashCode()) == 3) {
                return object.toString();
            } else {
                hashes.put(object.hashCode(), hashes.get(object.hashCode()) + 1);
            }
        } else {
            hashes.put(object.hashCode(), 0);
        }

        // 配列やコレクションなら展開する TODO 配列はまだ...
        if (object instanceof List<?>) {
            return expand((List<?>) object);
        } else if (object instanceof Set<?>) {
            return expand((Set<?>) object);
        } else if (object instanceof Map<?, ?>) {
            return expand((Map<?, ?>) object);
        } else if (object instanceof int[]) {
            return expand((int[]) object);
        } else if (object instanceof byte[]) {
            return expand((byte[]) object);
        } else if (object instanceof Object[]) {
            return expand((Object[]) object);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(clazz.getName());
        sb.append("{");

        // private メソッド、フィールドの取得には getDeclared...
        // を使います。
        Field[] fields = clazz.getDeclaredFields();
// Debug.println("fialds: " + fields.length);
        boolean isFirst = true;

        for (Field field : fields) {
            // we need to accessible flag true, when accessing private field
            // jdk9+ cause java.lang.reflect.InaccessibleObjectException?
            field.setAccessible(true);

            String name = field.getName();
// Debug.println("fiald name: " + name);
            Object value = null;
            try {
                if (depth > 0) {
                    if (depth <= recursiveDepth) {
                        value = paramString(field.get(object), depth + 1);
                    } else {
                        value = paramString(field.get(object), 0);
                    }
                } else {
                    value = field.get(object);
                }
            } catch (IllegalAccessException e) {
                value = "*";
            }
// Debug.println("field: " + name + ": type: " + field.getDeclaringClass() + ": " + (value != null ? value.getClass() : null));

            int modifiers = field.getModifiers();
            if (ignoredStatics && Modifier.isStatic(modifiers)) {
                // ignore static
// Debug.println("ignore statics: " + modifiers);
            } else if (ignoredFinals && Modifier.isFinal(modifiers)) {
                // ignore final
//         if (name.startsWith("this$")) {
                // static でない inner class の outer class オブジェクト
// Debug.println("value: " + value);
//             } else {
// Debug.println("ignore final: " + modifiers);
//             }
            } else if (ignoredInnerClasses &&
//                       value != null &&
                    (value instanceof Class<?> || value instanceof String) &&
                    name.startsWith("class$")) {
                // ignore inner classes TODO `class$` may depend on SUN j2se
// Debug.println("ignore innerclass: " + value.getClass());
            } else {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(",");
                }
                sb.append(name);
                sb.append("=");
                // TODO if (formattable) {
                if (value instanceof Calendar) {
                    sb.append(new SimpleDateFormat().format(((Calendar) value).getTime()));
                } else if (value instanceof Date) {
                    sb.append(new SimpleDateFormat().format((Date) value));
                } else {
                    sb.append(value);
                }
            }
        }

        if (!ignoredSuperClass) {
            // dig into super class
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !isNotExpanded(superClass)) {
//              String name = getClassName(superClass);
                Object value = null;
                if (depth > 0 && depth <= recursiveDepth + 1) {
                    value = paramString(superClass, object, depth - 1);
                } else {
//Debug.println("depth: " + depth + ", super: " + superClass + ", object: " + object + ", recursiveDepth: " + recursiveDepth);
                    value = paramString(superClass, object, recursiveDepth + 1);
                }

                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(",");
                }
                sb.append("super");
                sb.append("=");
                sb.append(value);
            } else {
                // clazz is Object or interface or primitives or void
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * if a class marked as expandable or not.
     * @see "StringUtil.properties#vavi.util.StringUtil.isNotExpanded.*"
     */
    private static boolean isNotExpanded(Class<?> clazz) {
//Debug.println(clazz + ": " + classesIgnored.contains(clazz.getName()));
        return classesNotExpanded.contains(clazz.getName());
    }

    /**
     * if a class marked as visible or not.
     * @see "StringUtil.properties#vavi.util.StringUtil.isIgnored.*"
     */
    private static boolean isIgnored(Class<?> clazz) {
//Debug.println(clazz + ": " + classesIgnored.contains(clazz.getName()));
        return classesIgnored.contains(clazz.getName());
    }

    /**
     * パッケージ名を取り除いたクラス名を取得します．
     *
     * @param name パッケージ付きのクラス名
     * @return パッケージ名を取り除いたクラス名
     * @deprecated use {@link Class#getSimpleName()}
     */
    public static String getClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * パッケージ名を取り除いたクラス名を取得します．
     *
     * @param clazz クラス
     * @return パッケージ名を取り除いたクラス名
     * @deprecated use {@link Class#getSimpleName()}
     */
    @Deprecated
    public static String getClassName(Class<?> clazz) {
        return getClassName(clazz.getName());
    }

    /**
     * expand array string.
     * TODO deep flag
     * @deprecated use {@link java.util.Arrays#toString}
     */
    @Deprecated
    public static String expand(int[] array) {
        Integer[] objectArray = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            objectArray[i] = array[i];
        }
        return expand(objectArray);
    }

    /**
     * 配列を展開します．
     * @see #bytesExpand 展開する最大値
     * TODO deep flag
     */
    @Deprecated
    public static String expand(byte[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(array.length);
        sb.append("]");
        sb.append("{");
        for (int i = 0; i < Math.min(array.length, bytesExpand); i++) {
            sb.append(getPrintableChar((char) array[i]));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 配列を展開します．
     * TODO deep flag
     */
    @Deprecated
    public static String expand(Object[] array) {
        StringBuilder sb = new StringBuilder(array.getClass().getName());
        sb.append("[");
        sb.append(array.length);
        sb.append("]");
        sb.append("{");
        for (Object o : array) {
            sb.append(paramString(o, 1));
            sb.append(",");
        }
        if (array.length > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * List を展開します．
     * <pre>
     * className[collectionSize]{value0,value1,value2,...}
     * </pre>
     * TODO deep flag
     * @deprecated use {@link Object#toString()}
     */
    @Deprecated
    public static String expand(List<?> list) {
//Debug.println(list.getClass() + ", " + list.hashCode());
        StringBuilder sb = new StringBuilder(list.getClass().getName());
        sb.append("[");
        sb.append(list.size());
        sb.append("]");
        sb.append("{");
        for (Object object : list) {
            //Debug.println(object.getClass() + ", " + object.hashCode());
            sb.append(paramString(object, 0));
            sb.append(",");
        }
        if (list.size() > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Set を展開します．
     * <pre>
     * className[collectionSize]{value0,value1,value2,...}
     * </pre>
     * TODO deep flag
     * @deprecated use {@link Object#toString()}
     */
    @Deprecated
    public static String expand(Set<?> set) {
        StringBuilder sb = new StringBuilder(set.getClass().getName());
        sb.append("[");
        sb.append(set.size());
        sb.append("]");
        sb.append("{");
        for (Object object : set) {
            sb.append(paramString(object, 1));
            sb.append(",");
        }
        if (set.size() > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Map を展開します．
     * <pre>
     * className[collectionSize]{key0=value0,key1=value1,key2=value2,...}
     * </pre>
     * TODO deep flag
     * @deprecated use {@link Object#toString()}
     */
    @Deprecated
    public static String expand(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder(map.getClass().getName());
        sb.append("[");
        sb.append(map.size());
        sb.append("]");
        sb.append("{");
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            sb.append(paramString(key, 1)).append("=").append(paramString(value, 1));
            sb.append(",");
        }
        if (map.size() > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * バイト配列を 16 進数でダンプします．
     */
    public static String getDump(byte[] buf) {
        return getDump(buf, 0, buf.length);
    }

    /**
     * Dumps double array.
     */
    public static String getDump(double[] buf, int length) {
        return getDump(buf, 0, length);
    }

    /**
     * 長さ制限付でバイト配列を 16 進数でダンプします．
     */
    public static String getDump(byte[] buf, int length) {
        return getDump(buf, 0, length);
    }

    /**
     * Dumps double array limited by length.
     */
    public static String getDump(double[] buf, int offset, int length) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = offset; i < Math.min(offset + length, buf.length - offset); i++) {
                baos.write(ByteUtil.getBeBytes(Double.doubleToRawLongBits(buf[i])));
            }
            return getDump(new ByteArrayInputStream(baos.toByteArray()), 0, baos.size());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Dumps double array limited by length.
     */
    public static String getNumberDump(double[] buf, int length) {
        return getNumberDump(buf, 0, length);
    }

    /**
     * Dumps double array limited by length.
     */
    public static String getNumberDump(double[] buf, int offset, int length) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        for (int i = offset; i < Math.min(offset + length, buf.length - offset); i++) {
            ps.printf("%015.9f ", buf[i]);
            if (i % 8 == 7) {
                ps.println();
            }
        }
        return baos.toString();
    }

    /**
     * 長さ制限付で offset からのバイト配列を 16 進数でダンプします．
     */
    public static String getDump(byte[] buf, int offset, int length) {
        return getDump(new ByteArrayInputStream(buf), offset, length);
    }

    /** TODO make '.' property */
    private static char getPrintableChar(char c) {
        return isPrintableChar(c) ? c : '.';
    }

    /**
     * Creates byte array declaration string.
     */
    public static String getByteArrayDeclaration(byte[] bytes) {
        return getByteArrayDeclaration(new ByteArrayInputStream(bytes));
    }
    /**
     * Creates byte array declaration string.
     */
    public static String getByteArrayDeclaration(InputStream is) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        try {
            if (is.markSupported()) {
                is.mark(is.available());
            }

            boolean breakFlag = false;

top:
            while (true) {
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        int c = is.read();
                        if (c == -1) {
                            if (!breakFlag) {
                                break top;
                            }
                        } else {
                            if (c > 127) {
                                ps.print("(byte) ");
                            }
                            ps.print("0x");
                            ps.printf("%02X, ", c);
                        }
                    }
                    ps.println();
                }
                ps.println();
            }
        } catch (IOException e) {
Debug.printStackTrace(e);
        } finally {
            if (is.markSupported()) {
                try {
                    is.reset();
                } catch (IOException e) {
Debug.printStackTrace(e);
                }
            }
        }

        return baos.toString();
    }

    /**
     * Dumps a stream.
     */
    public static String getDump(InputStream is) {
        return getDump(is, 0, Integer.MAX_VALUE);
    }

    /**
     * 長さ制限付でストリームを 16 進数でダンプします．
     *
     * @param length 制限する長さ
     */
    public static String getDump(InputStream is, int offset, int length) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        try {
            long l = 0;
            while (l < offset) {
                long r = is.skip(offset);
                l += r;
            }

            int count = 0;

            if (is.markSupported()) {
                is.mark(is.available());
            } else {
Debug.println(Level.FINER, "mark is not supported");
                is = new BufferedInputStream(is);
                is.mark(is.available());
            }

            byte[] buf = new byte[16];
            boolean breakFlag = false;
            int m;

            while (true) {
                for (int y = 0; y < 16; y++) {
                    m = -1;
                    for (int x = 0; x < 16; x++) {
                        int c = is.read();
                        count++;
                        if (c == -1 || count > length) {
                            if (!breakFlag) {
                                breakFlag = true;
                                m = x;
                            }
                            if (m > 0) {
                                ps.print("   ");
                            } else {
                                break;
                            }
                        } else {
                            ps.printf("%02X ", c);
                            buf[x] = (byte) c;
                        }
                    }
                    if (m != 0) {
                        ps.print("   ");
                    }
                    for (int x = 0; x < 16; x++) {
                        if (breakFlag && x == m) {
                            break;
                        } else {
                            ps.print(getPrintableChar((char) buf[x]));
                        }
                    }
                    if (breakFlag || count == length) {
                        return baos.toString();
                    }
                    ps.println();
                }
                ps.println();
            }
        } catch (IOException e) {
Debug.printStackTrace(e);
            return null;
        } finally {
            if (is.markSupported()) {
                try {
                    is.reset();
                } catch (IOException e) {
Debug.printStackTrace(e);
                }
            }
        }
    }

    /**
     * 文字列を 16 進数でダンプします．
     */
    public static String getDump(String s) {
        StringBuilder sb = new StringBuilder();
        int l = s.length();
        for (int i = 0; i < l; i++) {
            sb.append(toHex4(s.charAt(i)));
            sb.append(i == l - 1 ? "" : " ");
        }
        return sb.toString();
    }

    /**
     * 文字列を 16 進数でダンプします．
     */
    public static String getDump(String s, String encoding) {
        try {
            byte[] b = s.getBytes(encoding);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                sb.append(toHex2(b[i]));
                sb.append(i == b.length - 1 ? "" : " ");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
e.printStackTrace(System.err);
            return getDump(s);
        }
    }

    /**
     * 文字列を char でダンプします．
     */
    public static String getCharDump(String s) {
        StringBuilder sb = new StringBuilder();
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            sb.append("   ");
            sb.append(!Character.isISOControl(c) ? c : '.');
            sb.append(i == l - 1 ? "" : " ");
        }
        return sb.toString();
    }

    /**
     * 先頭を 0 で埋めた 2 桁の大文字の 16 進数を返します．
     */
    @Deprecated
    public static String toHex2(int i) {
        String s = "0" + Integer.toHexString(i);
        s = s.substring(s.length() - 2);
        return hexUpperCase ? s.toUpperCase() : s;
    }

    /**
     * 先頭を 0 で埋めた 4 桁の大文字の 16 進数を返します．
     */
    @Deprecated
    public static String toHex4(int i) {
        String s = "000" + Integer.toHexString(i);
        s = s.substring(s.length() - 4);
        return hexUpperCase ? s.toUpperCase() : s;
    }

    /**
     * 先頭を 0 で埋めた 8 桁の大文字の 16 進数を返します．
     */
    @Deprecated
    public static String toHex8(int i) {
        String s = "0000000" + Integer.toHexString(i);
        s = s.substring(s.length() - 8);
        return hexUpperCase ? s.toUpperCase() : s;
    }

    /**
     * 先頭を 0 で埋めた 16 桁の大文字の 16 進数を返します．
     */
    @Deprecated
    public static String toHex16(long l) {
        String s = "000000000000000" + Long.toHexString(l);
        s = s.substring(s.length() - 16);
        return hexUpperCase ? s.toUpperCase() : s;
    }

    /**
     * ビット列を表す文字列を返します．
     */
    public static String toBits(int b) {
        return toBits(b, 8);
    }

    /**
     * ビット列を表す文字列を返します．
     *
     * @param b ビット列にしたい数値
     * @param n ビット列の桁数
     */
    public static String toBits(int b, int n) {
        int mask = 0x0001 << (n - 1);
//Debug.println(toHex8(mask));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append((b & (mask >>> i)) != 0 ? onBit : offBit);
//Debug.println(toHex8(mask >>> i));
        }
        return sb.toString();
    }

    /**
     * @param s String object to capitalize.
     * @return a capitalized String object.
     */
    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /** */
    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) && c != KeyEvent.CHAR_UNDEFINED && block != null
               && block != Character.UnicodeBlock.SPECIALS;
    }

    /**
     * コマンドラインを分割します。
     *
     * @param line The String to tokenize
     * @return An array of substrings.
     * @exception IOException Thrown if StreamTokenizer.nextToken() throws it.
     */
    public static String[] splitCommandLine(String line) throws IOException {

        // The java.lang.Runtime.exec(String command) call uses
        // java.util.StringTokenizer() to parse the command string.
        // Unfortunately, this means that double quotes are not handled
        // in the same way that the shell handles them in that 'ls "foo
        // 'bar"' will interpreted as three tokens 'ls', '"foo' and
        // 'bar"'.  In the shell, the string would be two tokens 'ls' and
        // '"foo bar"'.  What is worse is that the exec() behaviour is
        // slightly different under Windows and Unix.  To solve this
        // problem, we preprocess the command argument using
        // java.io.StreamTokenizer, which converts quoted substrings into
        // single tokens.  We then call java.lang.Runtime.exec(String []
        // commands);

        // Parse the command into tokens
        List<String> commandList = new LinkedList<>();

        StreamTokenizer streamTokenizer =
            new StreamTokenizer(new StringReader(line));

        // We reset the syntax so that we don't convert to numbers,
        // otherwise, if PTII is "d:\\tmp\\ptII\ 2.0", then
        // we have no end of problems.
        streamTokenizer.resetSyntax();
        streamTokenizer.whitespaceChars(0 , 32);
        streamTokenizer.wordChars(33, 127);

        // We can't use quoteChar here because it does backslash
        // substitution, so "c:\ptII" ends up as "c:ptII"
        // Substituting forward slashes for backward slashes seems like
        // overkill.
        // streamTokenizer.quoteChar('"');
        streamTokenizer.ordinaryChar('"');

        streamTokenizer.eolIsSignificant(true);

        // Current token
        String token = "";

        // Single character token, usually a -
        String singleToken = "";

        // Set to true if we are inside a double quoted String.
        boolean inDoubleQuotedString = false;

        while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

            switch (streamTokenizer.ttype) {
            case StreamTokenizer.TT_WORD:
            case StreamTokenizer.TT_NUMBER:
                if (inDoubleQuotedString) {
                    if (token.length() > 0) {
                        token += " ";
                    }
                    token += singleToken + streamTokenizer.sval;
                } else {
                    token = singleToken + streamTokenizer.sval;
                    commandList.add(token);
                }
                singleToken = "";
                break;
            case StreamTokenizer.TT_EOL:
                break;
            case StreamTokenizer.TT_EOF:
                if (inDoubleQuotedString) {
                    throw new IllegalArgumentException("unbalanced quote");
                }
                break;
            default:
                singleToken =
                    (Character.valueOf((char) streamTokenizer.ttype)).toString();
                if (singleToken.equals("\"")) {
                    if (inDoubleQuotedString) {
                        commandList.add(token);
                    }
                    inDoubleQuotedString = ! inDoubleQuotedString;
                    singleToken = "";
                    token = "";
                }
                break;
            }
        }

        return commandList.toArray(new String[0]);
    }

    /**
     * @see "https://stackoverflow.com/a/31869147"
     */
    public static String abbreviate(String input, String middle, int length) {
        if (input != null && input.length() > length) {
            int half = (length - middle.length()) / 2;

            Pattern pattern = Pattern.compile(
                    "^(.{" + half + ",}?)" + "\\b.*\\b" + "(.{" + half + ",}?)$");
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                return matcher.group(1) + middle + matcher.group(2);
            }
        }

        return input;
    }

    //----

    /** @see #toBits(int) */
    private static String onBit = "*";

    /** @see #toBits(int) */
    private static String offBit = ".";

    /** @see #toHex2(int) */
    private static boolean hexUpperCase = true;

    /** @see #expand(byte[]) */
    private static int bytesExpand = 16;

    /** @see #paramString(Object) */
    private static int recursiveDepth = 3;

    /** @see #paramString(Object) */
    private static boolean ignoredStatics = false;

    /** @see #paramString(Object) */
    private static boolean ignoredFinals = true;

    /** @see #paramString(Object) */
    private static boolean ignoredInnerClasses = true;

    /** @see #paramString(Object) */
    private static boolean ignoredSuperClass = false;

    /** isNotExpanded のクラス集 */
    private static Set<String> classesNotExpanded = new HashSet<>();

    /** isIgnored のクラス集 */
    private static Set<String> classesIgnored = new HashSet<>();

    /** @see "https://hacknote.jp/archives/30235/" */
    public static String getRandomString() {
        try {
            byte[] bytes = new byte[16];
            SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");
            secRandom.nextBytes(bytes);

            StringBuilder buf = new StringBuilder();
            for (byte aByte : bytes) {
                buf.append(String.format("%02x", aByte));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /* */
    static {
        try {
            Properties props = new Properties();
            final String path = "StringUtil.properties";
            props.load(StringUtil.class.getResourceAsStream(path));

            // toBits
            onBit = props.getProperty("vavi.util.StringUtil.string.onBit");
            offBit = props.getProperty("vavi.util.StringUtil.string.offBit");

            // isNotExpanded
            Iterator<?> i = props.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (key.startsWith("vavi.util.StringUtil.isNotExpanded.")) {
                    String className = props.getProperty(key);
                    classesNotExpanded.add(className);
//Debug.println(classesNotExpanded.size() + ", class: " + className);
                }
            }

            // isIgnored
            i = props.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (key.startsWith("vavi.util.StringUtil.isIgnored.")) {
                    String className = props.getProperty(key);
                    classesNotExpanded.add(className);
//Debug.println(classesIgnored.size() + ", class: " + className);
                }
            }

            String value = null;

            // recursive depth
            value = props.getProperty("vavi.util.StringUtil.recursiveDepth");
            recursiveDepth = Integer.parseInt(value);
//Debug.println("recursiveDepth: " + recursiveDepth);

            // bytes expand
            value = props.getProperty("vavi.util.StringUtil.bytesExpand");
            bytesExpand = Integer.parseInt(value);
//Debug.println("bytesExpand: " + bytesExpand);

            // ignore static
            value = props.getProperty("vavi.util.StringUtil.isIgnored.static");
            ignoredStatics = Boolean.parseBoolean(value);
//Debug.println("ignoredStatics: " + ignoredStatics);

            // ignore final
            value = props.getProperty("vavi.util.StringUtil.isIgnored.final");
            ignoredFinals = Boolean.parseBoolean(value);
//Debug.println("ignoredFinals: " + ignoredFinals);

            // ignore inner class
            value = props.getProperty("vavi.util.StringUtil.isIgnored.inner");
            ignoredInnerClasses = Boolean.parseBoolean(value);
//Debug.println("ignoredInnerClasses: " + ignoredInnerClasses);

            // ignore super class
            value = props.getProperty("vavi.util.StringUtil.isIgnored.super");
            ignoredSuperClass = Boolean.parseBoolean(value);
//Debug.println("ignoredSuperClass: " + ignoredSuperClass);

            // hex case upper
            value = props.getProperty("vavi.util.StringUtil.toHex.upperCase");
            hexUpperCase = Boolean.parseBoolean(value);
//Debug.println("hexUpperCase: " + hexUpperCase);
        } catch (Exception e) {
Debug.printStackTrace(e);
        }
    }
}

/* */
