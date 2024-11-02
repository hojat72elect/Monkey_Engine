
package com.jme3.material.logic;

import com.jme3.asset.AssetManager;
import com.jme3.light.*;
import com.jme3.material.TechniqueDef;
import com.jme3.material.Material.BindUnits;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.instancing.InstancedGeometry;
import com.jme3.shader.DefineList;
import com.jme3.shader.Shader;
import java.util.EnumSet;

public class DefaultTechniqueDefLogic implements TechniqueDefLogic {

    protected final TechniqueDef techniqueDef;

    public DefaultTechniqueDefLogic(TechniqueDef techniqueDef) {
        this.techniqueDef = techniqueDef;
    }

    @Override
    public Shader makeCurrent(AssetManager assetManager, RenderManager renderManager,
            EnumSet<Caps> rendererCaps, LightList lights, DefineList defines) {
        return techniqueDef.getShader(assetManager, rendererCaps, defines);
    }

    public static void renderMeshFromGeometry(Renderer renderer, Geometry geom) {
        Mesh mesh = geom.getMesh();
        int lodLevel = geom.getLodLevel();
        if (geom instanceof InstancedGeometry) {
            InstancedGeometry instGeom = (InstancedGeometry) geom;
            int numVisibleInstances = instGeom.getNumVisibleInstances();
            if (numVisibleInstances > 0) {
                renderer.renderMesh(mesh, lodLevel, numVisibleInstances, instGeom.getAllInstanceData());
            }
        } else {
            renderer.renderMesh(mesh, lodLevel, 1, null);
        }
    }

    protected static ColorRGBA getAmbientColor(LightList lightList, boolean removeLights, ColorRGBA ambientLightColor) {
        ambientLightColor.set(0, 0, 0, 1);
        for (int j = 0; j < lightList.size(); j++) {
            Light l = lightList.get(j);
            if (l instanceof AmbientLight) {
                ambientLightColor.addLocal(l.getColor());
                if (removeLights) {
                    lightList.remove(l);
                }
            }
        }
        ambientLightColor.a = 1.0f;
        return ambientLightColor;
    }



    @Override
    public void render(RenderManager renderManager, Shader shader, Geometry geometry, LightList lights, BindUnits lastBindUnits) {
        Renderer renderer = renderManager.getRenderer();
        renderer.setShader(shader);
        renderMeshFromGeometry(renderer, geometry);
    }
}
