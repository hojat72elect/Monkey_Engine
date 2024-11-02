
package com.jme3.material.plugin;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.plugin.export.material.J3MExporter;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.math.ColorRGBA;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Texture;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class TestMaterialWrite {

    private AssetManager assetManager;

    @Before
    public void init() {
        assetManager = JmeSystem.newAssetManager(
                TestMaterialWrite.class.getResource("/com/jme3/asset/Desktop.cfg"));


    }


    @Test
    public void testWriteMat() throws Exception {

        Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");

        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.DarkGray);
        mat.setFloat("AlphaDiscardThreshold", 0.5f);

        mat.setFloat("Shininess", 2.5f);

        Texture tex = assetManager.loadTexture(new TextureKey("Common/Textures/MissingTexture.png", true));
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        tex.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
        tex.setWrap(Texture.WrapAxis.T, Texture.WrapMode.MirroredRepeat);

        mat.setTexture("DiffuseMap", tex);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setDepthTest(false);
        mat.getAdditionalRenderState().setLineWidth(5);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        J3MExporter exporter = new J3MExporter();
        try {
            exporter.save(mat, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println(stream.toString());

        J3MLoader loader = new J3MLoader();
        AssetInfo info = new AssetInfo(assetManager, new AssetKey("test")) {
            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(stream.toByteArray());
            }
        };
        Material mat2 = (Material)loader.load(info);

        assertTrue(mat.contentEquals(mat2));
    }

}
