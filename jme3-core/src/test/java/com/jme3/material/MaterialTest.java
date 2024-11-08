
package com.jme3.material;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import com.jme3.system.NullRenderer;
import com.jme3.system.TestUtil;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;

import java.util.Arrays;
import java.util.EnumSet;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MaterialTest {

    private Material material;
    private final Geometry geometry = new Geometry("Geometry", new Box(1, 1, 1));
    private final EnumSet<Caps> myCaps = EnumSet.noneOf(Caps.class);
    private final RenderManager renderManager = new RenderManager(new NullRenderer() {
        @Override
        public EnumSet<Caps> getCaps() {
            return MaterialTest.this.myCaps;
        }
    });

    @Test(expected = IllegalArgumentException.class)
    public void testSelectNonExistentTechnique() {
        material("Common/MatDefs/Gui/Gui.j3md");
        material.selectTechnique("Doesn't Exist", renderManager);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSelectDefaultTechnique_NoCaps() {
        material("Common/MatDefs/Gui/Gui.j3md");
        material.selectTechnique("Default", renderManager);
    }

    @Test
    public void testSelectDefaultTechnique_GLSL100Cap() {
        supportGlsl(100);
        material("Common/MatDefs/Gui/Gui.j3md");

        material.selectTechnique("Default", renderManager);

        checkRequiredCaps(Caps.GLSL100);
    }

    @Test
    public void testSelectDefaultTechnique_GLSL150Cap() {
        supportGlsl(150);
        material("Common/MatDefs/Gui/Gui.j3md");

        material.selectTechnique("Default", renderManager);

        checkRequiredCaps(Caps.GLSL150);
    }

    @Test
    public void testSelectDefaultTechnique_GLSL120Cap_MultipleLangs() {
        supportGlsl(120);
        material("Common/MatDefs/Misc/Particle.j3md");

        material.selectTechnique("Default", renderManager);

        checkRequiredCaps(Caps.GLSL120);
    }

    @Test
    public void testSelectDefaultTechnique_GLSL100Cap_MultipleLangs() {
        supportGlsl(100);
        material("Common/MatDefs/Misc/Particle.j3md");

        material.selectTechnique("Default", renderManager);

        checkRequiredCaps(Caps.GLSL100);
    }

    @Test
    public void testSelectNamedTechnique_GLSL150Cap() {
        supportGlsl(150);
        material("Common/MatDefs/Light/Lighting.j3md");

        material.selectTechnique("PostShadow", renderManager);

        checkRequiredCaps(Caps.GLSL150);
    }

    @Test
    public void testForcedColorSpace(){
       
        Image img=new Image(Format.RGBA8,2,2,BufferUtils.createByteBuffer(16),null,ColorSpace.sRGB);
        Image img2=new Image(Format.RGBA8,2,2,BufferUtils.createByteBuffer(16),null,ColorSpace.sRGB);
        Texture2D tx=new Texture2D(img);
        Texture2D tx2=new Texture2D(img2);

        assertTrue(tx2.getImage().getColorSpace()==ColorSpace.sRGB);
        assertTrue(tx2.getImage().getColorSpace()==ColorSpace.sRGB);

        AssetManager assetManager = TestUtil.createAssetManager();
        MaterialDef def=new MaterialDef(assetManager,"test");
        def.addMaterialParamTexture(VarType.Texture2D, "ColorMap",ColorSpace.Linear, null);
        Material mat=new Material(def);
        
        mat.setTexture("ColorMap",tx);          
        assertTrue(tx.getImage().getColorSpace()==ColorSpace.Linear);
        
        mat.setTexture("ColorMap",tx2);  
        assertTrue(tx2.getImage().getColorSpace()==ColorSpace.Linear);       
    
    }

    @Test
    public void testSelectNamedTechnique_GLSL100Cap() {
        supportGlsl(100);
        material("Common/MatDefs/Light/Lighting.j3md");

        material.selectTechnique("PostShadow", renderManager);

        checkRequiredCaps(Caps.GLSL100);
    }

    private void checkRequiredCaps(Caps... caps) {
        EnumSet<Caps> expected = EnumSet.noneOf(Caps.class);
        expected.addAll(Arrays.asList(caps));

        Technique tech = material.getActiveTechnique();

        assertEquals(expected, tech.getDef().getRequiredCaps());
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
