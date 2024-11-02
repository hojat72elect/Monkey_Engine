
package com.jme3.system.lwjglx;

import org.lwjgl.opengl.awt.PlatformLinuxGLCanvas;
import org.lwjgl.system.jawt.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.jawt.JAWTFunctions.*;

/**
 * Class <code>X11GLPlatform</code>; overrides the following methods: <code>swapBuffers()</code> 
 * and <code>makeCurrent(long context)</code>. So that the canvas can be removed and 
 * added back from its parent component.
 * 
 * <p>
 * Works only for <b>Linux</b> based platforms
 * 
 * @author wil
 */
final class X11GLPlatform extends PlatformLinuxGLCanvas implements LwjglxGLPlatform {
    
    /**
     * (non-Javadoc)
     * @see org.lwjgl.opengl.awt.PlatformGLCanvas#swapBuffers() 
     * @return boolean
     */
    @Override
    public boolean swapBuffers() {
         // Get the drawing surface info
        JAWTDrawingSurfaceInfo dsi = JAWT_DrawingSurface_GetDrawingSurfaceInfo(ds, ds.GetDrawingSurfaceInfo());
        if (dsi == null) {
            throw new IllegalStateException("JAWT_DrawingSurface_GetDrawingSurfaceInfo() failed");
        }
        
        try {
            // Get the platform-specific drawing info
            JAWTX11DrawingSurfaceInfo dsi_x11 = JAWTX11DrawingSurfaceInfo.create(dsi.platformInfo());

            // Set new values
            display  = dsi_x11.display();
            drawable = dsi_x11.drawable();
            
            // Swap-Buffers            
            return super.swapBuffers();
        } finally {
            JAWT_DrawingSurface_FreeDrawingSurfaceInfo(dsi, ds.FreeDrawingSurfaceInfo());
        }
    }

    /**
     * (non-Javadoc)
     * @see org.lwjgl.opengl.awt.PlatformGLCanvas#makeCurrent(long) 
     * 
     * @param context long
     * @return boolean
     */
    @Override
    public boolean makeCurrent(long context) {
        // Get the drawing surface info
        JAWTDrawingSurfaceInfo dsi = JAWT_DrawingSurface_GetDrawingSurfaceInfo(ds, ds.GetDrawingSurfaceInfo());
        if (dsi == null) {
            throw new IllegalStateException("JAWT_DrawingSurface_GetDrawingSurfaceInfo() failed");
        }

        try {
            // Get the platform-specific drawing info
            JAWTX11DrawingSurfaceInfo dsi_x11 = JAWTX11DrawingSurfaceInfo.create(dsi.platformInfo());
            
            // Set new values
            display  = dsi_x11.display();
            drawable = dsi_x11.drawable();
            
            if (drawable == NULL) {
                return false;
            }
            return super.makeCurrent(context);
        } finally {
            JAWT_DrawingSurface_FreeDrawingSurfaceInfo(dsi, ds.FreeDrawingSurfaceInfo());
        }
    }

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
