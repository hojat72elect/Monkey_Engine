
package com.jme3.bullet.joints;

import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 * <i>From bullet manual:</i><br>
 * Point to point constraint, also known as ball socket joint limits the translation
 * so that the local pivot points of 2 rigid bodies match in worldspace.
 * A chain of rigid bodies can be connected using this constraint.
 * @author normenhansen
 */
public class Point2PointJoint extends PhysicsJoint {

    protected Point2PointJoint() {
    }

    /**
     * @param nodeA the body for the A end (not null, alias created)
     * @param nodeB the body for the B end (not null, alias created)
     * @param pivotA local translation of the joint connection point in node A
     * @param pivotB local translation of the joint connection point in node B
     */
    public Point2PointJoint(PhysicsRigidBody nodeA, PhysicsRigidBody nodeB, Vector3f pivotA, Vector3f pivotB) {
        super(nodeA, nodeB, pivotA, pivotB);
        createJoint();
    }

    public void setDamping(float value) {
        ((Point2PointConstraint) constraint).setting.damping = value;
    }

    public void setImpulseClamp(float value) {
        ((Point2PointConstraint) constraint).setting.impulseClamp = value;
    }

    public void setTau(float value) {
        ((Point2PointConstraint) constraint).setting.tau = value;
    }

    public float getDamping() {
        return ((Point2PointConstraint) constraint).setting.damping;
    }

    public float getImpulseClamp() {
        return ((Point2PointConstraint) constraint).setting.impulseClamp;
    }

    public float getTau() {
        return ((Point2PointConstraint) constraint).setting.tau;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule cap = ex.getCapsule(this);
        cap.write(getDamping(), "damping", 1.0f);
        cap.write(getTau(), "tau", 0.3f);
        cap.write(getImpulseClamp(), "impulseClamp", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        createJoint();
        InputCapsule cap=im.getCapsule(this);
        setDamping(cap.readFloat("damping", 1.0f));
        setDamping(cap.readFloat("tau", 0.3f));
        setDamping(cap.readFloat("impulseClamp", 0f));
    }

    protected void createJoint() {
        constraint = new Point2PointConstraint(nodeA.getObjectId(), nodeB.getObjectId(), Converter.convert(pivotA), Converter.convert(pivotB));
    }
}
