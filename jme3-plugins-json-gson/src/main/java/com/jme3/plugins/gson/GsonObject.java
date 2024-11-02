
package com.jme3.plugins.gson;

import java.util.Set;
import java.util.Map.Entry;

import com.jme3.plugins.json.JsonArray;
import com.jme3.plugins.json.JsonElement;
import com.jme3.plugins.json.JsonObject;
import com.jme3.plugins.json.JsonPrimitive;

/**
 * GSON implementation of {@link JsonObject}
 */
class GsonObject extends GsonElement implements JsonObject {

    GsonObject(com.google.gson.JsonObject gsonObject) {
        super(gsonObject);
    }
    
    private com.google.gson.JsonObject obj() {
        return (com.google.gson.JsonObject) element;
    }

    @Override
    public JsonArray getAsJsonArray(String string) {
        com.google.gson.JsonArray el = obj().getAsJsonArray(string);
        return isNull(el) ? null : new GsonArray(el);        
    }

    @Override
    public JsonObject getAsJsonObject(String string) {
        com.google.gson.JsonObject el = obj().getAsJsonObject(string);
        return isNull(el) ? null : new GsonObject(el);
    }

    @Override
    public boolean has(String string) {
        return obj().has(string);
    }

    @Override
    public JsonElement get(String string) {
        com.google.gson.JsonElement el = obj().get(string);
        return isNull(el)?null:new GsonElement(el).autoCast();
    }

    @Override
    public Entry<String, JsonElement>[] entrySet() {
        Set<Entry<String, com.google.gson.JsonElement>> entrySet = obj().entrySet();
        Entry<String, JsonElement>[] entries = new Entry[entrySet.size()];
        int i = 0;
        for (Entry<String, com.google.gson.JsonElement> entry : entrySet) {

            Entry<String, JsonElement> e = new Entry<String, JsonElement>() {
                @Override
                public String getKey() {
                    return entry.getKey();
                }

                @Override
                public GsonElement getValue() {
                    return new GsonElement(entry.getValue()).autoCast();
                }

                @Override
                public GsonElement setValue(JsonElement value) {
                    throw new UnsupportedOperationException("Unimplemented method 'setValue'");
                }
            };

            entries[i++] = e;
        }
        return entries;

    }

    @Override
    public JsonPrimitive getAsJsonPrimitive(String string) {
        com.google.gson.JsonPrimitive el= obj().getAsJsonPrimitive(string);
        return isNull(el) ? null : new GsonPrimitive(el);
    }
}