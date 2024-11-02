
package com.jme3.input.event;

import com.jme3.input.KeyInput;

/**
 * Keyboard key event.
 *
 * @author Kirill Vainer
 */
public class KeyInputEvent extends InputEvent {

    private final int keyCode;
    private final char keyChar;
    private final boolean pressed;
    private final boolean repeating;

    public KeyInputEvent(int keyCode, char keyChar, boolean pressed, boolean repeating) {
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.pressed = pressed;
        this.repeating = repeating;
    }

    /**
     * Returns the key character. Returns 0 if the key has no character.
     *
     * @return the key character. 0 if the key has no character.
     */
    public char getKeyChar() {
        return keyChar;
    }

    /**
     * The key code.
     * <p>
     * See KEY_*** constants in {@link KeyInput}.
     *
     * @return key code.
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Returns true if this event is key press, false is it was a key release.
     *
     * @return true if this event is key press, false is it was a key release.
     */
    public boolean isPressed() {
        return pressed;
    }

    /**
     * Returns true if this event is a repeat event.
     *
     * @return true if this event is a repeat event
     */
    public boolean isRepeating() {
        return repeating;
    }

    /**
     * Returns true if this event is a key release, false if it was a key press.
     *
     * @return true if this event is a key release, false if it was a key press.
     */
    public boolean isReleased() {
        return !pressed;
    }

    @Override
    public String toString() {
        String str = "Key(CODE="+keyCode;
        if (keyChar != '\0')
            str = str + ", CHAR=" + keyChar;

        if (repeating) {
            return str + ", REPEATING)";
        } else if (pressed) {
            return str + ", PRESSED)";
        } else {
            return str + ", RELEASED)";
        }
    }
}
