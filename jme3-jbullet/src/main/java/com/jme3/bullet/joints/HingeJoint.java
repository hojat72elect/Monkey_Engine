
package com.jme3.bullet.joints;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
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
 * Hinge constraint, or revolute joint restricts two additional angular degrees of freedom,
 * so the body can only rotate around one axis, the hinge axis.
 * This can be useful to represent doors or wheels rotating around one axis.
 * The user can specify limits and motor for the hinge.
 * @author normenhansen
 */
public class HingeJoint extends PhysicsJoint {

    protected Vector3f axisA;
    protected Vector3f axisB;
    protected boolean angularOnly = false;
    protected float biasFactor = 0.3f;
    protected float relaxationFactor = 1.0f;
    protected float limitSoftness = 0.9f;

    protected HingeJoint() {
    }

    /**
     * Creates a new HingeJoint
     *
     * @param nodeA the body for the A end (not null, alias created)
     * @param nodeB the body for the B end (not null, alias created)
     * @param pivotA local translation of the joint connection point in node A
     * @param pivotB local translation of the joint connection point in node B
     * @param axisA the joint axis in A's local coordinates (unit vector,
     * alias created)
     * @param axisB the joint axis in B's local coordinates (unit vector,
     * alias created)
     */
    public HingeJoint(PhysicsRigidBody nodeA, PhysicsRigidBody nodeB, Vector3f pivotA, Vector3f pivotB, Vector3f axisA, Vector3f axisB) {
        super(nodeA, nodeB, pivotA, pivotB);
        this.axisA = axisA;
        this.axisB = axisB;
        createJoint();
    }

    /**
     * Enables the motor.
     * @param enable if true, motor is enabled.
     * @param targetVelocity the target velocity of the rotation.
     * @param maxMotorImpulse the max force applied to the hinge to rotate it.
     */
    public void enableMotor(boolean enable, float targetVelocity, float maxMotorImpulse) {
        ((HingeConstraint) constraint).enableAngularMotor(enable, targetVelocity, maxMotorImpulse);
    }

    /**
     * Sets the limits of this joint.
     * @param low the low limit in radians.
     * @param high the high limit in radians.
     */
    public void setLimit(float low, float high) {
        ((HingeConstraint) constraint).setLimit(low, high);
    }

    /**
     * Sets the limits of this joint.
     * If you're above the softness, velocities that would shoot through the actual limit are slowed down. The bias be in the range of 0.2 - 0.5.
     * @param low the low limit in radians.
     * @param high the high limit in radians.
     * @param _softness the factor at which the velocity error correction starts operating,i.e a softness of 0.9 means that the vel. corr starts at 90% of the limit range.
     * @param _biasFactor the magnitude of the position correction. It tells you how strictly the position error (drift ) is corrected.
     * @param _relaxationFactor the rate at which velocity errors are corrected. This can be seen as the strength of the limits. A low value will make the limits more spongy.
     */
    public void setLimit(float low, float high, float _softness, float _biasFactor, float _relaxationFactor) {
        biasFactor = _biasFactor;
        relaxationFactor = _relaxationFactor;
        limitSoftness = _softness;
        ((HingeConstraint) constraint).setLimit(low, high, _softness, _biasFactor, _relaxationFactor);
    }

    public float getUpperLimit(){
        return ((HingeConstraint) constraint).getUpperLimit();
    }

    public float getLowerLimit(){
        return ((HingeConstraint) constraint).getLowerLimit();
    }

    public void setAngularOnly(boolean angularOnly) {
        this.angularOnly = angularOnly;
        ((HingeConstraint) constraint).setAngularOnly(angularOnly);
    }

    public float getHingeAngle() {
        return ((HingeConstraint) constraint).getHingeAngle();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(axisA, "axisA", new Vector3f());
        capsule.write(axisB, "axisB", new Vector3f());

        capsule.write(angularOnly, "angularOnly", false);

        capsule.write(((HingeConstraint) constraint).getLowerLimit(), "lowerLimit", 1e30f);
        capsule.write(((HingeConstraint) constraint).getUpperLimit(), "upperLimit", -1e30f);

        capsule.write(biasFactor, "biasFactor", 0.3f);
        capsule.write(relaxationFactor, "relaxationFactor", 1f);
        capsule.write(limitSoftness, "limitSoftness", 0.9f);

        capsule.write(((HingeConstraint) constraint).getEnableAngularMotor(), "enableAngularMotor", false);
        capsule.write(((HingeConstraint) constraint).getMotorTargetVelosity(), "targetVelocity", 0.0f);
        capsule.write(((HingeConstraint) constraint).getMaxMotorImpulse(), "maxMotorImpulse", 0.0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        this.axisA = (Vector3f) capsule.readSavable("axisA", new Vector3f());
        this.axisB = (Vector3f) capsule.readSavable("axisB", new Vector3f());

        this.angularOnly = capsule.readBoolean("angularOnly", false);
        float lowerLimit = capsule.readFloat("lowerLimit", 1e30f);
        float upperLimit = capsule.readFloat("upperLimit", -1e30f);

        this.biasFactor = capsule.readFloat("biasFactor", 0.3f);
        this.relaxationFactor = capsule.readFloat("relaxationFactor", 1f);
        this.limitSoftness = capsule.readFloat("limitSoftness", 0.9f);

        boolean enableAngularMotor=capsule.readBoolean("enableAngularMotor", false);
        float targetVelocity=capsule.readFloat("targetVelocity", 0.0f);
        float maxMotorImpulse=capsule.readFloat("maxMotorImpulse", 0.0f);

        createJoint();
        enableMotor(enableAngularMotor, targetVelocity, maxMotorImpulse);
        ((HingeConstraint) constraint).setLimit(lowerLimit, upperLimit, limitSoftness, biasFactor, relaxationFactor);
    }

    protected void createJoint() {
        constraint = new HingeConstraint(nodeA.getObjectId(), nodeB.getObjectId(),
                Converter.convert(pivotA), Converter.convert(pivotB),
                Converter.convert(axisA), Converter.convert(axisB));
        ((HingeConstraint) constraint).setAngularOnly(angularOnly);
    }
}
