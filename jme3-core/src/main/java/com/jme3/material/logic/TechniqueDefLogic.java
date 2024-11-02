
package com.jme3.material.logic;

import com.jme3.asset.AssetManager;
import com.jme3.light.LightList;
import com.jme3.material.Material.BindUnits;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.shader.DefineList;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.texture.Texture;
import java.util.EnumSet;

/**
 * <code>TechniqueDefLogic</code> is used to customize how 
 * a material should be rendered.
 * 
 * Typically used to implement {@link com.jme3.material.TechniqueDef.LightMode lighting modes}.
 * Implementations can register 
 * {@link com.jme3.material.TechniqueDef#addShaderUnmappedDefine(java.lang.String, com.jme3.shader.VarType) unmapped defines} 
 * in their constructor and then later set them based on the geometry 
 * or light environment being rendered.
 * 
 * @author Kirill Vainer
 */
public interface TechniqueDefLogic {
    
    /**
     * Determine the shader to use for the given geometry / material combination.
     * 
     * @param assetManager The asset manager to use for loading shader source code,
     * shader nodes, and lookup textures.
     * @param renderManager The render manager for which rendering is to be performed.
     * @param rendererCaps Renderer capabilities. The returned shader must
     * support these capabilities.
     * @param lights The lights with which the geometry shall be rendered. This
     * list must not include culled lights.
     * @param defines The define list used by the technique, any 
     * {@link com.jme3.material.TechniqueDef#addShaderUnmappedDefine(java.lang.String, com.jme3.shader.VarType) unmapped defines}
     * should be set here to change shader behavior.
     * 
     * @return The shader to use for rendering.
     */
    public Shader makeCurrent(AssetManager assetManager, RenderManager renderManager, 
            EnumSet<Caps> rendererCaps, LightList lights, DefineList defines);
    
    /**
     * Requests that the <code>TechniqueDefLogic</code> renders the given geometry.
     * 
     * Fixed material functionality such as {@link com.jme3.material.RenderState}, 
     * {@link com.jme3.material.MatParam material parameters}, and 
     * {@link com.jme3.shader.UniformBinding uniform bindings}
     * have already been applied by the material, however, 
     * {@link com.jme3.material.RenderState}, {@link Uniform uniforms}, {@link Texture textures},
     * can still be overridden.
     * 
     * @param renderManager The render manager to perform the rendering against.
     * @param shader The shader that was selected by this logic in 
     * {@link #makeCurrent(com.jme3.asset.AssetManager, com.jme3.renderer.RenderManager, java.util.EnumSet, com.jme3.light.LightList, com.jme3.shader.DefineList)}.
     * @param geometry The geometry to render
     * @param lights Lights which influence the geometry.
     * @param lastTexUnit the index of the most recently used texture unit
     */
    public void render(RenderManager renderManager, Shader shader, Geometry geometry, LightList lights, BindUnits lastBindUnits);
}
