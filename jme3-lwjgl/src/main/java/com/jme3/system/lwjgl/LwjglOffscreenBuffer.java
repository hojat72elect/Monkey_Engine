

package com.jme3.system.lwjgl;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class LwjglOffscreenBuffer extends LwjglContext implements Runnable {

    private static final Logger logger = Logger.getLogger(LwjglOffscreenBuffer.class.getName());
    private Pbuffer pbuffer;
    protected AtomicBoolean needClose = new AtomicBoolean(false);
    private int width;
    private int height;
    private PixelFormat pixelFormat;

    protected void initInThread(){
        if ((Pbuffer.getCapabilities() & Pbuffer.PBUFFER_SUPPORTED) == 0){
            logger.severe("Offscreen surfaces are not supported.");
            return;
        }

        int samples = getNumSamplesToUse();
        pixelFormat = new PixelFormat(settings.getBitsPerPixel(),
                                      settings.getAlphaBits(),
                                      settings.getDepthBits(),
                                      settings.getStencilBits(),
                                      samples);
        
        width = settings.getWidth();
        height = settings.getHeight();
        try{
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable thrown) {
                    listener.handleError("Uncaught exception thrown in "+thread.toString(), thrown);
                }
            });

            pbuffer = new Pbuffer(width, height, pixelFormat, null, null, createContextAttribs());
            pbuffer.makeCurrent();

            renderable.set(true);

            logger.fine("Offscreen buffer created.");
            printContextInitInfo();
        } catch (LWJGLException ex){
            listener.handleError("Failed to create display", ex);
        } finally {
            // TODO: It is possible to avoid "Failed to find pixel format"
            // error here by creating a default display.
        }
        super.internalCreate();
        listener.initialize();
    }

    protected boolean checkGLError(){
        try {
            Util.checkGLError();
        } catch (OpenGLException ex){
            listener.handleError("An OpenGL error has occurred!", ex);
        }
        // NOTE: Always return true since this is used in an "assert" statement
        return true;
    }

    protected void runLoop(){
        if (!created.get()) {
            throw new IllegalStateException();
        }

        if (pbuffer.isBufferLost()) {
            pbuffer.destroy();

            try {
                pbuffer = new Pbuffer(width, height, pixelFormat, null);
                pbuffer.makeCurrent();
                
                // Context MUST be reset here to avoid invalid objects!
                renderer.invalidateState();
            } catch (LWJGLException ex) {
                listener.handleError("Failed to restore PBuffer content", ex);
            }
        }

        listener.update();
        assert checkGLError();

        renderer.postFrame();
        
        // Need to flush GL commands 
        // to see any result on the pbuffer's front buffer.
        GL11.glFlush();

        int frameRate = settings.getFrameRate();
        if (frameRate >= 1) {
            Display.sync(frameRate);
        }
    }

    protected void deinitInThread(){
        renderable.set(false);

        listener.destroy();
        renderer.cleanup();
        pbuffer.destroy();
        logger.fine("Offscreen buffer destroyed.");
        
        super.internalDestroy();
    }

    @Override
    public void run(){
        loadNatives();
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Using LWJGL {0}", Sys.getVersion());
        }
        initInThread();
        while (!needClose.get()){
            runLoop();
        }
        deinitInThread();
    }

    @Override
    public void destroy(boolean waitFor){
        needClose.set(true);
        if (waitFor)
            waitFor(false);
    }

    @Override
    public void create(boolean waitFor){
        if (created.get()){
            logger.warning("create() called when pbuffer is already created!");
            return;
        }

        new Thread(this, THREAD_NAME).start();
        if (waitFor)
            waitFor(true);
    }

    @Override
    public void restart() {
    }

    @Override
    public void setAutoFlushFrames(boolean enabled){
    }

    @Override
    public Type getType() {
        return Type.OffscreenSurface;
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
        return null;
    }

    @Override
    public TouchInput getTouchInput() {
        return null;
    }

    @Override
    public void setTitle(String title) {
    }

}
