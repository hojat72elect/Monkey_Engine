package com.jme3.system.lwjgl;



import com.jme3.system.JmeContext;

/**
 * A VR oriented LWJGL offscreen buffer.
 * @author Daniel Johansson
 * @author reden - phr00t - https://github.com/phr00t
 * @author Julien Seinturier - (c) 2016 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 */
public class LwjglOffscreenBufferVR extends LwjglWindow {
    /**
     * Create a new VR oriented LWJGL offscreen buffer.
     */
    public LwjglOffscreenBufferVR() {
        super(JmeContext.Type.OffscreenSurface);
    }
}
