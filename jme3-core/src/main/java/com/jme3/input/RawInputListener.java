
package com.jme3.input;

import com.jme3.input.event.*;

/**
 * An interface used for receiving raw input from devices.
 */
public interface RawInputListener {

    /**
     * Called before a batch of input will be sent to this
     * <code>RawInputListener</code>.
     */
    public void beginInput();

    /**
     * Called after a batch of input was sent to this
     * <code>RawInputListener</code>.
     *
     * The listener should set the {@link InputEvent#setConsumed() consumed flag}
     * on any events that have been consumed either at this call or previous calls.
     */
    public void endInput();

    /**
     * Invoked on joystick axis events.
     *
     * @param evt information about the event
     */
    public void onJoyAxisEvent(JoyAxisEvent evt);

    /**
     * Invoked on joystick button presses.
     *
     * @param evt information about the event
     */
    public void onJoyButtonEvent(JoyButtonEvent evt);

    /**
     * Invoked on mouse movement/motion events.
     *
     * @param evt information about the event
     */
    public void onMouseMotionEvent(MouseMotionEvent evt);

    /**
     * Invoked on mouse button events.
     *
     * @param evt information about the event
     */
    public void onMouseButtonEvent(MouseButtonEvent evt);

    /**
     * Invoked on keyboard key press or release events.
     *
     * @param evt information about the event
     */
    public void onKeyEvent(KeyInputEvent evt);


    /**
     * Invoked on touchscreen touch events.
     *
     * @param evt information about the event
     */
    public void onTouchEvent(TouchEvent evt);

}
