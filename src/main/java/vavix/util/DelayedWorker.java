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

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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

    private static final ThreadLocal<DelayedWorkDetector> detectors = new ThreadLocal<>();

    /**
     * @param millis delay in milliseconds
     */
    public static DelayedWorkDetector later(long millis) {
        DelayedWorkDetector detector = detectors.get();
        if (detector == null) {
            detector = new DelayedWorkDetector() {
                boolean flag = false;
                boolean exec = false;
                @Override public boolean come() {
                    if (!exec) {
                        later(millis, this::exec);
Debug.println(Level.FINEST, "exec after: " + millis + " [ms], @" + this.hashCode() + ", " + Thread.currentThread().getId());
                        exec = true;
                    }
                    if (flag) {
                        cleanup();
                    }
                    return flag;
                }
                private void exec() {
                    flag = true;
Debug.println(Level.FINEST, "time to come: @" + this.hashCode() + ", " + Thread.currentThread().getId());
                }
            };
Debug.println(Level.FINEST, "new detector: @" + detector.hashCode());
            detectors.set(detector);
        }
        return detector;
    }

    /**
     * clean up if stop checking come() before schedule
     */
    public static void cleanup() {
Debug.println(Level.FINEST, "cleanup: @" + detectors.get().hashCode() + ", " + Thread.currentThread().getId());
        detectors.remove();
    }
}
