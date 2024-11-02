
package com.jme3.bullet.collision;

/**
 * Interface to receive notifications whenever an object in a particular
 * collision group is about to collide.
 * <p>
 * This interface is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public interface PhysicsCollisionGroupListener {

    /**
     * Invoked when two physics objects of the registered group are about to
     * collide. <i>invoked on the physics thread</i>.<br>
     * This is only invoked when the collision will happen based on the
     * collisionGroup and collideWithGroups settings in the
     * PhysicsCollisionObject. That is the case when <b>one</b> of the parties
     * has the collisionGroup of the other in its collideWithGroups set.
     *
     * @param nodeA collision object #1
     * @param nodeB collision object #2
     * @return true if the collision should happen, false otherwise
     */
    public boolean collide(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB);

}
