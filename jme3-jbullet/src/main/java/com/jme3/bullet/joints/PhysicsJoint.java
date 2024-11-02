
package com.jme3.bullet.joints;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.export.*;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 * <p>PhysicsJoint - Basic Physics Joint</p>
 * @author normenhansen
 */
public abstract class PhysicsJoint implements Savable {

    protected TypedConstraint constraint;
    protected PhysicsRigidBody nodeA;
    protected PhysicsRigidBody nodeB;
    protected Vector3f pivotA;
    protected Vector3f pivotB;
    protected boolean collisionBetweenLinkedBodys = true;

    protected PhysicsJoint() {
    }

    /**
     * @param nodeA the body for the A end (not null, alias created)
     * @param nodeB the body for the B end (not null, alias created)
     * @param pivotA local translation of the joint connection point in node A
     * @param pivotB local translation of the joint connection point in node B
     */
    public PhysicsJoint(PhysicsRigidBody nodeA, PhysicsRigidBody nodeB, Vector3f pivotA, Vector3f pivotB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.pivotA = pivotA;
        this.pivotB = pivotB;
        nodeA.addJoint(this);
        nodeB.addJoint(this);
    }

    public float getAppliedImpulse(){
        return constraint.getAppliedImpulse();
    }

    /**
     * @return the constraint
     */
    public TypedConstraint getObjectId() {
        return constraint;
    }

    /**
     * @return the collisionBetweenLinkedBodys
     */
    public boolean isCollisionBetweenLinkedBodys() {
        return collisionBetweenLinkedBodys;
    }

    /**
     * toggles collisions between linked bodies<br>
     * joint has to be removed from and added to PhysicsSpace to apply this.
     * @param collisionBetweenLinkedBodies set to false to have no collisions between linked bodies
     */
    public void setCollisionBetweenLinkedBodys(boolean collisionBetweenLinkedBodies) {
        this.collisionBetweenLinkedBodys = collisionBetweenLinkedBodies;
    }

    public PhysicsRigidBody getBodyA() {
        return nodeA;
    }

    public PhysicsRigidBody getBodyB() {
        return nodeB;
    }

    public Vector3f getPivotA() {
        return pivotA;
    }

    public Vector3f getPivotB() {
        return pivotB;
    }

    /**
     * destroys this joint and removes it from its connected PhysicsRigidBody's joint lists
     */
    public void destroy() {
        getBodyA().removeJoint(this);
        getBodyB().removeJoint(this);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(nodeA, "nodeA", null);
        capsule.write(nodeB, "nodeB", null);
        capsule.write(pivotA, "pivotA", null);
        capsule.write(pivotB, "pivotB", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        this.nodeA = ((PhysicsRigidBody) capsule.readSavable("nodeA", null));
        this.nodeB = (PhysicsRigidBody) capsule.readSavable("nodeB", null);
        this.pivotA = (Vector3f) capsule.readSavable("pivotA", new Vector3f());
        this.pivotB = (Vector3f) capsule.readSavable("pivotB", new Vector3f());
    }

}
