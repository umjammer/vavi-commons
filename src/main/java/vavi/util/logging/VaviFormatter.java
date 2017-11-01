/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.logging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import vavi.util.StringUtil;
import vavi.util.properties.annotation.PropsEntity;
import vavi.util.properties.annotation.Property;


/**
 * VaviFormatter.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 021027 nsano initial version <br>
 *          0.01 031220 nsano clean imports <br>
 */
@PropsEntity(url = "classpath:vavi/util/logging/logging.properties")
public class VaviFormatter extends Formatter {

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
        if (record.getThrown() != null) {
            sb.append(record.getThrown());
            sb.append("\n");
            StackTraceElement[] stes = record.getThrown().getStackTrace();
            for (int i = 0; i < stes.length; i++) {
                sb.append("\tat ");
                sb.append(stes[i]);
                sb.append("\n");
            }
            return sb.toString();
        } else {
            StackTraceElement[] stes = new Exception().getStackTrace();
            StackTraceElement ste = null;
            for (int i = 0; i < stes.length; i++) {
                if (!containsExcludingPackages(stes[i].getClassName())) {
                    ste = stes[i];
                    break;
                }
            }
            if (ste != null) {
                sb.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS").format(new Date()));
                sb.append(" [");
                sb.append(record.getLevel());
                sb.append("] ");
                sb.append(record.getMessage().replaceAll("\n$", ""));
                sb.append("\n");
                sb.append("\tat ");
                sb.append(ste.getClassName());
                sb.append(".");
                sb.append(ste.getMethodName());
                sb.append("(");
                if (ste.getFileName() != null) {
                    sb.append(ste.getFileName());
                    sb.append(":");
                    sb.append(ste.getLineNumber());
                } else {
                    sb.append("Unknown");
                }
                sb.append(")");
                sb.append("\n");
            } else {
                sb.append(StringUtil.getClassName(record.getSourceClassName()));
                sb.append("::");
                sb.append(record.getSourceMethodName());
                sb.append(": ");
                sb.append(record.getMessage());
            }
            return sb.toString();
        }
    }
}

/* */
