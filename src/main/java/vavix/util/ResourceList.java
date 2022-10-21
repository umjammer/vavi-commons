/*
 * https://stackoverflow.com/a/3923182
 */

package vavix.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * list resources available from the classpath @ *
 */
public final class ResourceList {

    private ResourceList() {}

    /**
     * for all elements of java.class.path get a Collection of resources Pattern
     * pattern = Pattern.compile(".*"); gets all resources
     *
     * @param pattern the pattern to match
     * @return the resources in the order they are found
     * <ul>
     * <li> file ... "file://***"
     * <li> in jar ... "jar://***.jar!***"
     * </ul>
     * last "/" is directory
     */
    public static Collection<String> getResources(final Pattern pattern) {
        List<String> retval = new ArrayList<>();
        String classPath = System.getProperty("java.class.path", ".");
        String[] classPathElements = classPath.split(System.getProperty("path.separator"));
        for (String element : classPathElements) {
            retval.addAll(getResources(element, pattern));
        }
        return retval;
    }

    private static Collection<String> getResources(final String element, final Pattern pattern) {
        ArrayList<String> retval = new ArrayList<>();
        File file = new File(element);
        if (file.isDirectory()) {
            retval.addAll(getResourcesFromDirectory(file, pattern));
        } else {
            retval.addAll(getResourcesFromJarFile(file, pattern));
        }
        return retval;
    }

    private static Collection<String> getResourcesFromJarFile(final File file, final Pattern pattern) {
        List<String> retval = new ArrayList<>();
        ZipFile zf;
        try {
            zf = new ZipFile(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        Enumeration<?> e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            String fileName = ze.getName();
            boolean accept = pattern.matcher(fileName).matches();
            if (accept) {
                retval.add("jar://" + file.getPath() + "!" + fileName);
            }
        }
        try {
            zf.close();
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
        return retval;
    }

    private static Collection<String> getResourcesFromDirectory(final File directory, final Pattern pattern) {
        List<String> retval = new ArrayList<>();
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                try {
                    String fileName = file.getCanonicalPath();
                    boolean accept = pattern.matcher(fileName).matches();
                    if (accept) {
                        retval.add("file://" + fileName);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        return retval;
    }
}
