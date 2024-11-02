
package com.jme3.util.res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public interface ResourceLoader {
    /**
     * Finds the resource with the given name relative to the given parent class
     * or to the root if the parent is null.
     * 
     * @param path
     *            The resource name
     * @param parent
     *            Optional parent class
     * @return The resource URL or null if not found
     */
    public URL getResource(String path, Class<?> parent);

    /**
     * Finds the resource with the given name relative to the given parent class
     * or to the root if the parent is null.
     * 
     * @param path
     *            The resource name
     * @param parent
     *            Optional parent class
     * @return An input stream to the resource or null if not found
     */
    public InputStream getResourceAsStream(String path, Class<?> parent);


    /**
     * Finds all resources with the given name.
     * 
     * 
     * @param path
     *            The resource name
     * @return An enumeration of {@link java.net.URL <code>URL</code>} objects for
     *         the resource. If no resources could be found, the enumeration
     *         will be empty.
     *
     * @throws IOException
     *             If I/O errors occur
     *
     * @throws IOException
     */
    public Enumeration<URL> getResources(String path) throws IOException;
}
