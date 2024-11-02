
package com.jme3.bullet.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.control.Control;

/**
 * An interface for a scene-graph control that links a physics object to a
 * Spatial.
 * <p>
 * This interface is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public interface PhysicsControl extends Control {

    /**
     * If enabled, add this control's physics object to the specified physics
     * space. In not enabled, alter where the object would be added. The object
     * is removed from any other space it's currently in.
     *
     * @param space where to add, or null to simply remove
     */
    public void setPhysicsSpace(PhysicsSpace space);

    /**
     * Access the physics space to which the object is (or would be) added.
     *
     * @return the pre-existing space, or null for none
     */
    public PhysicsSpace getPhysicsSpace();

    /**
     * Enable or disable this control.
     * <p>
     * The physics object is removed from its physics space when the control is
     * disabled. When the control is enabled again, the physics object is moved
     * to the current location of the spatial and then added to the physics
     * space.
     *
     * @param state true&rarr;enable the control, false&rarr;disable it
     */
    public void setEnabled(boolean state);

    /**
     * Test whether this control is enabled.
     *
     * @return true if enabled, otherwise false
     */
    public boolean isEnabled();
}
