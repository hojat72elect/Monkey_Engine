
 
package com.jme3.profile;

import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;


/**
 *  Can be hooked into the application (and render manager)
 *  to receive callbacks about specific frame steps.  It is up
 *  to the specific implementation to decide what to do with
 *  the information.
 *
 *  @author    Paul Speed
 */
public interface AppProfiler {

    /**
     *  Called at the beginning of the specified AppStep.
     * 
     * @param step the application-level step that's about to begin
     */
    public void appStep(AppStep step);
    
    /**
     * Called as a substep of the previous AppStep
     *
     * @param additionalInfo information about the substep
     */
    public void appSubStep(String... additionalInfo);
    
    /**
     *  Called at the beginning of the specified VpStep during
     *  the rendering of the specified ViewPort.  For bucket-specific
     *  steps the Bucket parameter will be non-null.
     *
     * @param step the ViewPort-level step that's about to begin
     * @param vp which ViewPort is being processed
     * @param bucket which Bucket is being processed
     */
    public void vpStep(VpStep step, ViewPort vp, Bucket bucket);

    /**
     * Called at the beginning of the specified SpStep (SceneProcessor step).
     * For more detailed steps it is possible to provide additional information as strings, like the name of the processor.
     *
     * @param step the SceneProcessor step that's about to begin
     * @param additionalInfo information about the SceneProcessor step
     */
    public void spStep(SpStep step, String... additionalInfo);
}


