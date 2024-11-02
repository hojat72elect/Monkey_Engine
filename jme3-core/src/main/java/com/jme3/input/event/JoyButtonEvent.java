
package com.jme3.input.event;

import com.jme3.input.Joystick;
import com.jme3.input.JoystickButton;

/**
 * Joystick button event.
 * 
 * @author Kirill Vainer, Paul Speed
 */
public class JoyButtonEvent extends InputEvent {

    private final JoystickButton button;
    private final boolean pressed;

    public JoyButtonEvent(JoystickButton button, boolean pressed) {
        this.button = button;
        this.pressed = pressed;
    }

    /**
     * Returns the JoystickButton that triggered this event.
     *
     * @see com.jme3.input.JoystickAxis#assignAxis(java.lang.String, java.lang.String)
     * @return the button
     */
    public JoystickButton getButton() {
        return button;
    }

    /**
     * The button index.
     * 
     * @return button index.
     * 
     * @see Joystick#assignButton(java.lang.String, int) 
     */
    public int getButtonIndex() {
        return button.getButtonId();
    }

    /**
     * The joystick index.
     * 
     * @return joystick index.
     * 
     * @see com.jme3.input.InputManager#getJoysticks() 
     */
    public int getJoyIndex() {
        return button.getJoystick().getJoyId();
    }

    /**
     * Returns true if the event was a button press,
     * returns false if the event was a button release.
     * 
     * @return true if the event was a button press,
     * false if the event was a button release.
     */
    public boolean isPressed() {
        return pressed;
    }



}
