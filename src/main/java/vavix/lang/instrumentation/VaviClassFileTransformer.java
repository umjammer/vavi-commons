/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.lang.instrument.ClassFileTransformer;


/**
 * This class indicates a loaded class by {@link VaviInstrumentation}.
 * Each class has id.
 * <p>
 * {{@link #setId(String)}'s id means the id of transformer classes.
 * </p>
 * ex. id is "1"
 * <pre>
 * -Dvavix.lang.instrumentation.VaviInstrumentation.1=vavix.lang.instrumentation.PassClassFileTransformer
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 060809 nsano initial version <br>
 */
public interface VaviClassFileTransformer extends ClassFileTransformer {
    /** */
    void setId(String id);
    /** */
    String getId();
}

/* */
