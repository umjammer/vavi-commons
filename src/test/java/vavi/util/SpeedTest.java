// Copyright 2010 Google Inc. All Rights Reserved.

package vavi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;


/**
 * Diff Speed Test
 *
 * @author fraser@google.com (Neil Fraser)
 * @see "https://github.com/google/diff-match-patch"
 */
class SpeedTest {

    @Test
    void test1() throws IOException {
        String text1 = readFile("/vavi/util/Speedtest1.txt");
        String text2 = readFile("/vavi/util/Speedtest2.txt");

        DiffMatchPatch dmp = new DiffMatchPatch();
        dmp.Diff_Timeout = 0;

        // Execute one reverse diff as a warmup.
        dmp.diff_main(text2, text1, false);

        long start_time = System.nanoTime();
        dmp.diff_main(text1, text2, false);
        long end_time = System.nanoTime();
        System.out.printf("Elapsed time: %f\n", ((end_time - start_time) / 1000000000.0));
    }

    private static String readFile(String filename) throws IOException {
        // Read a file from disk and return the text contents.
        StringBuilder sb = new StringBuilder();
        try (InputStream input = SpeedTest.class.getResourceAsStream(filename);
             BufferedReader bufRead = new BufferedReader(new InputStreamReader(input))) {
            String line = bufRead.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = bufRead.readLine();
            }
        }
        return sb.toString();
    }
}