
package com.jme3.water;

import com.jme3.math.Plane;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

/**
 * Reflection Processor
 * Used to render the reflected scene in an off view port
 */
public class ReflectionProcessor implements SceneProcessor {

    private RenderManager rm;
    private ViewPort vp;
    private Camera reflectionCam;
    private FrameBuffer reflectionBuffer;
    private Plane reflectionClipPlane;

    /**
     * Creates a ReflectionProcessor
     * @param reflectionCam the cam to use for reflection
     * @param reflectionBuffer the FrameBuffer to render to
     * @param reflectionClipPlane the clipping plane
     */
    public ReflectionProcessor(Camera reflectionCam, FrameBuffer reflectionBuffer, Plane reflectionClipPlane) {
        this.reflectionCam = reflectionCam;
        this.reflectionBuffer = reflectionBuffer;
        this.reflectionClipPlane = reflectionClipPlane;
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        this.rm = rm;
        this.vp = vp;
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
    }

    @Override
    public boolean isInitialized() {
        return rm != null;
    }

    @Override
    public void preFrame(float tpf) {
    }

    @Override
    public void postQueue(RenderQueue rq) {
        //we need special treatment for the sky because it must not be clipped
        rm.getRenderer().setFrameBuffer(reflectionBuffer);
        reflectionCam.setProjectionMatrix(null);
        rm.setCamera(reflectionCam, false);
        rm.getRenderer().clearBuffers(true, true, true);
        //Rendering the sky without clipping
        rm.getRenderer().setDepthRange(1, 1);
        vp.getQueue().renderQueue(RenderQueue.Bucket.Sky, rm, reflectionCam, true);
        rm.getRenderer().setDepthRange(0, 1);
        //setting the clip plane to the cam
        reflectionCam.setClipPlane(reflectionClipPlane, Plane.Side.Positive);//,1
        rm.setCamera(reflectionCam, false);

    }

    @Override
    public void postFrame(FrameBuffer out) {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void setProfiler(AppProfiler profiler) {
        // not implemented
    }

    /**
     * Internal use only<br>
     * returns the frame buffer
     * @return the pre-existing buffer
     */
    public FrameBuffer getReflectionBuffer() {
        return reflectionBuffer;
    }

    /**
     * Internal use only<br>
     * sets the frame buffer
     *
     * @param reflectionBuffer the FrameBuffer to use (alias created)
     */
    public void setReflectionBuffer(FrameBuffer reflectionBuffer) {
        this.reflectionBuffer = reflectionBuffer;
    }

    /**
     * returns the reflection cam
     * @return the pre-existing Camera
     */
    public Camera getReflectionCam() {
        return reflectionCam;
    }

    /**
     * sets the reflection cam
     *
     * @param reflectionCam the Camera to use (alias created)
     */
    public void setReflectionCam(Camera reflectionCam) {
        this.reflectionCam = reflectionCam;
    }

    /**
     * returns the reflection clip plane
     * @return the pre-existing instance
     */
    public Plane getReflectionClipPlane() {
        return reflectionClipPlane;
    }

    /**
     * Sets the reflection clip plane
     *
     * @param reflectionClipPlane the Plane to use (alias created)
     */
    public void setReflectionClipPlane(Plane reflectionClipPlane) {
        this.reflectionClipPlane = reflectionClipPlane;
    }
}
