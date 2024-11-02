
 
package com.jme3.profile;


/**
 *  Indicates a ViewPort-level step within the profiled
 *  frame.
 *
 *  @author    Paul Speed
 */
public enum VpStep {
    BeginRender,
    RenderScene,
    PreFrame,
    PostQueue,
    FlushQueue,
    PostFrame,
    ProcEndRender,
    RenderBucket,
    EndRender
}

