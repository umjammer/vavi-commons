/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * DelayedWorker.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/13 umjammer initial version <br>
 */
public final class DelayedWorker {

    private DelayedWorker() {}

    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * @param millis delay in milliseconds
     */
    public static void later(long millis, Runnable r) {
        scheduler.schedule(r, millis, TimeUnit.MILLISECONDS);
    }

    @FunctionalInterface
    public interface DelayedWorkDetector {
        /**
         * delayed scheduled time to come or not
         */
        boolean come();
    }

    private static ThreadLocal<DelayedWorkDetector> detectors = new ThreadLocal<>();

    /**
     * @param millis delay in milliseconds
     */
    public static DelayedWorkDetector later(long millis) {
        DelayedWorkDetector detector = detectors.get();
        if (detector == null) {
            detector = new DelayedWorkDetector() {
                boolean flag = false;
                boolean exec = false;
                public boolean come() {
                    if (exec == false) {
                        exec();
                        exec = true;
                    }
                    if (flag) {
Debug.println(Level.FINE, "cleanup: " + this.hashCode() + ", " + Thread.currentThread().getId());
                        detectors.remove();
                    }
                    return flag;
                }
                private void exec() {
Debug.println(Level.FINE, "exec after: " + millis + " [ms], " + this.hashCode() + ", " + Thread.currentThread().getId());
                    later(millis, () -> {
Debug.println(Level.FINE, "time to come: " + this.hashCode() + ", " + Thread.currentThread().getId());
                        flag = true;
                    });
                }
            };
Debug.println(Level.FINE, "new detector: " + detector.hashCode());
            detectors.set(detector);
        }
        return detector;
    }
}

/* */
