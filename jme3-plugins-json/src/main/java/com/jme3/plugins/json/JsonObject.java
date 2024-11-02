
package com.jme3.plugins.json;

import java.util.Map.Entry;

/**
 * A generic object/map
 * 
 * @author Riccardo Balbo
 */
public interface JsonObject extends JsonElement {

    /**
     * Returns the object property as a String
     * 
     * @param string
     *            name of the property
     * @return the string
     */
    public JsonArray getAsJsonArray(String string);

    /**
     * Returns the object property as a JsonObject
     * 
     * @param string
     *            name of the property
     * @return the JsonObject
     */
    public JsonObject getAsJsonObject(String string);

    /**
     * Check if the object has a property
     * 
     * @param string
     *            name of the property
     * @return true if it exists, false otherwise
     */
    public boolean has(String string);

    /**
     * Returns the object property as generic element
     * 
     * @param string
     *            name of the property
     * @return the element
     */
    public JsonElement get(String string);

    /**
     * Returns the object's key-value pairs
     * 
     * @return an array of key-value pairs
     */
    public Entry<String, JsonElement>[] entrySet();

    /**
     * Returns the object property as a wrapped primitive
     * 
     * @param string
     *            name of the property
     * @return the wrapped primitive
     */
    public JsonPrimitive getAsJsonPrimitive(String string);

}
