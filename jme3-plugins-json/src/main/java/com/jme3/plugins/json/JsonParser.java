
package com.jme3.plugins.json;

import java.io.InputStream;

/**
 * A json parser
 * 
 * @author Riccardo Balbo
 */
public interface JsonParser {
    /**
     * Parse a json input stream and returns a {@link JsonObject}
     * 
     * @param stream
     *            the stream to parse
     * @return the JsonObject
     */
    public JsonObject parse(InputStream stream);
}
