/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.logging;

import java.io.IOException;
import java.util.logging.LogManager;


/**
 * デバッグのコンフィグレーションクラスです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 021104 nsano initial version <br>
 */
public final class VaviConfig {

    /** */
    public VaviConfig() {
        try {
            LogManager.getLogManager().readConfiguration(
                this.getClass().getResourceAsStream("logging.properties"));
        } catch (IOException e) {
System.err.println(e);
        }
    }
}

/* */
