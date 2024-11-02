

package com.jme3.collision;

import com.jme3.math.Vector3f;

public interface MotionAllowedListener {

    /**
     * Check if motion allowed. Modify position and velocity vectors
     * appropriately if not allowed..
     * 
     * @param position the position vector (modified)
     * @param velocity the velocity vector (modified)
     */
    public void checkMotionAllowed(Vector3f position, Vector3f velocity);

}
