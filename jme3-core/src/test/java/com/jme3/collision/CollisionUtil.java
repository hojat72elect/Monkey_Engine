
package com.jme3.collision;

import com.jme3.bounding.BoundingVolume;

/**
 * Utilities for testing collision.
 * 
 * @author Kirill Vainer
 */
final class CollisionUtil {

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private CollisionUtil() {
    }

    private static void checkCollisionBase(Collidable a, Collidable b, int expected) {
        // Test bounding volume methods
        if (a instanceof BoundingVolume && b instanceof BoundingVolume) {
            BoundingVolume bv1 = (BoundingVolume) a;
            BoundingVolume bv2 = (BoundingVolume) b;
            assert bv1.intersects(bv2) == (expected != 0);
        }

        // Test standard collideWith method
        CollisionResults results = new CollisionResults();
        int numCollisions = a.collideWith(b, results);
        assert results.size() == numCollisions;
        assert numCollisions == expected;

        // Force the results to be sorted here.
        results.getClosestCollision();

        if (results.size() > 0) {
            assert results.getCollision(0) == results.getClosestCollision();
        }
        if (results.size() == 1) {
            assert results.getClosestCollision() == results.getFarthestCollision();
        }
    }
    
    /**
     * Tests various collisions between the two collidables and 
     * the transitive property.
     * 
     * @param a First collidable
     * @param b Second collidable
     * @param expected the expected number of results
     */
    public static void checkCollision(Collidable a, Collidable b, int expected) {
        checkCollisionBase(a, b, expected);
        checkCollisionBase(b, a, expected);
    }
}
