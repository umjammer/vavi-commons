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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import static java.lang.System.getLogger;


/**
 * This is a class for handling .ini.
 * <p>
 * Since it is treated as an extension class of the Properties class,
 * the key name of the property should be treated as "section name" + "." + "key name".
 * </p>
 * Note: Property order and comments are not saved.
 *
 * TODO "http://ini4j.sourceforge.net/"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020502 nsano initial version <br>
 *          0.01 031220 nsano clean imports <br>
 */
public class WindowsProperties extends Properties {

    private static final Logger logger = getLogger(WindowsProperties.class.getName());

    /** The vector for section */
    private final Vector<String> sections = new Vector<>();

    /**
     * Reads the .ini stream.
     */
    @Override
    public void load(InputStream in) throws IOException {

        String section = null;

        this.clear();

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
logger.log(Level.DEBUG, "---- loading ----");
        while (r.ready()) {
            String s = r.readLine().trim();

            if (s.startsWith(";")) {
                // comment
logger.log(Level.DEBUG, "comment: " + s);
            } else if (s.startsWith("[") && s.endsWith("]")) {
                // start a section
                section = s.substring(1, s.length() - 1);
//logger.log(Level.TRACE, "start section: " + section);
            } else {
                // inside a section
                if (section != null) {
                    int p = s.indexOf('=');
                    if (p != -1) {
                        String key = s.substring(0, p);
                        String value = s.substring(p + 1);
//logger.log(Level.TRACE, "set prop: " + section + "." + key + "=" + value);
                        this.setProperty(section + "." + key, value);
                    } else {
logger.log(Level.DEBUG, "bad line: " + s);
                    }
                } else {
logger.log(Level.DEBUG, "outside section: " + s);
                }
            }
        }
    }

    /**
     * Overrides to handle sections.
     *
     * @param key "section name" + "." + "key name"
     * @throws IllegalArgumentException When there is no section name
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
//logger.log(Level.TRACE, "add section: " + section);
        }
        return super.setProperty(key, value);
    }

    /**
     * Writes to the stream in .ini format.
     * Note: Property order and comments are not saved.
     */
    @Override
    public void store(OutputStream out, String header) throws IOException {

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));

logger.log(Level.DEBUG, "---- storing ----");
        w.write(";" + header);
        w.newLine();
        w.write(";" + new Date());
        w.newLine();

        for (int i = 0; i < sections.size(); i++) {
            String section = sections.elementAt(i);
logger.log(Level.DEBUG, "start section: " + section);
            w.write("[" + section + "]");
            w.newLine();

            Enumeration<?> e = this.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.startsWith(section + ".")) {
                    String value = this.getProperty(key);
                    key = key.substring(key.indexOf('.') + 1);
logger.log(Level.DEBUG, "prop: " + key + "=" + value);
                    w.write(key + "=" + value);
                    w.newLine();
                }
            }
        }

        w.flush();
    }

    /**
     * Deletes the properties of the relevant section.
     */
    public void removePropertiesOfSection(String section) {
        Enumeration<?> e = this.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith(section + ".")) {
                this.remove(key);
logger.log(Level.DEBUG, "delete: " + key);
            }
        }
        sections.removeElement(section);
    }
}
