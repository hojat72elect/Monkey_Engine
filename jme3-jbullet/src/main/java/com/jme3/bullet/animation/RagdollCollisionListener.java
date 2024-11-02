
package com.jme3.bullet.animation;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionObject;

/**
 * Interface to receive notifications whenever a linked rigid body in a
 * DynamicAnimControl collides with another physics object.
 * <p>
 * This class is shared between JBullet and Native Bullet.
 *
 * @author Nehon
 */
public interface RagdollCollisionListener {

    /**
     * Invoked when a collision involving a linked rigid body occurs.
     *
     * @param physicsLink the physics link that collided (not null)
     * @param object the collision object that collided with the bone (not null)
     * @param event other event details (not null)
     */
    void collide(PhysicsLink physicsLink, PhysicsCollisionObject object,
            PhysicsCollisionEvent event);
}
