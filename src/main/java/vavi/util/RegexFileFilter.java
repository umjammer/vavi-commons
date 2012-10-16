/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;


/**
 * Regex でフィルタをかけるファイルフィルタです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010910 nsano initial version <br>
 *          0.01 010912 nsano ignors InterruptedException <br>
 */
public class RegexFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

    /** パターンの配列 */
    private List<String> regexs = new ArrayList<String>(1);

    /** フィルタの説明 */
    private String description;

    /**
     * ファイルフィルタを作成します．
     */
    public RegexFileFilter() {
    }

    /**
     * 指定したパターンのファイルフィルタを作成します．
     * 
     * @param regex パターン
     */
    public RegexFileFilter(String regex) {
        regexs.add(regex);
    }

    /**
     * 指定したパターンのファイルフィルタを作成します．
     * 
     * @param regex パターン
     */
    public RegexFileFilter(String regex, String description) {
        regexs.add(regex);
        this.description = description;
    }

    /**
     * フィルタの説明を設定します．
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * パターンを追加します．
     */
    public void addPattern(String regex) {
        regexs.add(regex);
    }

    /**
     * 指定したファイルをフィルタが受け付けるかどうかを返します．
     * パターンが無い場合と、ディレクトリは true を返します。
     * @param file 対象のファイル
     */
    public boolean accept(File file) {

        if (file.isDirectory()) {
            return true;
        }

        if (regexs.size() == 0) {
Debug.println("no pattern");
            return true;
        }

        try {
            Iterator<String> i = regexs.iterator();
            while (i.hasNext()) {
                Pattern p = Pattern.compile(i.next());
                Matcher m = p.matcher(file.getName());
                if (m.matches()) {
                    return true;
                }
            }
        } catch (Exception e) {
// Debug.println(e);
        }

        return false;
    }

    /**
     * フィルタの説明を返します．
     */
    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            if (regexs.size() == 0) {
                return "all";
            }
            description = new String("Regex: ");
            for (int i = 0; i < regexs.size(); i++) {
                description += "\'" + regexs.get(i) + "\', ";
            }
            description = description.substring(0, description.length() - 2);
            return description;
        }
    }

    /** Tests this class. */
    public static void main(String[] args) {
        RegexFileFilter fileFilter = new RegexFileFilter(".*\\.xml");
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileFilter(fileFilter);
        fc.showOpenDialog(null);
        System.exit(0);
    }
}

/* */
