

package com.jme3.terrain;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.FastMath;
import com.jme3.terrain.collision.BaseAWTTest;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test saving/loading terrain.
 *
 * @author Ali-RS
 */
public class TestTerrainExporting extends BaseAWTTest {

    /**
     * Test saving/loading a TerrainQuad.
     */
    @Test
    public void testTerrainExporting() {

        Texture heightMapImage = getAssetManager().loadTexture("Textures/Terrain/splat/mountains512.png");
        AbstractHeightMap map = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
        map.load();

        TerrainQuad terrain = new TerrainQuad("Terrain", 65, 513, map.getHeightMap());

        TerrainQuad saveAndLoad = BinaryExporter.saveAndLoad(getAssetManager(), terrain);

        Assert.assertEquals(513, saveAndLoad.getTotalSize());
        Assert.assertEquals(65, saveAndLoad.getPatchSize());
        Assert.assertArrayEquals(terrain.getHeightMap(), saveAndLoad.getHeightMap(), FastMath.ZERO_TOLERANCE);
    }

}
