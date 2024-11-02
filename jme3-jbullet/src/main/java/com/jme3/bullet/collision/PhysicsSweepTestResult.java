
package com.jme3.bullet.collision;

import com.jme3.math.Vector3f;

/**
 * Contains the results of a PhysicsSpace rayTest
 * @author normenhansen
 */
public class PhysicsSweepTestResult {

    private PhysicsCollisionObject collisionObject;
    private Vector3f hitNormalLocal;
    private float hitFraction;
    private boolean normalInWorldSpace;

    public PhysicsSweepTestResult() {
    }

    public PhysicsSweepTestResult(PhysicsCollisionObject collisionObject, Vector3f hitNormalLocal, float hitFraction, boolean normalInWorldSpace) {
        this.collisionObject = collisionObject;
        this.hitNormalLocal = hitNormalLocal;
        this.hitFraction = hitFraction;
        this.normalInWorldSpace = normalInWorldSpace;
    }

    /**
     * @return the collisionObject
     */
    public PhysicsCollisionObject getCollisionObject() {
        return collisionObject;
    }

    /**
     * @return the hitNormalLocal
     */
    public Vector3f getHitNormalLocal() {
        return hitNormalLocal;
    }

    /**
     * @return the hitFraction
     */
    public float getHitFraction() {
        return hitFraction;
    }

    /**
     * @return the normalInWorldSpace
     */
    public boolean isNormalInWorldSpace() {
        return normalInWorldSpace;
    }

    public void fill(PhysicsCollisionObject collisionObject, Vector3f hitNormalLocal, float hitFraction, boolean normalInWorldSpace) {
        this.collisionObject = collisionObject;
        this.hitNormalLocal = hitNormalLocal;
        this.hitFraction = hitFraction;
        this.normalInWorldSpace = normalInWorldSpace;
    }
}
