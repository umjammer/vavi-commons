/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import vavi.util.Debug;


/**
 * .ini を扱うためのクラスです．
 *
 * Properties クラスの拡張クラスとして扱うためプロパティのキー名は
 * "セクション名" + "." + "キー名" として扱ってください．
 *
 * 注意：プロパティの順序、コメントは保存されません。
 *
 * TODO "http://ini4j.sourceforge.net/"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020502 nsano initial version <br>
 *          0.01 031220 nsano clean imports <br>
 */
public class WindowsProperties extends Properties {

    /** The vector for section */
    private Vector<String> sections = new Vector<>();

    /**
     * .ini のストリームを読み込みます．
     */
    @Override
    public void load(InputStream in) throws IOException {

        String section = null;

        this.clear();

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
Debug.println("---- loading ----");
        while (r.ready()) {
            String s = r.readLine().trim();

            if (s.startsWith(";")) {
                // comment
Debug.println("comment: " + s);
            } else if (s.startsWith("[") && s.endsWith("]")) {
                // start a section
                section = s.substring(1, s.length() - 1);
//Debug.println("start section: " + section);
            } else {
                // inside a section
                if (section != null) {
                    int p = s.indexOf('=');
                    if (p != -1) {
                        String key = s.substring(0, p);
                        String value = s.substring(p + 1);
//System.err.println("set prop: " + section + "." + key + "=" + value);
                        this.setProperty(section + "." + key, value);
                    } else {
Debug.println("bad line: " + s);
                    }
                } else {
Debug.println("outside section: " + s);
                }
            }
        }
    }

    /**
     * セクションを扱うためオーバーライドします．
     *
     * @param key "セクション名" + "." + "キー名"
     * @throws IllegalArgumentException セクション名が無いとき
     */
    @Override
    public Object setProperty(String key, String value) {
        int p = key.indexOf('.');
        if (p == -1) {
            throw new IllegalArgumentException("no section: " + key);
        }
        String section = key.substring(0, p);
        if (!sections.contains(section)) {
            sections.addElement(section);
//Debug.println("add section: " + section);
        }
        return super.setProperty(key, value);
    }

    /**
     * .ini 形式でストリームに書き込みます．
     * 注意：プロパティの順序、コメントは保存されません。
     */
    @Override
    public void store(OutputStream out, String header) throws IOException {

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));

Debug.println("---- storing ----");
        w.write(";" + header);
        w.newLine();
        w.write(";" + new Date());
        w.newLine();

        for (int i = 0; i < sections.size(); i++) {
            String section = sections.elementAt(i);
Debug.println("start section: " + section);
            w.write("[" + section + "]");
            w.newLine();

            Enumeration<?> e = this.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.startsWith(section + ".")) {
                    String value = this.getProperty(key);
                    key = key.substring(key.indexOf('.') + 1);
Debug.println("prop: " + key + "=" + value);
                    w.write(key + "=" + value);
                    w.newLine();
                }
            }
        }

        w.flush();
    }

    /**
     * 該当セクションのプロパティをを削除します．
     */
    public void removePropertiesOfSection(String section) {
        Enumeration<?> e = this.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith(section + ".")) {
                this.remove(key);
Debug.println("delete: " + key);
            }
        }
        sections.removeElement(section);
    }
}

/* */
