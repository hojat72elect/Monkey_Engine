
package com.jme3.input.controls;

/**
 * A trigger represents a physical input, such as a keyboard key, a mouse
 * button, or joystick axis.
 */
public interface Trigger {

    /**
     * @return A user friendly name for the trigger.
     */
    public String getName();
    
    /**
     * Returns the hash code for the trigger.
     * 
     * @return the hash code for the trigger.
     */
    public int triggerHashCode();
}
