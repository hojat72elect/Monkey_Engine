
package com.jme3.terrain.geomipmap.lodcalc;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.io.IOException;


/**
 * Just multiplies the terrain patch size by 2. So every two
 * patches away the camera is, the LOD changes.
 * 
 * Set it higher to have the LOD change less frequently.
 * 
 * @author bowens
 */
public class SimpleLodThreshold implements LodThreshold {

    private int size; // size of a terrain patch
    private float lodMultiplier = 2;

    
    public SimpleLodThreshold() {
    }
    
    public SimpleLodThreshold(Terrain terrain) {
        if (terrain instanceof TerrainQuad)
            this.size = ((TerrainQuad)terrain).getPatchSize();
    }

    public SimpleLodThreshold(int patchSize, float lodMultiplier) {
        this.size = patchSize;
    }

    public float getLodMultiplier() {
        return lodMultiplier;
    }

    public void setLodMultiplier(float lodMultiplier) {
        this.lodMultiplier = lodMultiplier;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public float getLodDistanceThreshold() {
        return size*lodMultiplier;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(size, "size", 16);
        oc.write(lodMultiplier, "lodMultiplier", 2);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        size = ic.readInt("size", 16);
        lodMultiplier = ic.readInt("lodMultiplier", 2);
    }

    @Override
    public LodThreshold clone() {
        SimpleLodThreshold clone = new SimpleLodThreshold();
        clone.size = size;
        clone.lodMultiplier = lodMultiplier;
        
        return clone;
    }

    @Override
    public String toString() {
        return "SimpleLodThreshold "+size+", "+lodMultiplier;
    }
}
