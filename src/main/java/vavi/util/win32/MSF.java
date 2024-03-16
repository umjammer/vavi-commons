/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.util.StringTokenizer;


/**
 * MSF フォーマットを扱うクラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020423 nsano initial version <br>
 *          0.01 020424 nsano add constructor 3, #toString <br>
 *          0.10 020507 nsano repackage <br>
 */
public class MSF {
    /** minutes */
    public int min;
    /** seconds */
    public int sec;
    /** frames (1/75th of a second) */
    public int frame;

    /** */
    public MSF(String msf) {
        StringTokenizer st = new StringTokenizer(msf, ":");
        min   = Integer.parseInt(st.nextToken());
        sec   = Integer.parseInt(st.nextToken());
        frame = Integer.parseInt(st.nextToken());
    }

    /** */
    public MSF(int frames) {
        this.min   = (int) (frames / (60. * 75));
        this.sec   = (int) ((frames - (min * 60. * 75)) / 75);
        this.frame = frames - (min * 60 * 75) - (sec * 75);
    }

    /** */
    public MSF(int min, int sec, int frame) {
        this.min   = min;
        this.sec   = sec;
        this.frame = frame;
    }

    /**
     * Convert String (or array of Strings) from "mm:ss:ff" format
     * to frames-only strings
     */
    public int toFrames() {
        return (min * 60 * 75) + (sec * 75) + frame;
    }

    /** */
    public String toString() {
        return toInt2(min) + ":" + toInt2(sec) + ":" + toInt2(frame);
    }

    /** TODO */
    private static String toInt2(int i) {
        String s = "0" + i;
        return s.substring(s.length() - 2);
    }
}
