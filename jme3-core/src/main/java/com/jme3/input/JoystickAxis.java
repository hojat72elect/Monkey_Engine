
package com.jme3.input;

/**
 *  Represents a single axis of a Joystick.
 *
 *  @author Paul Speed
 */
public interface JoystickAxis {

    public static final String X_AXIS = "x";
    public static final String Y_AXIS = "y";
    public static final String Z_AXIS = "z";
    public static final String Z_ROTATION = "rz";
    public static final String LEFT_TRIGGER = "rx";
    public static final String RIGHT_TRIGGER = "ry";
    
    // Note: the left/right trigger bit may be a bit controversial in
    // the sense that this is one case where XBox controllers make a lot
    // more sense.
    // I've seen the following mappings for various things:
    // 
    // Axis          | XBox  | Non-Xbox (generally) (includes actual Sony PS4 controllers)
    // --------------+-------+---------------
    // left trigger  | z     | rx   (also button 6)
    // right trigger | rz    | ry   (also button 7)
    // left stick x  | x     | x
    // left stick y  | y     | y
    // right stick x | rx    | z
    // right stick y | ry    | rz
    //
    // The issue is that in all cases I've seen, the XBox controllers will
    // use the name "xbox" somewhere in their name.  The Non-XBox controllers
    // never mention anything uniform... even the PS4 controller only calls
    // itself "Wireless Controller".  In that light, it seems easier to make
    // the default the ugly case and the "XBox" way the exception because it
    // can more easily be identified.

    public static final String POV_X = "pov_x";
    public static final String POV_Y = "pov_y";

    /**
     *  Assign the mappings to receive events from the given joystick axis.
     *
     *  @param positiveMapping The mapping to receive events when the axis is negative
     *  @param negativeMapping The mapping to receive events when the axis is positive
     */
    public void assignAxis(String positiveMapping, String negativeMapping);
    
    /**
     *  Returns the joystick to which this axis object belongs.
     *
     * @return the pre-existing instance
     */
    public Joystick getJoystick(); 

    /**
     *  Returns the name of this joystick.
     *
     *  @return the name of this joystick.
     */
    public String getName(); 

    /**
     *  Returns the logical identifier of this joystick axis.
     *
     *  @return the logical identifier of this joystick.
     */
    public String getLogicalId(); 

    /**
     *  Returns the unique axisId of this joystick axis within a given 
     *  InputManager context.
     *
     *  @return the axisId of this joystick axis.
     */
    public int getAxisId(); 

    /**
     *  Returns true if this is an analog axis, meaning the values
     *  are a continuous range instead of 1, 0, and -1.
     *
     * @return true if analog, otherwise false
     */
    public boolean isAnalog(); 
    
    /**
     *  Returns true if this axis presents relative values.
     * 
     * @return true if relative, otherwise false
     */
    public boolean isRelative(); 
    
    /**
     *  Returns the suggested dead zone for this axis.  Values less than this
     *  can be safely ignored.
     * 
     * @return the radius of the dead zone
     */
    public float getDeadZone();
}
