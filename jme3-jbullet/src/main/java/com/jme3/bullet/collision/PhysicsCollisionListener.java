
package com.jme3.bullet.collision;

/**
 * Interface to receive notifications whenever an object in a particular physics
 * space collides.
 * <p>
 * This interface is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public interface PhysicsCollisionListener {

    /**
     * Invoked when a collision happened in the PhysicsSpace. <i>Invoked on the
     * render thread.</i>
     * <p>
     * Do not retain the event object, as it will be reused after the
     * collision() method returns. Copy any data you need during the collide()
     * method.
     *
     * @param event the event that occurred (not null, reusable)
     */
    public void collision(PhysicsCollisionEvent event);

}
