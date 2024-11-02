
package com.jme3.scene.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.TestUtil;
import com.jme3.texture.Image;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OBJLoaderTest {
    private AssetManager assetManager;
    
    @Before
    public void init() {
        assetManager = TestUtil.createAssetManager();
        // texture loaders are outside of core, so creating stub
        assetManager.registerLoader(PngLoaderStub.class, "png");
        
    }
    
    @Test
    public void testHappyPath() {
        Node scene = (Node) assetManager.loadModel(new ModelKey("OBJLoaderTest/TwoChairs.obj"));
        String sceneAsString = toDiffFriendlyString("", scene);
        System.out.println(sceneAsString);
        String expectedText = "" +
            // generated root name (as before named groups support)
            "TwoChairs-objnode\n" +
            // unnamed geometry with generated name (as before named groups support).
            // actually it's partially smoothed, but this fact is ignored.
            "  TwoChairs-geom-0 (material: dot_purple)\n" +
            // named group as Geometry
            "  Chair 2 (material: dot_purple)\n" +
            // named group as Geometry
            "  Pillow 2 (material: dot_red)\n" +
            // named group as node with two different Geometry instances,
            // because two materials are used (as before named groups support)
            "  Podium\n" +
            "    TwoChairs-geom-3 (material: dot_red)\n" +
            "    TwoChairs-geom-4 (material: dot_blue)\n" +
            // named group as Geometry
            "  Pillow 1 (material: dot_green)";
        assertEquals(expectedText, sceneAsString.trim());
    }
    
    private static String toDiffFriendlyString(String indent, Spatial spatial) {
        if (spatial instanceof Geometry) {
            return indent + spatial.getName() + " (material: "+((Geometry) spatial).getMaterial().getName()+")\n";
        }
        if (spatial instanceof Node) {
            StringBuilder s = new StringBuilder();
            s.append(indent).append(spatial.getName()).append("\n");
            Node node = (Node) spatial;
            for (final Spatial child : node.getChildren()) {
                s.append(toDiffFriendlyString(indent + "  ", child));
            }
            return s.toString();
        }
        return indent + spatial + "\n";
    }
    
    public static class PngLoaderStub implements AssetLoader {
        @Override
        public Object load(final AssetInfo assetInfo) {
            return new Image();
        }
    }
}