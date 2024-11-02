
package com.jme3.terrain.geomipmap;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.util.SafeArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An extension of the TerrainLodControl that handles
 * multiple terrains at once. This is to be used if you 
 * have your own tiling/paging terrain system, such as
 * TerrainGrid.
 * 
 * @author Brent Owens
 */
public class MultiTerrainLodControl extends TerrainLodControl {

    private SafeArrayList<TerrainQuad> terrains;

    private List<TerrainQuad> addedTerrains;
    private List<TerrainQuad> removedTerrains;

    public MultiTerrainLodControl() {
        terrains = new SafeArrayList<>(TerrainQuad.class);
        removedTerrains = new ArrayList<>();
        addedTerrains = new ArrayList<>();
    }

    public MultiTerrainLodControl(final Terrain terrain) {
        this();
        setTerrain(terrain);
    }

    public MultiTerrainLodControl(final Camera camera) {
        this();
        setCamera(camera);
    }

    public MultiTerrainLodControl(final Terrain terrain, final Camera camera) {
        this(terrain);
        setCamera(camera);
    }

    public MultiTerrainLodControl(final Terrain terrain, final List<Camera> cameras) {
        this(terrain);
        setCameras(cameras);
    }

    @Override
    protected DistanceLodCalculator makeLodCalculator() {
        return new DistanceLodCalculator(65, 2.7f);
    }
    
    /**
     * Add a terrain that will have its LOD handled by this control.
     * It will be added next update run. You should only call this from
     * the render thread.
     */
    public void addTerrain(TerrainQuad tq) {
        addedTerrains.add(tq);
    }
    
    /**
     * Add a terrain that will no longer have its LOD handled by this control.
     * It will be removed next update run. You should only call this from
     * the render thread.
     */
    public void removeTerrain(TerrainQuad tq) {
        removedTerrains.add(tq);
    }
    
    @Override
    protected UpdateLOD createLodUpdateTask(final List<Vector3f> locations,
                                            final LodCalculator lodCalculator) {
        return new UpdateMultiLOD(locations, lodCalculator);
    }
    
    @Override
    protected void prepareTerrain() {
        if (!addedTerrains.isEmpty()) {
            for (TerrainQuad t : addedTerrains) {
                if (!terrains.contains(t)) {
                    terrains.add(t);
                }
            }
            addedTerrains.clear();
        }
        
        if (!removedTerrains.isEmpty()) {
            terrains.removeAll(removedTerrains);
            removedTerrains.clear();
        }
        
        for (TerrainQuad terrain : terrains.getArray()) {
            // cache the terrain's world transforms so they can be accessed on the separate thread safely
            terrain.cacheTerrainTransforms();
        }
    }
    
    /**
     * Overrides the parent UpdateLOD runnable to process
     * multiple terrains.
     */
    protected class UpdateMultiLOD extends UpdateLOD {

        protected UpdateMultiLOD(final List<Vector3f> camLocations, final LodCalculator lodCalculator) {
            super(camLocations, lodCalculator);
        }
        
        @Override
        public HashMap<String, UpdatedTerrainPatch> call() throws Exception {

            HashMap<String, UpdatedTerrainPatch> updated = new HashMap<>();
            
            for (TerrainQuad terrainQuad : terrains) {
                // go through each patch and calculate its LOD based on camera distance
                terrainQuad.calculateLod(camLocations, updated, lodCalculator); // 'updated' gets populated here
            }
            
            for (TerrainQuad terrainQuad : terrains) {
                // then calculate the neighbour LOD values for seaming
                terrainQuad.findNeighboursLod(updated);
            }
            
            for (TerrainQuad terrainQuad : terrains) {
                // check neighbour quads that need their edges seamed
                terrainQuad.fixEdges(updated);
            }
            
            for (TerrainQuad terrainQuad : terrains) {
                // perform the edge seaming, if it requires it
                terrainQuad.reIndexPages(updated, lodCalculator.usesVariableLod());
            }
            
            //setUpdateQuadLODs(updated); // set back to main ogl thread
            lodCalcRunning.set(false);
            
            return updated;
        }
    }
}
