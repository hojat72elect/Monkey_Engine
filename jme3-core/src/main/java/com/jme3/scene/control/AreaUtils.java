
package com.jme3.scene.control;

import com.jme3.bounding.BoundingVolume;

/**
 * <code>AreaUtils</code> is used to calculate the area of various objects, such as bounding volumes.  These
 * functions are very loose approximations.
 * @author Joshua Slack
 * @version $Id: AreaUtils.java 4131 2009-03-19 20:15:28Z blaine.dev $
 * @deprecated use {@link com.jme3.util.AreaUtils} instead, due to wrong package
 */
@Deprecated
public class AreaUtils {
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private AreaUtils() {
    }

    /**
    * Estimate the screen area of a bounding volume. If the volume isn't a
    * BoundingSphere, BoundingBox, or OrientedBoundingBox, 0 is returned.
    *
    * @param bound The bounds to calculate the volume from.
    * @param distance The distance from camera to object.
    * @param screenWidth The width of the screen.
    * @return The area in pixels on the screen of the bounding volume.
    */
    public static float calcScreenArea(BoundingVolume bound, float distance, float screenWidth) {
        return com.jme3.util.AreaUtils.calcScreenArea(bound, distance, screenWidth);
    }
}
