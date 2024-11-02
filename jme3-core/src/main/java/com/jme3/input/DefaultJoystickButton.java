
package com.jme3.input;

import com.jme3.input.controls.JoyButtonTrigger;

/**
 *  Default implementation of the JoystickButton interface.
 *
 *  @author Paul Speed
 */
public class DefaultJoystickButton implements JoystickButton {

    private final InputManager inputManager;
    private final Joystick parent;
    private final int buttonIndex;
    private final String name;
    private final String logicalId;

    public DefaultJoystickButton(InputManager inputManager, Joystick parent, int buttonIndex,
            String name, String logicalId) {
        this.inputManager = inputManager;
        this.parent = parent;
        this.buttonIndex = buttonIndex;
        this.name = name;
        this.logicalId = logicalId;
    }

    /**
     * Assign the mapping name to receive events from the given button index
     * on the joystick.
     *
     * @param mappingName The mapping to receive joystick button events.
     */
    @Override
    public void assignButton(String mappingName) {
        inputManager.addMapping(mappingName, new JoyButtonTrigger(parent.getJoyId(), buttonIndex));
    }

    /**
     *  Returns the joystick to which this axis object belongs.
     */
    @Override
    public Joystick getJoystick() {
        return parent;
    }

    /**
     *  Returns the name of this joystick.
     *
     *  @return the name of this joystick.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *  Returns the logical identifier of this joystick axis.
     *
     *  @return the logical identifier of this joystick.
     */
    @Override
    public String getLogicalId() {
        return logicalId;
    }

    /**
     *  Returns the unique buttonId of this joystick axis within a given
     *  InputManager context.
     *
     *  @return the buttonId of this joystick axis.
     */
    @Override
    public int getButtonId() {
        return buttonIndex;
    }

    @Override
    public String toString() {
        return "JoystickButton[name=" + getName() + ", parent=" + parent.getName() + ", id=" + getButtonId()
                                    + ", logicalId=" + getLogicalId() + "]";
    }
}
