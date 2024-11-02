

package com.jme3.input.lwjgl;

import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.system.lwjgl.LwjglWindow;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * The LWJGL implementation of {@link KeyInput}.
 */
public class GlfwKeyInput implements KeyInput {

    private static final Logger logger = Logger.getLogger(GlfwKeyInput.class.getName());

    /**
     * The queue of key events.
     */
    private final Queue<KeyInputEvent> keyInputEvents = new ArrayDeque<>();

    /**
     * The LWJGL context.
     */
    private final LwjglWindow context;

    /**
     * The key callback.
     */
    private GLFWKeyCallback keyCallback;

    /**
     * The char callback.
     */
    private GLFWCharCallback charCallback;

    /**
     * The raw input listener.
     */
    private RawInputListener listener;

    private boolean initialized;

    public GlfwKeyInput(final LwjglWindow context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        if (!context.isRenderable()) {
            return;
        }
        initCallbacks();

        initialized = true;
        logger.fine("Keyboard created.");
    }

    /**
     * Re-initializes the key input context window specific callbacks
     */
    public void resetContext() {
        if (!context.isRenderable()) {
            return;
        }

        closeCallbacks();
        initCallbacks();
    }

    private void closeCallbacks() {
        keyCallback.close();
        charCallback.close();
    }

    /**
     * Determine the name of the specified key in the current system language.
     *
     * @param jmeKey the keycode from {@link com.jme3.input.KeyInput}
     * @return the name of the key, or null if unknown
     */
    @Override
    public String getKeyName(int jmeKey) {
        int glfwKey = GlfwKeyMap.fromJmeKeyCode(jmeKey);
        if (glfwKey == GLFW_KEY_UNKNOWN) {
            return null;
        }

        String result = glfwGetKeyName(glfwKey, 0);
        return result;
    }

    private void initCallbacks() {
        glfwSetKeyCallback(context.getWindowHandle(), keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(final long window, final int key, final int scancode, final int action, final int mods) {

                if (key < 0 || key > GLFW_KEY_LAST) {
                    return;
                }

                int jmeKey = GlfwKeyMap.toJmeKeyCode(key);

                final KeyInputEvent event = new KeyInputEvent(jmeKey, '\0', GLFW_PRESS == action, GLFW_REPEAT == action);
                event.setTime(getInputTimeNanos());

                keyInputEvents.add(event);
            }
        });

        glfwSetCharCallback(context.getWindowHandle(), charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(final long window, final int codepoint) {

                final char keyChar = (char) codepoint;

                final KeyInputEvent pressed = new KeyInputEvent(KeyInput.KEY_UNKNOWN, keyChar, true, false);
                pressed.setTime(getInputTimeNanos());

                keyInputEvents.add(pressed);

                final KeyInputEvent released = new KeyInputEvent(KeyInput.KEY_UNKNOWN, keyChar, false, false);
                released.setTime(getInputTimeNanos());

                keyInputEvents.add(released);
            }
        });
    }

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

        closeCallbacks();
        logger.fine("Keyboard destroyed.");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInputListener(final RawInputListener listener) {
        this.listener = listener;
    }

    @Override
    public long getInputTimeNanos() {
        return (long) (glfwGetTime() * 1000000000);
    }
}
