
package com.jme3.system.lwjglx;

import org.lwjgl.system.Platform;
import static org.lwjgl.system.Platform.*;

/**
 * Class <code>wjglxDefaultGLPlatform</code> used to create a drawing platform.
 * @author wil
 */
public final class LwjglxDefaultGLPlatform {
    
    /**
     * Returns a drawing platform based on the platform it is running on.
     * @return LwjglxGLPlatform
     * @throws UnsupportedOperationException throws exception if platform is not supported
     */
    public static LwjglxGLPlatform createLwjglxGLPlatform() throws UnsupportedOperationException {
        switch (Platform.get()) {
            case WINDOWS:
                return new Win32GLPlatform();
            //case FREEBSD:  -> In future versions of lwjgl3 (possibly)
            case LINUX:
                return new X11GLPlatform();
            case MACOSX:
                return new MacOSXGLPlatform();
            default:
                throw new UnsupportedOperationException("Platform " + Platform.get() + " not yet supported");
        }
    }
    
    /**
     * private constructor.
     */
    private LwjglxDefaultGLPlatform() {}
}
