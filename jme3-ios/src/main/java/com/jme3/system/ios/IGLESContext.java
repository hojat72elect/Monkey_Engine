
package com.jme3.system.ios;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.input.ios.IosInputHandler;
import com.jme3.opencl.Context;
import com.jme3.renderer.ios.IosGL;
import com.jme3.renderer.opengl.*;
import com.jme3.system.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IGLESContext implements JmeContext {

    private static final Logger logger = Logger.getLogger(IGLESContext.class.getName());
    protected final AtomicBoolean created = new AtomicBoolean(false);
    protected final AtomicBoolean renderable = new AtomicBoolean(false);
    protected final AtomicBoolean needClose = new AtomicBoolean(false);
    protected AppSettings settings = new AppSettings(true);
    protected boolean autoFlush = true;

    /*
     * >= OpenGL ES 2.0 (iOS)
     */
    protected GLRenderer renderer;
    protected Timer timer;
    protected SystemListener listener;
    protected IosInputHandler input;
    protected int minFrameDuration = 0;                   // No FPS cap

    public IGLESContext() {
           logger.log(Level.FINE, "IGLESContext constructor");
    }

    @Override
    public Type getType() {
        return Type.Display;
    }

    @Override
    public void setSettings(AppSettings settings) {
        logger.log(Level.FINE, "IGLESContext setSettings");
        this.settings.copyFrom(settings);
        if (input != null) {
            input.loadSettings(settings);
        }
    }

    /**
     * Accesses the listener that receives events related to this context.
     *
     * @return the pre-existing instance
     */
    @Override
    public SystemListener getSystemListener() {
        logger.log(Level.FINE, "IGLESContext getSystemListener");
        return listener;
    }

    @Override
    public void setSystemListener(SystemListener listener) {
        logger.log(Level.FINE, "IGLESContext setSystemListener");
        this.listener = listener;
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    @Override
    public com.jme3.renderer.Renderer getRenderer() {
        logger.log(Level.FINE, "IGLESContext getRenderer");
        return renderer;
    }

    @Override
    public MouseInput getMouseInput() {
        return new DummyMouseInput();
    }

    @Override
    public KeyInput getKeyInput() {
        return new DummyKeyInput();
    }

    @Override
    public JoyInput getJoyInput() {
    /*
        if (androidSensorJoyInput == null) {
            androidSensorJoyInput = new AndroidSensorJoyInput();
        }
        return androidSensorJoyInput;
        */
        return null;//  new DummySensorJoyInput();
    }

    @Override
    public TouchInput getTouchInput() {
        return input;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public boolean isCreated() {
        logger.log(Level.FINE, "IGLESContext isCreated");
        return created.get();
    }

    @Override
    public void setAutoFlushFrames(boolean enabled) {
        this.autoFlush = enabled;
    }

    @Override
    public boolean isRenderable() {
        logger.log(Level.FINE, "IGLESContext isRenderable");
        return true;// renderable.get();
    }

    @Override
    public void create(boolean waitFor) {
        logger.log(Level.FINE, "IGLESContext create");
        IosGL gl = new IosGL();

        if (settings.getBoolean("GraphicsDebug")) {
            gl = (IosGL)GLDebug.createProxy(gl, gl, GL.class, GLExt.class, GLFbo.class);
        }

        renderer = new GLRenderer(gl, gl, gl);
        renderer.initialize();
        
        input = new IosInputHandler();
        timer = new NanoTimer();

//synchronized (createdLock){
            created.set(true);
            //createdLock.notifyAll();
        //}

        listener.initialize();

        if (waitFor) {
            //waitFor(true);
        }
        logger.log(Level.FINE, "IGLESContext created");
    }

    public void create() {
        create(false);
    }

    @Override
    public void restart() {
    }

    @Override
    public void destroy(boolean waitFor) {
        logger.log(Level.FINE, "IGLESContext destroy");
        listener.destroy();
        needClose.set(true);
        if (waitFor) {
            //waitFor(false);
        }
    }

    public void destroy() {
        destroy(true);
    }

    protected void waitFor(boolean createdVal) {
        while (renderable.get() != createdVal) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public Context getOpenCLContext() {
        logger.warning("OpenCL not yet supported on this platform");
        return null;
    }

    /**
     * Returns the height of the framebuffer.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int getFramebufferHeight() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Returns the width of the framebuffer.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int getFramebufferWidth() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Returns the screen X coordinate of the left edge of the content area.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int getWindowXPosition() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Returns the screen Y coordinate of the top edge of the content area.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int getWindowYPosition() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}