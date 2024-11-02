
package com.jme3.effect.shapes;

import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.JmeCloneable;

/**
 * This interface declares methods used by all shapes that represent particle emitters.
 * @author Kirill
 */
public interface EmitterShape extends Savable, Cloneable, JmeCloneable {

    /**
     * This method fills in the initial position of the particle.
     * @param store
     *        store variable for initial position
     */
    public void getRandomPoint(Vector3f store);

    /**
     * This method fills in the initial position of the particle and its normal vector.
     * @param store
     *        store variable for initial position
     * @param normal
     *        store variable for initial normal
     */
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal);

    /**
     * This method creates a deep clone of the current instance of the emitter shape.
     * @return deep clone of the current instance of the emitter shape
     */
    public EmitterShape deepClone();
}
