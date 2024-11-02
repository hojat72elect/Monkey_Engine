
package com.jme3.plugins.json;

/**
 * A generic element
 * 
 * @author Riccardo Balbo
 */
public interface JsonElement {

    /**
     * Returns the object as a String
     * 
     * @return the string
     */
    public String getAsString();

    /**
     * Returns the object as a JsonObject
     * 
     * @return the JsonObject
     */
    public JsonObject getAsJsonObject();

    /**
     * Returns the object as a float
     * 
     * @return the float
     */
    public float getAsFloat();

    /**
     * Returns the object as an int
     * 
     * @return the int
     */
    public int getAsInt();

    /**
     * Returns the object as a boolean
     * 
     * @return the boolean
     */
    public boolean getAsBoolean();

    /**
     * Returns the object as a JsonArray
     * 
     * @return the JsonArray
     */
    public JsonArray getAsJsonArray();

    /**
     * Returns the object as a Number
     * @return the Number
     */
    public Number getAsNumber();


    /**
     * Returns the object as a JsonPrimitive
     * @return the json primitive
     */
    public JsonPrimitive getAsJsonPrimitive();

    /**
     * Cast this JsonElement to a specific type
     * @return the casted JsonElement
     */
    public <T extends JsonElement> T autoCast();
}
