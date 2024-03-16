/*
 * Public Domain
 */

package vavi.net.www.protocol.classpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * A {@link URLStreamHandler} that handles resources on the <i>classpath</i>.
 *
 * <pre>
 * classpath:some/package/resource.extension
 * </pre>
 * You don't need to add the first <code>"/"</code>.
 * If you use this class, set <code>-Djava.protocol.handler.pkgs=vavi.net.www.protocol</code>
 *
 * @author http://stackoverflow.com/users/37193/stephen
 * @see "http://stackoverflow.com/questions/861500/url-to-load-resources-from-the-classpath-in-java"
 */
public class Handler extends URLStreamHandler {

    /** The classloader to find resources from. */
    private final ClassLoader classLoader;

    public Handler() {
        this.classLoader = getClass().getClassLoader();
    }

    public Handler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        final URL resourceUrl = classLoader.getResource(u.getPath());
        if (resourceUrl == null) {
            throw new IOException(u.getPath());
        }
//System.err.println("url: " + u);
        return resourceUrl.openConnection();
    }
}
