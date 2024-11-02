
 
package com.jme3.profile;


/**
 *  Indicates an application-level step within the profiled
 *  frame.
 *
 *  @author    Paul Speed
 */
public enum AppStep {
    BeginFrame,
    QueuedTasks,
    ProcessInput,
    ProcessAudio,
    StateManagerUpdate,
    SpatialUpdate,
    StateManagerRender,
    RenderFrame,
    RenderPreviewViewPorts,
    RenderMainViewPorts,
    RenderPostViewPorts,
    EndFrame
}    


