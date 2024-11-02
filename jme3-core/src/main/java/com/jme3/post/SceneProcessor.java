
package com.jme3.post;

import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

/**
 * Scene processors are used to compute/render things before and after the classic render of the scene.
 * They have to be added to a viewport and are rendered in the order they've been added
 *
 * @author Kirill Vainer
 */
public interface SceneProcessor {

    /**
     * Called in the render thread to initialize the scene processor.
     *
     * @param rm The render manager to which the SP was added to
     * @param vp The viewport to which the SP is assigned
     */
    public void initialize(RenderManager rm, ViewPort vp);

    /**
     * Called when the resolution of the viewport has been changed.
     *
     * @param vp the affected ViewPort
     * @param w the new width (in pixels)
     * @param h the new height (in pixels)
     */
    public void reshape(ViewPort vp, int w, int h);

    /**
     * Called when the scale of the viewport has been changed.
     *
     * @param vp the affected ViewPort
     * @param x the new horizontal scale 
     * @param y the new vertical scale 
     */
    public default void rescale(ViewPort vp, float x, float y) {

    }

    /**
     * @return True if initialize() has been called on this SceneProcessor,
     * false if otherwise.
     */
    public boolean isInitialized();

    /**
     * Called before a frame
     *
     * @param tpf Time per frame
     */
    public void preFrame(float tpf);

    /**
     * Called after the scene graph has been queued, but before it is flushed.
     *
     * @param rq The render queue
     */
    public void postQueue(RenderQueue rq);

    /**
     * Called after a frame has been rendered and the queue flushed.
     *
     * @param out The FB to which the scene was rendered.
     */
    public void postFrame(FrameBuffer out);

    /**
     * Called when the SP is removed from the RM.
     */
    public void cleanup();

    /**
     * Sets a profiler Instance for this processor.
     *
     * @param profiler the profiler instance.
     */
    public void setProfiler(AppProfiler profiler);

}
