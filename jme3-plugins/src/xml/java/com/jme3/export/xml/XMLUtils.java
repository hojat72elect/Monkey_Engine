
package com.jme3.export.xml;

import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.DOMException;

/**
 * Utilities for reading and writing XML files.
 * 
 * @author codex
 */
public class XMLUtils {
    
    /**
     * Prefix for every jme xml attribute for format versions 3 and up.
     * <p>
     * This prefix should be appended at the beginning of every xml
     * attribute name. For format versions 3 and up, every name to
     * access an attribute must append this prefix first.
     */
    public static final String PREFIX = "jme-";
    
    /**
     * Sets the attribute in the element under the name.
     * <p>
     * Automatically appends {@link #PREFIX} to the beginning of the name
     * before assigning the attribute to the element.
     * 
     * @param element element to set the attribute in
     * @param name name of the attribute (without prefix)
     * @param attribute attribute to save
     * 
     * @throws java.io.IOException wraps DOMException in IOException for convenience since everywhere this method
     * is used is expected to throw only IOException.
     */
    public static void setAttribute(Element element, String name, String attribute) throws IOException {
        try {
            element.setAttribute(PREFIX+name, attribute);
        } catch (DOMException domEx) {
            throw new IOException(domEx);
        }
    }
    
    /**
     * Fetches the named attribute from the element.
     * <p>
     * Automatically appends {@link #PREFIX} to the beginning
     * of the name before looking up the attribute for format versions 3 and up.
     * 
     * @param version format version of the xml
     * @param element XML element to get the attribute from
     * @param name name of the attribute (without prefix)
     * @return named attribute
     */
    public static String getAttribute(int version, Element element, String name) {
        if (version >= 3) {
            return element.getAttribute(PREFIX+name);
        } else {
            return element.getAttribute(name);
        }
    }
    
    /**
     * Tests if the element contains the named attribute.
     * <p>
     * Automatically appends {@link #PREFIX} to the beginning
     * of the name before looking up the attribute for format versions 3 and up.
     * 
     * @param version format version of the xml
     * @param element element to test
     * @param name name of the attribute (without prefix)
     * @return true if the element has the named attribute
     */
    public static boolean hasAttribute(int version, Element element, String name) {
        if (version >= 3) {
            return element.hasAttribute(PREFIX+name);
        } else {
            return element.hasAttribute(name);
        }
    }
    
    /**
     * Denies instantiation of this class.
     */
    private XMLUtils() {
    }
}
