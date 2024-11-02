
package com.jme3.plugins.json;

/**
 * Represents an array.
 * 
 * @author Riccardo Balbo
 */
public interface JsonArray extends Iterable<JsonElement> {
    /**
     * Get the element at index i
     * 
     * @param i
     *            index
     * @return the element
     */
    public JsonElement get(int i);

    /**
     * Get the size of the array
     * 
     * @return the size
     */
    public int size();
}
