
package com.jme3.plugins.gson;

import com.jme3.plugins.json.JsonArray;
import com.jme3.plugins.json.JsonElement;
import com.jme3.plugins.json.JsonObject;
import com.jme3.plugins.json.JsonPrimitive;

import java.util.Objects;

/**
 * GSON implementation of {@link JsonElement}
 */
class GsonElement implements JsonElement {
    com.google.gson.JsonElement element;

    GsonElement(com.google.gson.JsonElement element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GsonElement other = (GsonElement) obj;
        return Objects.equals(this.element, other.element);
    }
    
    protected boolean isNull(com.google.gson.JsonElement element) {
        if (element == null) return true;
        if (element.isJsonNull()) return true;
        return false;
    }
    
    @Override
    public String getAsString() {
        return element.getAsString();
    }

    @Override
    public JsonObject getAsJsonObject() {
        return new GsonObject(element.getAsJsonObject());
    }

    @Override
    public float getAsFloat() {
        return element.getAsFloat();
    }

    @Override
    public int getAsInt() {
        return element.getAsInt();
    }

    @Override
    public Number getAsNumber() {
        return element.getAsNumber();        
    }

    @Override
    public boolean getAsBoolean() {
        return element.getAsBoolean();
    }

    @Override
    public JsonArray getAsJsonArray() {
        return new GsonArray(element.getAsJsonArray());
    }

    @Override
    public JsonPrimitive getAsJsonPrimitive() {
        return new GsonPrimitive(element.getAsJsonPrimitive());
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonElement> T autoCast() {
        if(isNull(element)) return null;
        if (element.isJsonArray()) return (T) new GsonArray(element.getAsJsonArray());
        if (element.isJsonObject()) return (T) new GsonObject(element.getAsJsonObject());
        if (element.isJsonPrimitive()) return (T) new GsonPrimitive(element.getAsJsonPrimitive());
        return (T) new GsonElement(element);
    }
    
}
