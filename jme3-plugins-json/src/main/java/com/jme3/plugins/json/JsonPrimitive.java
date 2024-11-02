
package com.jme3.plugins.json;

/**
 * A wrapped primitive
 * 
 * @author Riccardo Balbo
 */
public interface JsonPrimitive {
    /**
     * Returns the wrapped primitive as a float
     * 
     * @return the float value
     */
    public float getAsFloat();

    /**
     * Returns the wrapped primitive as an int
     * 
     * @return the int value
     */
    public int getAsInt();

    /*
     * Returns the wrapped primitive as a boolean
     * 
     * @return the boolean value
     */
    public boolean getAsBoolean();
   
    /**
     * Check if the wrapped primitive is a number
     * @return true if it is a number
     */
    public boolean isNumber();

    /**
     * Check if the wrapped primitive is a boolean
     * @return true if it is a boolean
     */
    public boolean isBoolean();

    /**
     * Check if the wrapped primitive is a string
     * @return true if it is a string
     */
    public boolean isString();

    /**
     * Returns the wrapped primitive as a string
     * @return the string value
     */
    public String getAsString();

    /**
     * Returns the wrapped primitive as a generic number 
     * @return the number value
     */
    public Number getAsNumber();
}
