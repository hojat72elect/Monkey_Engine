
package com.jme3.terrain.geomipmap.lodcalc;

import com.jme3.export.Savable;


/**
 * Calculates the LOD value based on where the camera is.
 * This is plugged into the Terrain system and any terrain
 * using LOD will use this to determine when a patch of the 
 * terrain should switch Levels of Detail.
 * 
 * @author bowens
 */
public interface LodThreshold extends Savable, Cloneable {

    /**
     * A distance of how far between each LOD threshold.
     */
    public float getLodDistanceThreshold();

    public LodThreshold clone();
}
