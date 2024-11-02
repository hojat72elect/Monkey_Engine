
package com.jme3.input.android;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import java.util.logging.Logger;

/**
 * <code>AndroidJoyInput14</code> extends <code>AndroidJoyInput</code>
 * to include support for physical joysticks/gamepads.
 *
 * @author iwgeric
 */
public class AndroidJoyInput14 extends AndroidJoyInput {
    private static final Logger logger = Logger.getLogger(AndroidJoyInput14.class.getName());

    private AndroidJoystickJoyInput14 joystickJoyInput;

    public AndroidJoyInput14(AndroidInputHandler inputHandler) {
        super(inputHandler);
        joystickJoyInput = new AndroidJoystickJoyInput14(this);
    }

    /**
     * Pauses the joystick device listeners to save battery life if they are not needed.
     * Used to pause when the activity pauses
     */
    @Override
    public void pauseJoysticks() {
        super.pauseJoysticks();

        if (joystickJoyInput != null) {
            joystickJoyInput.pauseJoysticks();
        }
    }

    /**
     * Resumes the joystick device listeners.
     * Used to resume when the activity comes to the top of the stack
     */
    @Override
    public void resumeJoysticks() {
        super.resumeJoysticks();
        if (joystickJoyInput != null) {
            joystickJoyInput.resumeJoysticks();
        }

    }

    @Override
    public void destroy() {
        super.destroy();
        if (joystickJoyInput != null) {
            joystickJoyInput.destroy();
        }
    }

    @Override
    public Joystick[] loadJoysticks(InputManager inputManager) {
        // load the simulated joystick for device orientation
        super.loadJoysticks(inputManager);
        // load physical gamepads/joysticks
        joystickList.addAll(joystickJoyInput.loadJoysticks(joystickList.size(), inputManager));
        // return the list of joysticks back to InputManager
        return joystickList.toArray( new Joystick[joystickList.size()] );
    }

    public boolean onGenericMotion(MotionEvent event) {
        return joystickJoyInput.onGenericMotion(event);
    }

    public boolean onKey(KeyEvent event) {
        return joystickJoyInput.onKey(event);
    }

}
