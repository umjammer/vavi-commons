/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.www.protocol;

import java.net.URLStreamHandler;
import java.util.ServiceLoader;
import java.util.logging.Logger;


/**
 * URLStreamHandlerUtil.
 * <p>
 * Prepare to be loaded handlers specified in <code>META-INF/services/java.net.URLStreamHandler</code>.
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/06 umjammer initial version <br>
 */
public class URLStreamHandlerUtil {

    private static final Logger logger = Logger.getLogger(URLStreamHandlerUtil.class.getName());

    static {
        logger.setUseParentHandlers(false);
    }

    private URLStreamHandlerUtil() {
    }

    /**
     * @see "classpath:META-INF/services/java.net.URLStreamHandler"
     * @see "https://docs.oracle.com/javase/8/docs/api/java/net/URL.html#URL-java.lang.String-java.lang.String-int-java.lang.String-"
     */
    public static void loadService() {
        ServiceLoader<URLStreamHandler> loader = ServiceLoader.load(URLStreamHandler.class);
        StringBuilder packages = new StringBuilder(System.getProperty("java.protocol.handler.pkgs", ""));
logger.fine("java.protocol.handler.pkgs: before: " + packages);
        for (URLStreamHandler handler : loader) {
logger.fine("protocol: " + handler.getClass().getName());
            String packageName = handler.getClass().getPackage().getName();
            String superPackageName = packageName.substring(0, packageName.lastIndexOf('.'));
            if (packages.indexOf(superPackageName) < 0) {
                if (packages.length() != 0) {
                    packages.append('|');
                }
                packages.append(superPackageName);
            }
        }
logger.fine("java.protocol.handler.pkgs: after: " + packages);
        System.setProperty("java.protocol.handler.pkgs", packages.toString());
    }
}
