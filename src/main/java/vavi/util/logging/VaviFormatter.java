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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.util.StringUtil;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * VaviFormatter.
 * <p>
 * Sets exclusive class names and method pattern into the system property "vavi.util.logging.VaviFormatter.extraClassMethod".
 * <pre>
 *   System.setProperty("vavi.util.logging.VaviFormatter.extraClassMethod", "co\\.paralleluniverse\\.fuse\\.LoggedFuseFilesystem#log");
 * </pre>
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021027 nsano initial version <br>
 *          0.01 031220 nsano clean imports <br>
 */
@PropsEntity(url = "classpath:vavi/util/logging/logging.properties")
public class VaviFormatter extends Formatter {

    /** default excluding pattern */
    @Property(name = "vavi.util.logging.VaviFormatter.classMethod")
    private String defaultClassMethod;

    // TODO make it enabled to put this in user logging.properties
    /** user defined excluding pattern */
    @Property(name = "vavi.util.logging.VaviFormatter.extraClassMethod", useSystem = true)
    private String extraClassMethod;

    /** excluding pattern */
    private Pattern pattern;

    /* */
    {
        try {
            String systemProperty = System.getProperty("vavi.util.logging.VaviFormatter.classMethod");

            if (systemProperty == null) {
                try {
                    PropsEntity.Util.bind(this);
                } catch (IOException e) {
e.printStackTrace();
                }
            } else {
                defaultClassMethod = systemProperty;
            }

            if (extraClassMethod != null && !extraClassMethod.isEmpty()) {
                defaultClassMethod = defaultClassMethod.substring(0, defaultClassMethod.length() - 1) + "|" + extraClassMethod + ")";
            }
//System.err.println("default + extra: " + defaultClassMethod);
            pattern = Pattern.compile(defaultClassMethod);
        } catch (Throwable t) {
t.printStackTrace();
        }
    }

    // wtf thread unsafe?
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");

    /**
     * TODO - check other logging library
     *      - profiler reports slow
     */
    private StackTraceElement findStackTraceElement(StackTraceElement[] stes) {
//java.util.List<String> x = new java.util.ArrayList<>();
        for (int i = stes.length - 1; i >= 0; i--) {
            Matcher matcher = pattern.matcher(stes[i].getClassName() + "#" + stes[i].getMethodName());
//System.err.println("[" + i + "]: " + stes[i].getClassName() + "#" + stes[i].getMethodName() + " - " + matcher.matches());
//x.add("[" + i + "]: " + stes[i].getClassName() + "#" + stes[i].getMethodName() + " - " + matcher.matches());
            if (i != stes.length - 1 && matcher.matches()) {
                return stes[i + 1];
            }
        }
//x.forEach(System.err::println);
        return null;
    }

    /** restoring */
    private static final String color0 = (char) 0x1b + "[" + 0 + "m";

    /** highlighting */
    private static final String color1 = (char) 0x1b + "[" + 37 + "m";

    /** */
    private static final String EOL = System.getProperty("line.separator");

    /* */
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        if (record.getThrown() != null) {
            sb.append(record.getThrown());
            sb.append(EOL);
            StackTraceElement[] stes = record.getThrown().getStackTrace();
            for (StackTraceElement ste : stes) {
                sb.append("\tat ");
                sb.append(ste);
                sb.append(EOL);
            }
            return sb.toString();
        } else {
            String message = formatMessage(record);
            message = message != null ? message.replaceFirst(EOL + "$", "") : "";
            StackTraceElement[] stes = new Exception().getStackTrace();
            StackTraceElement ste = findStackTraceElement(stes);
            if (ste != null) {
                sb.append(color1);
                sb.append(sdf.format(new Date()));
                sb.append(color0);
                sb.append(" [");
                sb.append(record.getLevel());
                sb.append("] ");
                sb.append(message);
                sb.append(EOL);
                sb.append(color1);
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
                sb.append(color0);
                sb.append(EOL);
            } else {
                sb.append(color1);
                sb.append(sdf.format(new Date()));
                sb.append(color0);
                sb.append(" [");
                sb.append(record.getLevel());
                sb.append("] ");
                sb.append(message);
                sb.append(EOL);
                sb.append(color1);
                sb.append("\tat ");
                sb.append(StringUtil.getClassName(record.getSourceClassName()));
                sb.append(".");
                sb.append(record.getSourceMethodName());
                sb.append("(");
                sb.append("Unknown");
                sb.append(")");
                sb.append(color0);
                sb.append(EOL);
            }
            return sb.toString();
        }
    }
}

/* */
