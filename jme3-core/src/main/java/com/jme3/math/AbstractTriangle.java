
package com.jme3.math;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;

/**
 * A Collidable with a triangular shape.
 */
public abstract class AbstractTriangle implements Collidable {
    /**
     * Determine the location of the first vertex.
     *
     * @return a location vector
     */
    public abstract Vector3f get1();

    /**
     * Determine the location of the 2nd vertex.
     *
     * @return a location vector
     */
    public abstract Vector3f get2();

    /**
     * Determine the location of the 3rd vertex.
     *
     * @return a location vector
     */
    public abstract Vector3f get3();

    /**
     * Alter all 3 vertex locations.
     *
     * @param v1 the location for the first vertex
     * @param v2 the location for the 2nd vertex
     * @param v3 the location for the 3rd vertex
     */
    public abstract void set(Vector3f v1, Vector3f v2, Vector3f v3);

    /**
     * Generate collision results for this triangle with another Collidable.
     *
     * @param other the other Collidable
     * @param results storage for collision results
     * @return the number of collisions found
     */
    @Override
    public int collideWith(Collidable other, CollisionResults results) {
        return other.collideWith(this, results);
    }

    /**
     * Returns a string representation of the triangle, which is unaffected. For
     * example, a {@link com.jme3.math.Triangle} joining (1,0,0) and (0,1,0)
     * with (0,0,1) is represented by:
     * <pre>
     * Triangle [V1: (1.0, 0.0, 0.0)  V2: (0.0, 1.0, 0.0)  V3: (0.0, 0.0, 1.0)]
     * </pre>
     *
     * @return the string representation (not null, not empty)
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [V1: " + get1() + "  V2: "
                + get2() + "  V3: " + get3() + "]";
    }
}
