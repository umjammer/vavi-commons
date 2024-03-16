/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;


/**
 * Checksum.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 060121 nsano initial version <br>
 */
public final class Checksum {

    /**
     * Compute Adler-32 checksum.
     */
    public static long getChecksum(File file) throws IOException {
        return getChecksum(Files.newInputStream(file.toPath()));
    }

    /**
     * Compute Adler-32 checksum.
     */
    public static long getChecksum(Path path) throws IOException {
        return getChecksum(Files.newInputStream(path));
    }

    /**
     * Compute Adler-32 checksum.
     */
    public static long getChecksum(InputStream is) throws IOException {
        try (CheckedInputStream cis = new CheckedInputStream(is, new Adler32())) {
            byte[] buffer = new byte[4 * 1024 * 1024];
            while (cis.read(buffer) >= 0);
            return cis.getChecksum().getValue();
        }
    }
}
