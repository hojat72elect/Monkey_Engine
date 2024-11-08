
package com.jme3.terrain.geomipmap.grid;


import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainGridTileLoader;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.noise.Basis;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 *
 * , normenhansen
 */
public class FractalTileLoader implements TerrainGridTileLoader{
            
    public class FloatBufferHeightMap extends AbstractHeightMap {

        private final FloatBuffer buffer;

        public FloatBufferHeightMap(FloatBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public boolean load() {
            this.heightData = this.buffer.array();
            return true;
        }

    }

    private int patchSize;
    private int quadSize;
    private final Basis base;
    private final float heightScale;

    public FractalTileLoader(Basis base, float heightScale) {
        this.base = base;
        this.heightScale = heightScale;
    }

    private HeightMap getHeightMapAt(Vector3f location) {
        AbstractHeightMap heightmap = null;
        
        FloatBuffer buffer = this.base.getBuffer(location.x * (this.quadSize - 1), location.z * (this.quadSize - 1), 0, this.quadSize);

        float[] arr = buffer.array();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] * this.heightScale;
        }
        heightmap = new FloatBufferHeightMap(buffer);
        heightmap.load();
        return heightmap;
    }

    @Override
    public TerrainQuad getTerrainQuadAt(Vector3f location) {
        HeightMap heightMapAt = getHeightMapAt(location);
        TerrainQuad q = new TerrainQuad("Quad" + location, patchSize, quadSize, heightMapAt == null ? null : heightMapAt.getHeightMap());
        return q;
    }

    @Override
    public void setPatchSize(int patchSize) {
        this.patchSize = patchSize;
    }

    @Override
    public void setQuadSize(int quadSize) {
        this.quadSize = quadSize;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        //TODO: serialization
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        //TODO: serialization
    }    
}
