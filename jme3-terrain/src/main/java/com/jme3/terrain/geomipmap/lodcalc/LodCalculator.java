
package com.jme3.terrain.geomipmap.lodcalc;

import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainPatch;
import com.jme3.terrain.geomipmap.UpdatedTerrainPatch;
import java.util.HashMap;
import java.util.List;

/**
 * Calculate the Level of Detail of a terrain patch based on the
 * cameras, or other locations.
 *
 * @author Brent Owens
 */
public interface LodCalculator extends Savable, Cloneable {

    public boolean calculateLod(TerrainPatch terrainPatch, List<Vector3f> locations, HashMap<String,UpdatedTerrainPatch> updates);
    
    public LodCalculator clone();
    
    public void turnOffLod();
    public void turnOnLod();
    public boolean isLodOff();

    /**
     * If true, then this calculator can cause neighbouring terrain chunks to 
     * have LOD levels that are greater than 1 apart.
     * Entropy algorithms will want to return true for this. Straight distance
     * calculations will just want to return false.
     */
    public boolean usesVariableLod();
}
