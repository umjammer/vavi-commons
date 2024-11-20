/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.*;
import static vavix.util.DelayedWorker.cleanup;
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
        long w = System.getProperty("vavi.test", "").equals("ide") ? 10 * 1000 : 5 * 1000;
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

    @Test
    @DisplayName("execute multiply")
    synchronized void test2() throws Exception {
        long w = System.getProperty("vavi.test", "").equals("ide") ? 10 * 1000 : 5 * 1000;
        long s = System.currentTimeMillis();
        DelayedWorker.DelayedWorkDetector dw = later(w);
        int dw1 = dw.hashCode();
        while (!dw.come()) {
            Thread.sleep(2000);
            break;
        }
        assertFalse(dw.come());
        cleanup();
        dw = later(w);
        int dw2 = dw.hashCode();
        assertNotEquals(dw1, dw2);
    }
}
