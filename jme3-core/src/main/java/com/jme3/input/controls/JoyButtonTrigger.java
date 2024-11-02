
package com.jme3.input.controls;

import com.jme3.input.Joystick;

public class JoyButtonTrigger implements Trigger {

    private final int joyId, buttonId;

    /**
     * Use {@link Joystick#assignButton(java.lang.String, int) } instead.
     *
     * @param joyId the ID of a joystick
     * @param buttonId the index of a joystick button
     */
    public JoyButtonTrigger(int joyId, int buttonId) {
        this.joyId = joyId;
        this.buttonId = buttonId;
    }

    public static int joyButtonHash(int joyId, int joyButton) {
        assert joyButton >= 0 && joyButton <= 255;
        return (2048 * joyId) | 1536 | (joyButton & 0xff);
    }

    public int getAxisId() {
        return buttonId;
    }

    public int getJoyId() {
        return joyId;
    }

    @Override
    public String getName() {
        return "JoyButton[joyId="+joyId+", axisId="+buttonId+"]";
    }

    @Override
    public int triggerHashCode() {
        return joyButtonHash(joyId, buttonId);
    }

}
