
package com.jme3.scene.plugins.fbx.anim;

public class FbxAnimUtil {
    /**
     * Conversion factor from FBX animation time unit to seconds.
     */
    public static final double SECONDS_PER_UNIT = 1 / 46186158000d;
    
    public static final String CURVE_NODE_PROPERTY_X          = "d|X";
    public static final String CURVE_NODE_PROPERTY_Y          = "d|Y";
    public static final String CURVE_NODE_PROPERTY_Z          = "d|Z";
    public static final String CURVE_NODE_PROPERTY_VISIBILITY = "d|Visibility";

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private FbxAnimUtil() {
    }
}
