
package com.jme3.renderer;

/**
 * Raised when a renderer encounters
 * a fatal rendering error.
 *
 * @author Kirill Vainer
 */
public class RendererException extends RuntimeException {

    /**
     * Creates a new instance of <code>RendererException</code>.
     *
     * @param message the desired message text
     */
    public RendererException(String message) {
        super(message);
    }
}
