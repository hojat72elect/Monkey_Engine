
package com.jme3.shader;

import com.jme3.asset.AssetManager;
import com.jme3.shader.Shader.ShaderType;


/**
 * This shader Generator can generate Vertex and Fragment shaders from
 * ShaderNodes for GLESSL 3.0
 * Nowadays it's just a subclass of Glsl150ShaderGenerator overriding the version
 * string because GLSL 1.5 is mostly compatible with GLESSL 3.0
 *
 * @author Nehon
 * @author Joliver82
 */
public class Glsl300ShaderGenerator extends Glsl150ShaderGenerator {

    /**
     * Creates a Glsl300ShaderGenerator
     *
     * @param assetManager the assetmanager
     */
    public Glsl300ShaderGenerator(AssetManager assetManager) {
        super(assetManager);
    }

    @Override
    protected String getLanguageAndVersion(ShaderType type) {
        return "GLSL300";
    }

}
