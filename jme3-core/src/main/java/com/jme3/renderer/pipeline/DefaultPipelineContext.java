
package com.jme3.renderer.pipeline;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default implementation of AbstractPipelineContext that
 * does nothing extra.
 * 
 * @author codex
 */
public class DefaultPipelineContext implements PipelineContext {

    // decided to use an atomic boolean, since it is less hassle
    private final AtomicBoolean rendered = new AtomicBoolean(false);
    
    @Override
    public boolean startViewPortRender(RenderManager rm, ViewPort vp) {
        return rendered.getAndSet(true);
    }
    
    @Override
    public void endViewPortRender(RenderManager rm, ViewPort vp) {}
    
    @Override
    public void endContextRenderFrame(RenderManager rm) {
        rendered.set(false);
    }
    
}
