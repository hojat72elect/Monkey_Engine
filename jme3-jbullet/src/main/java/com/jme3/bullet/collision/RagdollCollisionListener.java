
package com.jme3.bullet.collision;

import com.jme3.animation.Bone;

/**
 * Interface to receive notifications whenever a KinematicRagdollControl
 * collides with another physics object.
 * <p>
 * This interface is shared between JBullet and Native Bullet.
 *
 * @author Nehon
 */
public interface RagdollCollisionListener {
    
    /**
     * Invoked when a collision involving a KinematicRagdollControl occurs.
     *
     * @param bone the ragdoll bone that collided (not null)
     * @param object the collision object that collided with the bone (not null)
     * @param event other event details (not null)
     */    
    public void collide(Bone bone, PhysicsCollisionObject object, PhysicsCollisionEvent event);
    
}
