
package com.jme3.util.res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to load resources from the default location usually the
 * classpath.
 */
public class Resources {
    /**
     * The property name to set the ResourceLoader to use. Should be set automatically
     * by the JmeSystemDelegate. 
     * Note: changing this property after the first use of the ResourceLoader will have no effect.
     */
    public static final String PROPERTY_RESOURCE_LOADER_IMPLEMENTATION = "com.jme3.ResourceLoaderImplementation";

    private static final String DEFAULT_IMPL = "com.jme3.util.res.DefaultResourceLoader";
    private static final Logger LOGGER = Logger.getLogger(Resources.class.getName());
    private static ResourceLoader impl = null;

    @SuppressWarnings("unchecked")
    private static Class<? extends ResourceLoader> findResourceLoaderClass(String className) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (final Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to access {0}", className);
        }

        if (clazz != null && !ResourceLoader.class.isAssignableFrom(clazz)) {
            LOGGER.log(Level.WARNING, "{0} does not implement {1}", new Object[] { className, ResourceLoader.class.getName() });
            clazz = null;
        }

        return (Class<? extends ResourceLoader>) clazz;
    }

    private static ResourceLoader getResourceLoader() {
        if (impl != null) return impl;
        Class<? extends ResourceLoader> clazz = null;
        String userDefinedImpl = System.getProperty(PROPERTY_RESOURCE_LOADER_IMPLEMENTATION, null);
        
        if (userDefinedImpl != null) {
            LOGGER.log(Level.FINE, "Loading user defined ResourceLoader implementation {0}", userDefinedImpl);
            clazz = findResourceLoaderClass(userDefinedImpl);
        }

        if (clazz == null) {
            LOGGER.log(Level.FINE, "No usable user defined ResourceLoader implementation found, using default implementation {0}", DEFAULT_IMPL);
            clazz = findResourceLoaderClass(DEFAULT_IMPL);
        }

        if (clazz == null) {
            throw new RuntimeException("No ResourceLoader implementation found");
        }

        try {
            impl = (ResourceLoader) clazz.getDeclaredConstructor().newInstance();
        } catch (final Throwable e) {
            throw new RuntimeException("Could not instantiate ResourceLoader class " + clazz.getName(), e);
        }

        return impl;
    }

    /**
     * Sets the resource loader implementation to use.
     * @param impl The resource loader implementation
     */
    public static void setResourceLoader(ResourceLoader impl) {
        Resources.impl = impl;
    }

    /**
     * Finds the resource with the given name.
     * 
     * @param path
     *            The resource name
     * @return The resource URL or null if not found
     */
    public static URL getResource(String path) {
        return getResourceLoader().getResource(path, null);
    }

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
    public static URL getResource(String path, Class<?> parent) {
        return getResourceLoader().getResource(path, parent);
    }

    /**
     * Finds the resource with the given name.
     * 
     * @param path
     *            The resource name
     * @return An input stream to the resource or null if not found
     */
    public static InputStream getResourceAsStream(String path) {
        return getResourceLoader().getResourceAsStream(path, null);
    }

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
    public static InputStream getResourceAsStream(String path, Class<?> parent) {
        return getResourceLoader().getResourceAsStream(path, parent);
    }

    /**
     * Finds all resources with the given name.
     * 
     * 
     * @param path
     *            The resource name
     *
     * 
     * @return An enumeration of {@link java.net.URL <code>URL</code>} objects for
     *         the resource. If no resources could be found, the enumeration
     *         will be empty.
     *
     * @throws IOException
     *             If I/O errors occur
     *
     * @throws IOException
     */
    public static Enumeration<URL> getResources(String path) throws IOException {
        return getResourceLoader().getResources(path);
    }

}
