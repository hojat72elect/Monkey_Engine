
package com.jme3.renderer.pipeline;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Handles objects globally for a single type of RenderPipeline.
 * 
 * @author codex
 */
public interface PipelineContext {
    
    /**
     * Called when a ViewPort rendering session starts that this context
     * is participating in.
     * 
     * @param rm
     * @param vp viewport being rendered
     * @return true if this context has already rendered a viewport this frame
     */
    public boolean startViewPortRender(RenderManager rm, ViewPort vp);
    
    /**
     * Called when viewport rendering session ends that this context
     * is participating in.
     * 
     * @param rm 
     * @param vp viewport being rendered
     */
    public void endViewPortRender(RenderManager rm, ViewPort vp);
    
    /**
     * Called at the end of a render frame this context participated in.
     * 
     * @param rm 
     */
    public void endContextRenderFrame(RenderManager rm);
    
}
