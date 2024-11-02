
package com.jme3.input.event;

import com.jme3.input.InputManager;
import com.jme3.input.JoystickAxis;

/**
 * Joystick axis event.
 *
 * @author Kirill Vainer, Paul Speed
 */
public class JoyAxisEvent extends InputEvent {

    private JoystickAxis axis;
    private float value;
    private float rawValue;

    /**
     * Creates a new event for a joystick axis.
     *
     * @param axis     - The axis that generated this event.
     * @param value    - The value of the axis.
     */
    public JoyAxisEvent(JoystickAxis axis, float value) {
        this(axis, value, value);
    }

    /**
     * Creates a new event for a joystick axis.
     *
     * @param axis     - The axis that generated this event.
     * @param value    - The value of the axis, after rescaling took place.
     * @param rawValue - The original axis value before it was rescaled by {@link com.jme3.input.JoystickCompatibilityMappings JoystickCompatibilityMappings}.
     */
    public JoyAxisEvent(JoystickAxis axis, float value, float rawValue) {
        this.axis = axis;
        this.value = value;
        this.rawValue = rawValue;
    }

    /**
     * Returns the JoystickAxis that triggered this event.
     *
     * @return the pre-existing instance
     * @see com.jme3.input.JoystickAxis#assignAxis(java.lang.String, java.lang.String)
     */
    public JoystickAxis getAxis() {
        return axis;
    }

    /**
     * Returns the joystick axis index.
     *
     * @return joystick axis index.
     * @see com.jme3.input.JoystickAxis#assignAxis(java.lang.String, java.lang.String)
     */
    public int getAxisIndex() {
        return axis.getAxisId();
    }

    /**
     * The joystick index.
     *
     * @return joystick index.
     * @see InputManager#getJoysticks()
     */
    public int getJoyIndex() {
        return axis.getJoystick().getJoyId();
    }

    /**
     * The value of the axis.
     *
     * @return value of the axis.
     */
    public float getValue() {
        return value;
    }

    /**
     * The value of the axis before it was remapped.
     *
     * @return value of the axis.
     */
    public float getRawValue() {
        return rawValue;
    }
}
