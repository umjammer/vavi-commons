/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import javax.swing.JFileChooser;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * RegexFileFilterTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class RegexFileFilterTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    /** Tests this class. */
    public static void main(String[] args) {
        RegexFileFilter fileFilter = new RegexFileFilter(".*\\.xml");
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileFilter(fileFilter);
        fc.showOpenDialog(null);
        System.exit(0);
    }
}

/* */
