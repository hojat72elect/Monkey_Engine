
package com.jme3.input.lwjgl;

import com.jme3.input.*;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;

/**
 * The LWJGL implementation of {@link JoyInput}.
 *
 * @author Daniel Johansson (dannyjo)
 * @since 3.1
 */
public class GlfwJoystickInput implements JoyInput {

    private static final Logger LOGGER = Logger.getLogger(GlfwJoystickInput.class.getName());

    private RawInputListener listener;

    private final Map<Integer, GlfwJoystick> joysticks = new HashMap<>();

    private final Map<JoystickButton, Boolean> joyButtonPressed = new HashMap<>();

    private boolean initialized = false;

    @Override
    public void setJoyRumble(final int joyId, final float amount) {
        if (joyId >= joysticks.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void fireJoystickConnectedEvent(int jid) {
        Joystick joystick = joysticks.get(jid);
        ((InputManager)listener).fireJoystickConnectedEvent(joystick);
    }

    public void fireJoystickDisconnectedEvent(int jid) {
        Joystick joystick = joysticks.get(jid);
        ((InputManager)listener).fireJoystickDisconnectedEvent(joystick);
    }

    public void reloadJoysticks() {
        joysticks.clear();

        InputManager inputManager = (InputManager) listener;

        Joystick[] joysticks = loadJoysticks(inputManager);
        inputManager.setJoysticks(joysticks);
    }

    @Override
    public Joystick[] loadJoysticks(final InputManager inputManager) {

        for (int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
            if (glfwJoystickPresent(i)) {
                final String name = glfwGetJoystickName(i);
                final GlfwJoystick joystick = new GlfwJoystick(inputManager, this, i, name);
                joysticks.put(i, joystick);

                final FloatBuffer floatBuffer = glfwGetJoystickAxes(i);

                int axisIndex = 0;
                while (floatBuffer.hasRemaining()) {
                    floatBuffer.get();

                    final String logicalId = JoystickCompatibilityMappings.remapAxis(joystick.getName(), convertAxisIndex(axisIndex));
                    final JoystickAxis joystickAxis = new DefaultJoystickAxis(inputManager, joystick, axisIndex, convertAxisIndex(axisIndex), logicalId, true, false, 0.0f);
                    joystick.addAxis(axisIndex, joystickAxis);
                    axisIndex++;
                }

                final ByteBuffer byteBuffer = glfwGetJoystickButtons(i);

                if (byteBuffer != null) {
                    int buttonIndex = 0;
                    while (byteBuffer.hasRemaining()) {
                        byteBuffer.get();

                        final String logicalId = JoystickCompatibilityMappings.remapButton(joystick.getName(), String.valueOf(buttonIndex));
                        final JoystickButton button = new DefaultJoystickButton(inputManager, joystick, buttonIndex, String.valueOf(buttonIndex), logicalId);
                        joystick.addButton(button);
                        joyButtonPressed.put(button, false);
                        buttonIndex++;
                    }
                }
            }
        }

        return joysticks.values().toArray(new GlfwJoystick[joysticks.size()]);
    }

    private String convertAxisIndex(final int index) {
        if (index == 0) {
            return "pov_x";
        } else if (index == 1) {
            return "pov_y";
        } else if (index == 2) {
            return "z";
        } else if (index == 3) {
            return "rz";
        }

        return String.valueOf(index);
    }

    @Override
    public void initialize() {
        initialized = true;
    }

    @Override
    public void update() {
        float rawValue, value;
        for (final Map.Entry<Integer, GlfwJoystick> entry : joysticks.entrySet()) {

            // Axes
            final FloatBuffer axisValues = glfwGetJoystickAxes(entry.getKey());

            // if a joystick is added or removed, the callback reloads the joysticks.
            // when the callback is called and reloads the joystick, this iterator may already have started iterating.
            // To avoid a NullPointerException we null-check the axisValues and bytebuffer objects.
            // If the joystick it's iterating over no-longer exists it will return null.

            if (axisValues != null) {
                for (final JoystickAxis axis : entry.getValue().getAxes()) {
                    rawValue = axisValues.get(axis.getAxisId());
                    value = JoystickCompatibilityMappings.remapAxisRange(axis, rawValue);
                    listener.onJoyAxisEvent(new JoyAxisEvent(axis, value, rawValue));
                }
            }

            // Buttons
            final ByteBuffer byteBuffer = glfwGetJoystickButtons(entry.getKey());

            if (byteBuffer != null) {
                for (final JoystickButton button : entry.getValue().getButtons()) {
                    final boolean pressed = byteBuffer.get(button.getButtonId()) == GLFW_PRESS;

                    if (joyButtonPressed.get(button) != pressed) {
                        joyButtonPressed.put(button, pressed);
                        listener.onJoyButtonEvent(new JoyButtonEvent(button, pressed));
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {
        initialized = false;
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
        return 0;
    }

    protected class GlfwJoystick extends AbstractJoystick {

        private JoystickAxis povAxisX;
        private JoystickAxis povAxisY;

        public GlfwJoystick(final InputManager inputManager, final JoyInput joyInput, final int joyId, final String name) {
            super(inputManager, joyInput, joyId, name);
        }

        public void addAxis(final int index, final JoystickAxis axis) {
            super.addAxis(axis);

            if (index == 0) {
                povAxisX = axis;
            } else if (index == 1) {
                povAxisY = axis;
            }
        }

        @Override
        protected void addButton(final JoystickButton button) {
            super.addButton(button);
        }

        @Override
        public JoystickAxis getXAxis() {
            return povAxisX;
        }

        @Override
        public JoystickAxis getYAxis() {
            return povAxisY;
        }

        @Override
        public JoystickAxis getPovXAxis() {
            return povAxisX;
        }

        @Override
        public JoystickAxis getPovYAxis() {
            return povAxisY;
        }

        @Override
        public int getXAxisIndex() {
            return povAxisX.getAxisId();
        }

        @Override
        public int getYAxisIndex() {
            return povAxisY.getAxisId();
        }
    }
}
