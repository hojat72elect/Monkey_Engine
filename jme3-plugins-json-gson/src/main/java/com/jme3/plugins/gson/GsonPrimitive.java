
package com.jme3.plugins.gson;

import com.jme3.plugins.json.JsonPrimitive;

/**
 * GSON implementation of {@link JsonPrimitive}
 */
public class GsonPrimitive extends GsonElement implements JsonPrimitive {
        
    public GsonPrimitive(com.google.gson.JsonPrimitive element) {
        super(element);
    }

    private com.google.gson.JsonPrimitive prim() {
        return (com.google.gson.JsonPrimitive) element;
    } 

    @Override
    public boolean isNumber() {
        return prim().isNumber();
    }

    @Override
    public boolean isBoolean() {
        return prim().isBoolean();
    }

    @Override
    public boolean isString() {
        return prim().isString();
    }
    
}
