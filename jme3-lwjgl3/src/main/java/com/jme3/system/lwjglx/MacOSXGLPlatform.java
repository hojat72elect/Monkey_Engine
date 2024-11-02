
package com.jme3.system.lwjglx;

import org.lwjgl.opengl.awt.PlatformMacOSXGLCanvas;
import static org.lwjgl.system.jawt.JAWTFunctions.*;

/**
 * <code>MacOSXGLPlatform</code> class that implements the {@link com.jme3.system.lwjglx.LwjglxGLPlatform} 
 * interface for the MacOS platform.
 * 
 * @author wil
 */
final class MacOSXGLPlatform extends PlatformMacOSXGLCanvas implements LwjglxGLPlatform {

    /**
     * (non-Javadoc)
     * @see com.jme3.system.lwjglx.LwjglxGLPlatform#destroy() 
     */
    @Override
    public void destroy() {
        if (ds != null) {
            JAWT_FreeDrawingSurface(ds, awt.FreeDrawingSurface());
            awt.free();
        }
    }
 }
