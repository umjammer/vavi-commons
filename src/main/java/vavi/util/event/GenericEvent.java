/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.util.EventObject;


/**
 * This is a generic event.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class GenericEvent extends EventObject {

    /** */
    private final String name;

    /** */
    private final Object[] arguments;

    /**
     * Creates a generic event.
     *
     * @param source the event source
     * @param name the event name
     */
    public GenericEvent(Object source, String name) {
        this(source, name, (Object[]) null);
    }

    /**
     * Creates a generic event.
     *
     * @param source the event source
     * @param name the event name
     * @param arguments the event argument
     */
    public GenericEvent(Object source, String name, Object ... arguments) {
        super(source);

        this.name = name;
        this.arguments = arguments;
    }

    /** */
    public String getName() {
        return name;
    }

    /** @return nullable */
    public Object[] getArguments() {
        return arguments;
    }
}
