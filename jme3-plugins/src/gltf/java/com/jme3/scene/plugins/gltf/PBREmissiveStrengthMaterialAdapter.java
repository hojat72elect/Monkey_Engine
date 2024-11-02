
package com.jme3.scene.plugins.gltf;

/**
 * Adapter for converting GLTF emissive strength to JME emissive intensity.
 * 
 * @author codex
 */
public class PBREmissiveStrengthMaterialAdapter extends PBRMaterialAdapter {
    
    public PBREmissiveStrengthMaterialAdapter() {
        super();
        addParamMapping("emissiveStrength", "EmissiveIntensity");
    }
    
}
