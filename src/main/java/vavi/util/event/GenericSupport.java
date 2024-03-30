/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the basic implementation class for the general-purpose event mechanism.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class GenericSupport implements Serializable {

    /** The generic listeners */
    private final List<GenericListener> listeners = new ArrayList<>();

    /** Adds a generic listener. */
    public void addGenericListener(GenericListener l) {
        listeners.add(l);
    }

    /** Removes a generic listener. */
    public void removeGenericListener(GenericListener l) {
        listeners.remove(l);
    }

    /** Fires a generic event. */
    public void fireEventHappened(GenericEvent ev) {
        listeners.forEach(listener -> listener.eventHappened(ev));
    }
}
