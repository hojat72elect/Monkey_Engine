
package com.jme3.plugins.json;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A json parser factory that allows you to set the parser to use.
 * 
 * @author Riccardo Balbo
 */
public class Json {
    /**
     * The property name to set the parser to use.
     * Should be set automatically by the JmeSystemDelegate.
     * Note: changing this property after the first call to Json.create() will have no effect.
     */
    public static final String PROPERTY_JSON_PARSER_IMPLEMENTATION = "com.jme3.JsonParserImplementation";
    private static final Logger LOGGER = Logger.getLogger(Json.class.getName());
    private static final String DEFAULT_JSON_PARSER_IMPLEMENTATION = "com.jme3.plugins.gson.GsonParser";

    private static Class<? extends JsonParser> clazz = null;

    /**
     * Set the parser to use.
     * 
     * @param clazz
     *            a class that implements JsonParser
     */
    public static void setParser(Class<? extends JsonParser> clazz) {
        Json.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends JsonParser> findJsonParser(String className) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (final Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to access {0}", className);
        }

        if (clazz != null && !JsonParser.class.isAssignableFrom(clazz)) {
            LOGGER.log(Level.WARNING, "{0} does not implement {1}", new Object[] { className, JsonParser.class.getName() });
            clazz = null;
        }

        return (Class<? extends JsonParser>) clazz;
    }

    /**
     * Create a new JsonParser instance.
     * 
     * @return a new JsonParser instance
     */

    public static JsonParser create() {
        if (Json.clazz == null) {
            String userDefinedImpl = System.getProperty(PROPERTY_JSON_PARSER_IMPLEMENTATION, null);
            if (userDefinedImpl != null) {
                LOGGER.log(Level.FINE, "Loading user defined JsonParser implementation {0}", userDefinedImpl);
                Json.clazz = findJsonParser(userDefinedImpl);
            }
            if (Json.clazz == null) {
                LOGGER.log(Level.FINE, "No usable user defined JsonParser implementation found, using default implementation {0}", DEFAULT_JSON_PARSER_IMPLEMENTATION);
                Json.clazz = findJsonParser(DEFAULT_JSON_PARSER_IMPLEMENTATION);
            }
        }

        if (Json.clazz == null) {
            throw new RuntimeException("No JsonParser implementation found");
        }

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate JsonParser class " + clazz.getName(), e);
        }
    }
}
