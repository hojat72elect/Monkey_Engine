
package com.jme3.terrain.geomipmap.lodcalc;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainPatch;
import com.jme3.terrain.geomipmap.UpdatedTerrainPatch;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Calculates the LOD of the terrain based on its distance from the
 * cameras. Taking the minimum distance from all cameras.
 *
 * @author bowens
 */
public class DistanceLodCalculator implements LodCalculator {

    private int size; // size of a terrain patch
    private float lodMultiplier = 2;
    private boolean turnOffLod = false;
    
    public DistanceLodCalculator() {
    }
    
    public DistanceLodCalculator(int patchSize, float multiplier) {
        this.size = patchSize;
        this.lodMultiplier = multiplier;
    }
    
    @Override
    public boolean calculateLod(TerrainPatch terrainPatch, List<Vector3f> locations, HashMap<String, UpdatedTerrainPatch> updates) {
        if (locations == null || locations.isEmpty())
            return false;// no camera yet
        float distance = getCenterLocation(terrainPatch).distance(locations.get(0));

        if (turnOffLod) {
            // set to full detail
            int prevLOD = terrainPatch.getLod();
            UpdatedTerrainPatch utp = updates.get(terrainPatch.getName());
            if (utp == null) {
                utp = new UpdatedTerrainPatch(terrainPatch);
                updates.put(utp.getName(), utp);
            }
            utp.setNewLod(0);
            utp.setPreviousLod(prevLOD);
            //utp.setReIndexNeeded(true);
            return true;
        }
        
        // go through each lod level to find the one we are in
        for (int i = 0; i <= terrainPatch.getMaxLod(); i++) {
            if (distance < getLodDistanceThreshold() * (i + 1)*terrainPatch.getWorldScaleCached().x || i == terrainPatch.getMaxLod()) {
                boolean reIndexNeeded = false;
                if (i != terrainPatch.getLod()) {
                    reIndexNeeded = true;
                    //System.out.println("lod change: "+lod+" > "+i+"    dist: "+distance);
                }
                int prevLOD = terrainPatch.getLod();
                
                UpdatedTerrainPatch utp = updates.get(terrainPatch.getName());
                if (utp == null) {
                    utp = new UpdatedTerrainPatch(terrainPatch);//save in here, do not update actual variables
                    updates.put(utp.getName(), utp);
                }
                utp.setNewLod(i);
                utp.setPreviousLod(prevLOD);
                //utp.setReIndexNeeded(reIndexNeeded);

                return reIndexNeeded;
            }
        }

        return false;
    }

    protected Vector3f getCenterLocation(TerrainPatch terrainPatch) {
        Vector3f loc = terrainPatch.getWorldTranslationCached();
        loc.x += terrainPatch.getSize()*terrainPatch.getWorldScaleCached().x / 2;
        loc.z += terrainPatch.getSize()*terrainPatch.getWorldScaleCached().z / 2;
        return loc;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(size, "patchSize", 32);
        oc.write(lodMultiplier, "lodMultiplier", 32);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        size = ic.readInt("patchSize", 32);
        lodMultiplier = ic.readFloat("lodMultiplier", 32f);
    }

    @Override
    public DistanceLodCalculator clone() {
        DistanceLodCalculator clone = new DistanceLodCalculator(size, lodMultiplier);
        return clone;
    }

    @Override
    public String toString() {
        return "DistanceLodCalculator "+size+"*"+lodMultiplier;
    }

    /**
     * Gets the camera distance where the LOD level will change
     */
    protected float getLodDistanceThreshold() {
        return size*lodMultiplier;
    }
    
    /**
     * Does this calculator require the terrain to have the difference of 
     * LOD levels of neighbours to be more than 1.
     */
    @Override
    public boolean usesVariableLod() {
        return false;
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
    public void turnOffLod() {
        turnOffLod = true;
    }
    
    @Override
    public boolean isLodOff() {
        return turnOffLod;
    }
    
    @Override
    public void turnOnLod() {
        turnOffLod = false;
    }
    
}
