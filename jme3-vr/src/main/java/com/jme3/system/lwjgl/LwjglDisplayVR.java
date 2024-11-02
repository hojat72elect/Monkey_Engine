
package com.jme3.system.lwjgl;

import com.jme3.opencl.Context;

/**
 * A VR oriented LWJGL display.
 * @author Daniel Johansson
 * @author reden - phr00t - https://github.com/phr00t
 * @author Julien Seinturier - (c) 2016 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 */
public class LwjglDisplayVR extends LwjglWindowVR {
    /**
     * Create a new VR oriented LWJGL display.
     */
    public LwjglDisplayVR() {
        super(Type.Display);
    }

    @Override
    public Context getOpenCLContext() {
        return null;
    }
}
