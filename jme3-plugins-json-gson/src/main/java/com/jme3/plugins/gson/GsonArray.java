
package com.jme3.plugins.gson;

import java.util.Iterator;

import com.jme3.plugins.json.JsonArray;
import com.jme3.plugins.json.JsonElement;

/**
 * GSON implementation of {@link JsonArray}.
 */
class GsonArray extends GsonElement implements JsonArray {

    GsonArray(com.google.gson.JsonElement element) {
        super(element);
    }

    private com.google.gson.JsonArray arr() {
        return element.getAsJsonArray();
    }

    @Override
    public Iterator<JsonElement> iterator() {
        return new Iterator<JsonElement>() {
            Iterator<com.google.gson.JsonElement> it = arr().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public JsonElement next() {
                return new GsonElement(it.next()).autoCast();
            }
        };
    }

    @Override
    public JsonElement get(int i) {
        com.google.gson.JsonElement el = arr().get(i);
        return isNull(el)?null:new GsonElement(el).autoCast();
    }

    @Override
    public int size() {
        return arr().size();
    }

}
