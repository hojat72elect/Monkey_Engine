
package com.jme3.material.plugin;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.material.plugin.export.materialdef.J3mdExporter;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.shader.*;
import com.jme3.system.JmeSystem;
import org.junit.*;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestMaterialDefWrite {

    private AssetManager assetManager;

    @Before
    public void init() {
        assetManager = JmeSystem.newAssetManager(
                TestMaterialDefWrite.class.getResource("/com/jme3/asset/Desktop.cfg"));


    }


    @Test
    public void testWriteMat() throws Exception {

        Material mat = new Material(assetManager,"example.j3md");

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        J3mdExporter exporter = new J3mdExporter();
        try {
            exporter.save(mat.getMaterialDef(), stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

     //   System.err.println(stream.toString());

        J3MLoader loader = new J3MLoader();
        AssetInfo info = new AssetInfo(assetManager, new AssetKey("test")) {
            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(stream.toByteArray());
            }
        };
        MaterialDef matDef = (MaterialDef)loader.load(info);
        MaterialDef ref = mat.getMaterialDef();

        for (MatParam refParam : ref.getMaterialParams()) {
            MatParam matParam = matDef.getMaterialParam(refParam.getName());
            assertTrue(refParam != null);
            assertEquals(refParam,matParam);
        }

        for (String key : ref.getTechniqueDefsNames()) {
            List<TechniqueDef> refDefs = ref.getTechniqueDefs(key);
            List<TechniqueDef> defs = matDef.getTechniqueDefs(key);

            assertNotNull(defs);
            assertTrue(refDefs.size() == defs.size());
            for (int i = 0; i < refDefs.size(); i++) {
                assertEqualTechniqueDefs(refDefs.get(i), defs.get(i));
            }
        }
    }

    private void assertEqualTechniqueDefs(TechniqueDef def1, TechniqueDef def2){
        assertEquals(def1.getName(), def2.getName());
        assertEquals(def1.getLightMode(), def2.getLightMode());
        assertEquals(def1.getShadowMode(), def2.getShadowMode());
        assertEquals(def1.getShaderProgramNames().size(), def2.getShaderProgramNames().size());

        //World params
        assertEquals(def1.getWorldBindings().size(), def2.getWorldBindings().size());
        for (UniformBinding uniformBinding : def1.getWorldBindings()) {
            assertTrue(def2.getWorldBindings().contains(uniformBinding));
        }

        //defines
        assertEquals(def1.getDefineNames().length, def2.getDefineNames().length);

        //renderState
        assertEquals(def1.getRenderState(), def2.getRenderState());

        //forced renderState
        assertEquals(def1.getForcedRenderState(), def2.getForcedRenderState());

        assertEquals(def1.isUsingShaderNodes(), def2.isUsingShaderNodes());

        if(def1.isUsingShaderNodes()){
            for (int i = 0; i < def1.getShaderNodes().size(); i++) {
                ShaderNode sh1 = def1.getShaderNodes().get(i);
                ShaderNode sh2 = def2.getShaderNodes().get(i);
                assertEquals(sh1.getName(), sh2.getName());
                assertEquals(sh1.getCondition(), sh2.getCondition());
                assertEquals(sh1.getDefinition().getName(), sh2.getDefinition().getName());
                for (int i1 = 0; i1 < sh1.getInputMapping().size(); i1++) {
                    VariableMapping im1 = sh1.getInputMapping().get(i);
                    VariableMapping im2 = sh2.getInputMapping().get(i);
                    assertEqualsVariableMapping(im1, im2);
                }
            }
        }

        //no render
        assertEquals(def1.isNoRender(), def2.isNoRender());
    }

    private void assertEqualsVariableMapping(VariableMapping im1, VariableMapping im2) {
        assertEquals(im1.getCondition(), im2.getCondition());
        assertEquals(im1.getLeftSwizzling(), im2.getLeftSwizzling());
        assertEquals(im1.getRightSwizzling(), im2.getRightSwizzling());
        assertEqualsVariables(im1.getRightVariable(), im2.getRightVariable());
        assertEqualsVariables(im1.getLeftVariable(), im2.getLeftVariable());
    }

    private void assertEqualsVariables(ShaderNodeVariable v1, ShaderNodeVariable v2) {
        assertEquals(v1.getName(), v2.getName());
        assertEquals(v1.getNameSpace(), v2.getNameSpace());
        assertEquals(v1.getMultiplicity(), v2.getMultiplicity());
        assertEquals(v1.getType(), v2.getType());
        assertEquals(v1.getCondition(), v2.getCondition());
    }

}
