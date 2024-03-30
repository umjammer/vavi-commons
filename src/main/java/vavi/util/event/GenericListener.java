/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.util.EventListener;


/**
 * A general-purpose listener interface.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
@FunctionalInterface
public interface GenericListener extends EventListener {

    /**
     * Called when an event is issued.
     *
     * @param ev generic event
     */
    void eventHappened(GenericEvent ev);
}
