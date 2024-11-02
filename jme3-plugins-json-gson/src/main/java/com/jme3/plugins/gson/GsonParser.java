
package com.jme3.plugins.gson;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.jme3.plugins.json.JsonObject;
import com.jme3.plugins.json.JsonParser;

/**
 * GSON implementation of {@link JsonParser}
 */
public class GsonParser implements JsonParser {
    @Override
    public JsonObject parse(InputStream stream) {
        return new GsonObject(com.google.gson.JsonParser.parseReader(new com.google.gson.stream.JsonReader(new InputStreamReader(stream))).getAsJsonObject());
    }
}
