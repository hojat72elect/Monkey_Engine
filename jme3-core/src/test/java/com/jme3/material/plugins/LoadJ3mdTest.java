
package com.jme3.material.plugins;

import com.jme3.asset.AssetManager;
import com.jme3.material.*;
import com.jme3.renderer.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.shader.Shader;
import com.jme3.system.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoadJ3mdTest {

    private Material material;
    private final Geometry geometry = new Geometry("Geometry", new Box(1, 1, 1));
    private final EnumSet<Caps> myCaps = EnumSet.noneOf(Caps.class);
    private final RenderManager renderManager = new RenderManager(new NullRenderer() {
        @Override
        public EnumSet<Caps> getCaps() {
            return LoadJ3mdTest.this.myCaps;
        }
    });

    @Test(expected = IllegalArgumentException.class)
    public void testBadBooleans1() {
        supportGlsl(100);
        material("bad-booleans1.j3md"); // DepthTest yes
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadBooleans2() {
        supportGlsl(100);
        material("bad-booleans2.j3md"); // DepthWrite on
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadBooleans3() {
        supportGlsl(100);
        material("bad-booleans3.j3md"); // Wireframe true
    }

    @Test
    public void testShaderNodesMaterialDefLoading() {
        supportGlsl(100);
        material("testMatDef.j3md");
        material.selectTechnique("Default", renderManager);

        assertEquals(material.getActiveTechnique().getDef().getShaderNodes().size(), 2);
        Shader s = material.getActiveTechnique().getDef().getShader(TestUtil.createAssetManager(), myCaps,  material.getActiveTechnique().getDynamicDefines());
        assertEquals(s.getSources().size(), 2);
    }

    private void supportGlsl(int version) {
        switch (version) {
            case 150:
                myCaps.add(Caps.GLSL150);
            case 140:
                myCaps.add(Caps.GLSL140);
            case 130:
                myCaps.add(Caps.GLSL130);
            case 120:
                myCaps.add(Caps.GLSL120);
            case 110:
                myCaps.add(Caps.GLSL110);
            case 100:
                myCaps.add(Caps.GLSL100);
                break;
        }
    }

    private void material(String path) {
        AssetManager assetManager = TestUtil.createAssetManager();
        material = new Material(assetManager, path);
        geometry.setMaterial(material);
    }

}
