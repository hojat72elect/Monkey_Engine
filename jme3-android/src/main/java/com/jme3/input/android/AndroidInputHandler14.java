

package com.jme3.input.android;

import android.opengl.GLSurfaceView;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import java.util.logging.Logger;

/**
 * <code>AndroidInputHandler14</code> extends <code>AndroidInputHandler</code> to
 * add the onHover and onGenericMotion events that where added in Android rev 14 (Android 4.0).<br>
 * The onGenericMotion events are the main interface to Joystick axes.  They
 * were actually released in Android rev 12.
 *
 * @author iwgeric
 */
public class AndroidInputHandler14 extends AndroidInputHandler implements View.OnHoverListener,
                                                                            View.OnGenericMotionListener {

    private static final Logger logger = Logger.getLogger(AndroidInputHandler14.class.getName());

    public AndroidInputHandler14() {
        touchInput = new AndroidTouchInput14(this);
        joyInput = new AndroidJoyInput14(this);
    }

    @Override
    protected void removeListeners(GLSurfaceView view) {
        super.removeListeners(view);
        view.setOnHoverListener(null);
        view.setOnGenericMotionListener(null);
    }

    @Override
    protected void addListeners(GLSurfaceView view) {
        super.addListeners(view);
        view.setOnHoverListener(this);
        view.setOnGenericMotionListener(this);
    }

    @Override
    public boolean onHover(View view, MotionEvent event) {
        if (view != getView()) {
            return false;
        }

        boolean consumed = false;

        int source = event.getSource();
//        logger.log(Level.INFO, "onTouch source: {0}", source);

        boolean isTouch = ((source & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN);
//        logger.log(Level.INFO, "onTouch source: {0}, isTouch: {1}",
//                new Object[]{source, isTouch});

        if (isTouch && touchInput != null) {
            // send the event to the touch processor
            consumed = ((AndroidTouchInput14)touchInput).onHover(event);
        }

        return consumed;
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent event) {
        if (view != getView()) {
            return false;
        }

        boolean consumed = false;

        int source = event.getSource();
//        logger.log(Level.INFO, "onGenericMotion source: {0}", source);

        boolean isJoystick =
                ((source & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                ((source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK);

        if (isJoystick && joyInput != null) {
//            logger.log(Level.INFO, "onGenericMotion source: {0}, isJoystick: {1}",
//                    new Object[]{source, isJoystick});
            // send the event to the touch processor
            consumed = consumed || ((AndroidJoyInput14)joyInput).onGenericMotion(event);
        }

        return consumed;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (view != getView()) {
            return false;
        }

        boolean consumed = false;

//        logger.log(Level.INFO, "onKey keyCode: {0}, action: {1}, event: {2}",
//                new Object[]{KeyEvent.keyCodeToString(keyCode), event.getAction(), event});
        int source = event.getSource();
//        logger.log(Level.INFO, "onKey source: {0}", source);

        boolean isTouch =
                ((source & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN) ||
                ((source & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD);
        boolean isJoystick =
                ((source & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                ((source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK);
        boolean isUnknown =
                (source & android.view.InputDevice.SOURCE_UNKNOWN) == android.view.InputDevice.SOURCE_UNKNOWN;

        if (touchInput != null && (isTouch || (isUnknown && this.touchInput.isSimulateKeyboard()))) {
//            logger.log(Level.INFO, "onKey source: {0}, isTouch: {1}",
//                    new Object[]{source, isTouch});
            consumed = touchInput.onKey(event);
        }
        if (isJoystick && joyInput != null) {
//            logger.log(Level.INFO, "onKey source: {0}, isJoystick: {1}",
//                    new Object[]{source, isJoystick});
            // use inclusive OR to make sure the onKey method is called.
            consumed = consumed | ((AndroidJoyInput14)joyInput).onKey(event);
        }

        return consumed;

    }

}
