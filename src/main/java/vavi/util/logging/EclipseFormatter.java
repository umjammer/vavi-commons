/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.logging;

import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * EclipseFormatter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040913 nsano initial version <br>
 */
@PropsEntity(url = "classpath:vavi/util/logging/logging.properties")
public class EclipseFormatter extends Formatter {

    @Property(name = "vavi.util.logging.excludes")
    private String defaultExcludingPackages;

    /* */
    {
        try {
            PropsEntity.Util.bind(this);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private static String excludingPackages;

    /* */
    static {
        excludingPackages = System.getProperty("vavi.util.logging.excludes", "");
    }

    /** */
    private boolean containsExcludingPackages(String className) {
        for (String excludingPackage : (defaultExcludingPackages + "," + excludingPackages).split(",")) {
            if (className.startsWith(excludingPackage.trim())) {
                return true;
            }
        }
        return false;
    }

    /** */
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stes = new Throwable().getStackTrace();
        int i = 0;
outer:
        for (; i < stes.length; i++) {
            if (!containsExcludingPackages(stes[i].getClassName())) {
                break outer;
            }
        }

        sb.append("at ");
        sb.append(stes[i].getClassName());
        sb.append("(");
        sb.append(stes[i].getMethodName());
        sb.append(":");
        sb.append(stes[i].getLineNumber());
        sb.append(")");
        return sb.toString();
    }
}

/* */
