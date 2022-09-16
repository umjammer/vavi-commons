/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;


/**
 * Locales.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/23 umjammer initial version <br>
 */
public @interface Locales {

    String[] countries() default "";

    String[] languages() default "";
}

/* */
