
package com.jme3.scene.plugins.gltf;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;

/**
 * @author Markil 3
 */
public class UnlitMaterialAdapter extends MaterialAdapter {

    public UnlitMaterialAdapter() {
        addParamMapping("baseColorFactor", "Color");
        addParamMapping("baseColorTexture", "ColorMap");
        addParamMapping("emissiveFactor", "GlowColor");
        addParamMapping("emissiveTexture", "GlowMap");
        addParamMapping("alphaMode", "alpha");
        addParamMapping("alphaCutoff", "AlphaDiscardThreshold");
        addParamMapping("doubleSided", "doubleSided");
    }

    @Override
    protected String getMaterialDefPath() {
        return "Common/MatDefs/Misc/Unshaded.j3md";
    }

    @Override
    protected MatParam adaptMatParam(MatParam param) {
        if (param.getName().equals("alpha")) {
            String alphaMode = (String) param.getValue();
            switch (alphaMode) {
                case "MASK": // fallthrough
                case "BLEND":
                    getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                    break;
            }
            // Alpha is a RenderState not a Material Parameter, so return null
            return null;
        }
        if (param.getName().equals("doubleSided")) {
            boolean doubleSided = (boolean) param.getValue();
            if (doubleSided) {
                //Note that this is not completely right as normals on the back side will be in the wrong direction.
                getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            }
            // FaceCulling is a RenderState not a Material Parameter, so return null
            return null;
        }
        return param;
    }    
    
    @Override
    protected void initDefaultMatParams(Material material) {}
    
}
