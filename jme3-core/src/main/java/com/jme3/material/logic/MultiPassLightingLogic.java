
package com.jme3.material.logic;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.RenderState;
import com.jme3.material.TechniqueDef;
import com.jme3.material.Material.BindUnits;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.VarType;
import com.jme3.util.TempVars;

public final class MultiPassLightingLogic extends DefaultTechniqueDefLogic {

    private static final RenderState ADDITIVE_LIGHT = new RenderState();
    private static final Quaternion NULL_DIR_LIGHT = new Quaternion(0, -1, 0, -1);
    
    private final ColorRGBA ambientLightColor = new ColorRGBA(0, 0, 0, 1);
    
    static {
        ADDITIVE_LIGHT.setBlendMode(RenderState.BlendMode.AlphaAdditive);
        ADDITIVE_LIGHT.setDepthWrite(false);
    }
    
    public MultiPassLightingLogic(TechniqueDef techniqueDef) {
        super(techniqueDef);
    }

    @Override
    public void render(RenderManager renderManager, Shader shader, Geometry geometry, LightList lights, BindUnits lastBindUnits) {
        Renderer r = renderManager.getRenderer();
        Uniform lightDir = shader.getUniform("g_LightDirection");
        Uniform lightColor = shader.getUniform("g_LightColor");
        Uniform lightPos = shader.getUniform("g_LightPosition");
        Uniform ambientColor = shader.getUniform("g_AmbientLightColor");
        boolean isFirstLight = true;
        boolean isSecondLight = false;
        
        getAmbientColor(lights, false, ambientLightColor);

        for (int i = 0; i < lights.size(); i++) {
            Light l = lights.get(i);
            if (l.getType() == Light.Type.Ambient  || l.getType() == Light.Type.Probe) {
                continue;
            }

            if (isFirstLight) {
                // set ambient color for first light only
                ambientColor.setValue(VarType.Vector4, ambientLightColor);
                isFirstLight = false;
                isSecondLight = true;
            } else if (isSecondLight) {
                ambientColor.setValue(VarType.Vector4, ColorRGBA.Black);
                // apply additive blending for 2nd and future lights
                r.applyRenderState(ADDITIVE_LIGHT);
                isSecondLight = false;
            }

            TempVars vars = TempVars.get();
            Quaternion tmpLightDirection = vars.quat1;
            Quaternion tmpLightPosition = vars.quat2;
            ColorRGBA tmpLightColor = vars.color;
            Vector4f tmpVec = vars.vect4f1;

            ColorRGBA color = l.getColor();
            tmpLightColor.set(color);
            tmpLightColor.a = l.getType().getId();
            lightColor.setValue(VarType.Vector4, tmpLightColor);

            switch (l.getType()) {
                case Directional:
                    DirectionalLight dl = (DirectionalLight) l;
                    Vector3f dir = dl.getDirection();
                    //FIXME : there is an inconsistency here due to backward
                    //compatibility of the lighting shader.
                    //The directional light direction is passed in the
                    //LightPosition uniform. The lighting shader needs to be
                    //reworked though in order to fix this.
                    tmpLightPosition.set(dir.getX(), dir.getY(), dir.getZ(), -1);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);
                    tmpLightDirection.set(0, 0, 0, 0);
                    lightDir.setValue(VarType.Vector4, tmpLightDirection);
                    break;
                case Point:
                    PointLight pl = (PointLight) l;
                    Vector3f pos = pl.getPosition();
                    float invRadius = pl.getInvRadius();

                    tmpLightPosition.set(pos.getX(), pos.getY(), pos.getZ(), invRadius);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);
                    tmpLightDirection.set(0, 0, 0, 0);
                    lightDir.setValue(VarType.Vector4, tmpLightDirection);
                    break;
                case Spot:
                    SpotLight sl = (SpotLight) l;
                    Vector3f pos2 = sl.getPosition();
                    Vector3f dir2 = sl.getDirection();
                    float invRange = sl.getInvSpotRange();
                    float spotAngleCos = sl.getPackedAngleCos();

                    tmpLightPosition.set(pos2.getX(), pos2.getY(), pos2.getZ(), invRange);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);

                    //We transform the spot direction in view space here to save 5 varying later in the lighting shader
                    //one vec4 less and a vec4 that becomes a vec3
                    //the downside is that spotAngleCos decoding happens now in the frag shader.
                    tmpVec.set(dir2.getX(), dir2.getY(), dir2.getZ(), 0);
                    renderManager.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
                    tmpLightDirection.set(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), spotAngleCos);

                    lightDir.setValue(VarType.Vector4, tmpLightDirection);

                    break;
                default:
                    throw new UnsupportedOperationException("Unknown type of light: " + l.getType());
            }
            vars.release();
            r.setShader(shader);
            renderMeshFromGeometry(r, geometry);
        }

        if (isFirstLight) {
            // Either there are no lights at all, or only ambient lights.
            // Render a dummy "normal light" so we can see the ambient color.
            ambientColor.setValue(VarType.Vector4, getAmbientColor(lights, false, ambientLightColor));
            lightColor.setValue(VarType.Vector4, ColorRGBA.BlackNoAlpha);
            lightPos.setValue(VarType.Vector4, NULL_DIR_LIGHT);
            r.setShader(shader);
            renderMeshFromGeometry(r, geometry);
        }
    }
}
