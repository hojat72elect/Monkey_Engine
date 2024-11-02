
package com.jme3.terrain.geomipmap.picking;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;

/**
 * Pick the location on the terrain from a given ray.
 *
 * @author Brent Owens
 */
public interface TerrainPicker {

    /**
     * Ask for the point of intersection between the given ray and the terrain.
     *
     * @param worldPick
     *            our pick ray, in world space.
     * @return The number of collisions found
     */
    public int getTerrainIntersection(final Ray worldPick, CollisionResults results);

}
