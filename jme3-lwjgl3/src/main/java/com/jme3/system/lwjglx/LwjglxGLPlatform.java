
package com.jme3.system.lwjglx;

import org.lwjgl.opengl.awt.PlatformGLCanvas;

/**
 * Interface <code>LwjglxGLPlatform</code>; It is used to implement and manage
 * the context of a specific platform.
 * 
 * @author wil
 */
public interface LwjglxGLPlatform extends PlatformGLCanvas {

    /**
     * Free the drawing surface.
     */
    public void destroy();    
}
