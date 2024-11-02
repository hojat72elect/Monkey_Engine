
package com.jme3.bullet.animation;

/**
 * Enumerate submodes for a link in kinematic mode.
 * <p>
 * This class is shared between JBullet and Native Bullet.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public enum KinematicSubmode {
    /**
     * driven by animation (if any)
     */
    Animated,
    /**
     * frozen in the transform it had when blending started
     */
    Frozen;
}
