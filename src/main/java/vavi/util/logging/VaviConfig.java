/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.logging;

import java.io.IOException;

import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * The configuration for users class for {@link VaviFormatter}.
 * <p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021104 nsano initial version <br>
 */
@PropsEntity(url = "classpath:logging.properties")
final class VaviConfig {

    /* initialize */
    {
        try {
            PropsEntity.Util.bind(this);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /** user defined excluding pattern */
    @Property(name = "vavi.util.logging.VaviFormatter.extraClassMethod", useSystem = true)
    String extraClassMethod;
}
