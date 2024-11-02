package com.jme3.input.lwjgl;



import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.system.lwjgl.LwjglWindowVR;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A key input that wraps GLFW underlying components.
 * @author reden - phr00t - https://github.com/phr00t
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 *
 */
public class GlfwKeyInputVR implements KeyInput {

    private static final Logger logger = Logger.getLogger(GlfwKeyInput.class.getName());

    private LwjglWindowVR context;
    private RawInputListener listener;
    private boolean initialized, shift_pressed;
    private GLFWKeyCallback keyCallback;
    private Queue<KeyInputEvent> keyInputEvents = new LinkedList<>();

    /**
     * Create a new input attached to the given {@link LwjglWindowVR context}.
     * @param context the context to attach to this input.
     */
    public GlfwKeyInputVR(LwjglWindowVR context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        if (!context.isRenderable()) {
            return;
        }

        glfwSetKeyCallback(context.getWindowHandle(), keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                scancode = GlfwKeyMap.toJmeKeyCode(key);
                if( key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT ) {
                    shift_pressed = (action == GLFW_PRESS);
                } else if( key >= 'A' && key <= 'Z' && !shift_pressed ) {
                    key += 32; // make lowercase
                } else if( key >= 'a' && key <= 'z' && shift_pressed ) {
                    key -= 32; // make uppercase
                }
                final KeyInputEvent evt = new KeyInputEvent(scancode, (char) key, GLFW_PRESS == action, GLFW_REPEAT == action);
                evt.setTime(getInputTimeNanos());
                keyInputEvents.add(evt);
            }
        });

        glfwSetInputMode(context.getWindowHandle(), GLFW_STICKY_KEYS, 1);

        initialized = true;
        logger.fine("Keyboard created.");
    }

    /**
     * Get the key count.
     * @return the key count.
     */
    public int getKeyCount() {
        // This might not be correct
        return GLFW_KEY_LAST - GLFW_KEY_SPACE;
    }

    @Override
    public void update() {
        if (!context.isRenderable()) {
            return;
        }

        while (!keyInputEvents.isEmpty()) {
            listener.onKeyEvent(keyInputEvents.poll());
        }
    }

    @Override
    public void destroy() {
        if (!context.isRenderable()) {
            return;
        }

        keyCallback.free();
        
        logger.fine("Keyboard destroyed.");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
    }

    @Override
    public long getInputTimeNanos() {
        return (long) (glfwGetTime() * 1000000000);
    }

    @Override
    public String getKeyName(int jmeKey) {
        int glfwKey = GlfwKeyMap.fromJmeKeyCode(jmeKey);
        return glfwGetKeyName(glfwKey, 0);
    }

}
