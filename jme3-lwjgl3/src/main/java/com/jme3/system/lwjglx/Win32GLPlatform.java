
package com.jme3.system.lwjglx;

import static org.lwjgl.system.jawt.JAWTFunctions.*;
import org.lwjgl.opengl.awt.PlatformWin32GLCanvas;

/**
 * <code>Win32GLPlatform</code> class that implements the {@link com.jme3.system.lwjglx.LwjglxGLPlatform} 
 * interface for the Windows (Win32) platform.
 * 
 * @author wil
 */
final class Win32GLPlatform extends PlatformWin32GLCanvas implements LwjglxGLPlatform {

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
