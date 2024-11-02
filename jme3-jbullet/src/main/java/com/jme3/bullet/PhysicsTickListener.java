
package com.jme3.bullet;

/**
 * Callback interface from the physics thread, used to clear/apply forces.
 * <p>
 * This interface is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public interface PhysicsTickListener {

    /**
     * Callback from Bullet, invoked just before the physics is stepped. A good
     * time to clear/apply forces.
     *
     * @param space the space that is about to be stepped (not null)
     * @param tpf the time per physics step (in seconds, &ge;0)
     */
    public void prePhysicsTick(PhysicsSpace space, float tpf);

    /**
     * Callback from Bullet, invoked just after the physics has been stepped.
     * Use it to check for forces etc.
     *
     * @param space the space that was just stepped (not null)
     * @param tpf the time per physics step (in seconds, &ge;0)
     */
    public void physicsTick(PhysicsSpace space, float tpf);

}
