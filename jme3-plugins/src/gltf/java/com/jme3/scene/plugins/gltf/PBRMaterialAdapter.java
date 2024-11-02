
package com.jme3.scene.plugins.gltf;

import com.jme3.material.*;

/**
 * Adapts GLTF PBR materials to JME PBR materials.
 * 
 * @author Nehon
 */
public abstract class PBRMaterialAdapter extends MaterialAdapter {
    
    /**
     * The default alpha discard threshold for "MASK" blend mode.
     */
    public static final float MASK_ALPHA_DISCARD = 0.5f;
    
    public PBRMaterialAdapter() {
        addParamMapping("normalTexture", "NormalMap");
        addParamMapping("normalScale", "NormalScale");
        addParamMapping("occlusionTexture", "LightMap");
        addParamMapping("occlusionStrength", "AoStrength");
        addParamMapping("emissiveTexture", "EmissiveMap");
        addParamMapping("emissiveFactor", "Emissive");
        addParamMapping("alphaMode", "alpha");
        addParamMapping("alphaCutoff", "AlphaDiscardThreshold");
        addParamMapping("doubleSided", "doubleSided");
    }

    @Override
    protected String getMaterialDefPath() {
        return "Common/MatDefs/Light/PBRLighting.j3md";
    }
    
    @Override
    protected void initDefaultMatParams(Material material) {
        material.setFloat("EmissiveIntensity", 1);
    }

    @Override
    protected MatParam adaptMatParam(MatParam param) {
        if (param.getName().equals("alpha")) {
            String alphaMode = (String) param.getValue();
            switch (alphaMode) {
                case "MASK":
                    // "MASK" -> BlendMode.Off
                    getMaterial().setFloat("AlphaDiscardThreshold", MASK_ALPHA_DISCARD);
                    break;
                case "BLEND":
                    getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                    break;
            }

            // Alpha mode is handled here, so return null
            return null;
        } else if (param.getName().equals("doubleSided")) {
            boolean doubleSided = (boolean) param.getValue();
            if (doubleSided) {
                //Note that this is not completely right as normals on the back side will be in the wrong direction.
                getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            }
            // FaceCulling is a RenderState not a Material Parameter, so return null
            return null;
        } else if (param.getName().equals("NormalMap")) {
            //Set the normal map type to OpenGl
            getMaterial().setFloat("NormalType", 1.0f);
        } else if (param.getName().equals("LightMap")) {
            //Gltf only supports AO maps (gray scales and only the r channel must be read)
            getMaterial().setBoolean("LightMapAsAOMap", true);
        } else if (param.getName().equals("alphaCutoff")) {
            getMaterial().setFloat("AlphaDiscardThreshold", (float)param.getValue());
        }

        return param;
    }
}
