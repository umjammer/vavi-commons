/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import org.junit.jupiter.api.Test;

import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.*;
import static vavix.util.DelayedWorker.later;


/**
 * DelayedWorkerTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/13 umjammer initial version <br>
 */
class DelayedWorkerTest {

    @Test
    synchronized void test() {
        long w = Boolean.valueOf(System.getProperty("vavi.test")) ? 1 * 1000 : 10 * 1000;
        long s = System.currentTimeMillis();
        while (!later(w).come()) {
            Thread.yield();
        }
        long e = System.currentTimeMillis();
        long t = e - s;
Debug.println("time: " + t);
        long delta = 100;
        assertTrue( w - delta < t && t < w + delta);

        //
        s = System.currentTimeMillis();
        while (!later(w).come()) {
            Thread.yield();
        }
        e = System.currentTimeMillis();
        t = e - s;
Debug.println("time: " + t);
        assertTrue( w - delta < t && t < w + delta);
    }
}

/* */
