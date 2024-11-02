
package com.jme3.scene.control;

import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * An interface for scene-graph controls. 
 * <p>
 * <code>Control</code>s are used to specify certain update and render logic
 * for a {@link Spatial}. 
 *
 * @author Kirill Vainer
 */
public interface Control extends Savable {

    /**
     * Creates a clone of the Control, the given Spatial is the cloned version
     * of the spatial to which this control is attached to.
     *
     * @param spatial the Spatial to be controlled by the clone
     * @return A clone of this control for the spatial
     * @deprecated Use
     * {@link com.jme3.util.clone.JmeCloneable#cloneFields(com.jme3.util.clone.Cloner, java.lang.Object)}
     */
    @Deprecated
    public Control cloneForSpatial(Spatial spatial);

    /**
     * @param spatial the spatial to be controlled. This should not be called
     * from user code.
     */
    public void setSpatial(Spatial spatial);

    /**
     * Updates the control. This should not be called from user code.
     * @param tpf Time per frame.
     */
    public void update(float tpf);

    /**
     * Should be called prior to queuing the spatial by the RenderManager. This
     * should not be called from user code.
     *
     * @param rm the caller (not null)
     * @param vp the relevant ViewPort (not null)
     */
    public void render(RenderManager rm, ViewPort vp);
}
