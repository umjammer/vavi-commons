/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.logging;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.stream.Stream;


/**
 * The configuration class for debug.
 * <p>
 * system property
 * <li> java.util.logging.config.class
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021104 nsano initial version <br>
 */
public final class VaviConfig {

    /** */
    public VaviConfig() {
        try {
            LogManager.getLogManager().readConfiguration(
                this.getClass().getResourceAsStream("/logging.properties"));
String rootLevel = LogManager.getLogManager().getProperty(".level");
// TODO isLoggable()
if (Stream.of("FINE", "FINER", "FINEST").anyMatch(l -> l.equals(rootLevel))) {
 System.err.println("---- classpath:logging.properties " + LogManager.getLogManager().getProperty(".level"));
 Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/logging.properties"));
 while (scanner.hasNextLine()) {
  System.err.println(scanner.nextLine());
 }
 scanner.close();
 System.err.println("----");
}
        } catch (Exception e) {
e.printStackTrace();
        }
    }
}
