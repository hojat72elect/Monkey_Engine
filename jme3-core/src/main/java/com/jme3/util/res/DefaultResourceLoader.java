
package com.jme3.util.res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Default implementation of {@link ResourceLoader}. 
 * Loads from classpath.
 */
class DefaultResourceLoader implements ResourceLoader {
    
    DefaultResourceLoader() {
        
    }

    @Override
    public InputStream getResourceAsStream(String path, Class<?> parent) {
        if (parent == null) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        } else {
            return parent.getResourceAsStream(path);
        }
    }

    @Override
    public URL getResource(String path, Class<?> parent) {
        if (parent == null) {
            return Thread.currentThread().getContextClassLoader().getResource(path);
        } else {
            return parent.getResource(path);
        }
    }

    @Override
    public Enumeration<URL> getResources(String path) throws IOException {
        return Thread.currentThread().getContextClassLoader().getResources(path);        
    }

}
