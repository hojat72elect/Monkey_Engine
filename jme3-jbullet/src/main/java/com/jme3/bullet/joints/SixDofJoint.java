
package com.jme3.bullet.joints;

import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.linearmath.Transform;
import com.jme3.bullet.joints.motors.RotationalLimitMotor;
import com.jme3.bullet.joints.motors.TranslationalLimitMotor;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <i>From bullet manual:</i><br>
 * This generic constraint can emulate a variety of standard constraints,
 * by configuring each of the 6 degrees of freedom (DOF).
 * The first 3 DOF axes are linear axes, which represent translation of rigid bodies,
 * and the latter 3 DOF axes represent the angular motion. Each axis can be either locked,
 * free or limited. On construction of a new btGeneric6DofConstraint, all axis are locked.
 * Afterwards the axis can be reconfigured. Note that several combinations that
 * include free and/or limited angular degrees of freedom are undefined.
 * @author normenhansen
 */
public class SixDofJoint extends PhysicsJoint {

    private boolean useLinearReferenceFrameA = true;
    private LinkedList<RotationalLimitMotor> rotationalMotors = new LinkedList<>();
    private TranslationalLimitMotor translationalMotor;
    private Vector3f angularUpperLimit = new Vector3f(Vector3f.POSITIVE_INFINITY);
    private Vector3f angularLowerLimit = new Vector3f(Vector3f.NEGATIVE_INFINITY);
    private Vector3f linearUpperLimit = new Vector3f(Vector3f.POSITIVE_INFINITY);
    private Vector3f linearLowerLimit = new Vector3f(Vector3f.NEGATIVE_INFINITY);

    protected SixDofJoint() {
    }

    /**
     * @param nodeA the body for the A end (not null, alias created)
     * @param nodeB the body for the B end (not null, alias created)
     * @param pivotA local translation of the joint connection point in node A
     * @param pivotB local translation of the joint connection point in node B
     * @param rotA the joint orientation in A's local coordinates (rotation
     * matrix, unaffected)
     * @param rotB the joint orientation in B's local coordinates (rotation
     * matrix, unaffected)
     * @param useLinearReferenceFrameA true&rarr;use body A, false&rarr;use body
     */
    public SixDofJoint(PhysicsRigidBody nodeA, PhysicsRigidBody nodeB, Vector3f pivotA, Vector3f pivotB, Matrix3f rotA, Matrix3f rotB, boolean useLinearReferenceFrameA) {
        super(nodeA, nodeB, pivotA, pivotB);
        this.useLinearReferenceFrameA = useLinearReferenceFrameA;

        Transform transA = new Transform(Converter.convert(rotA));
        Converter.convert(pivotA, transA.origin);
        Converter.convert(rotA, transA.basis);

        Transform transB = new Transform(Converter.convert(rotB));
        Converter.convert(pivotB, transB.origin);
        Converter.convert(rotB, transB.basis);

        constraint = new Generic6DofConstraint(nodeA.getObjectId(), nodeB.getObjectId(), transA, transB, useLinearReferenceFrameA);
        gatherMotors();
    }

    /**
     * @param nodeA the body for the A end (not null, alias created)
     * @param nodeB the body for the B end (not null, alias created)
     * @param pivotA local translation of the joint connection point in node A
     * @param pivotB local translation of the joint connection point in node B
     * @param useLinearReferenceFrameA true&rarr;use body A, false&rarr;use body
     * B
     */
    public SixDofJoint(PhysicsRigidBody nodeA, PhysicsRigidBody nodeB, Vector3f pivotA, Vector3f pivotB, boolean useLinearReferenceFrameA) {
        super(nodeA, nodeB, pivotA, pivotB);
        this.useLinearReferenceFrameA = useLinearReferenceFrameA;

        Transform transA = new Transform(Converter.convert(new Matrix3f()));
        Converter.convert(pivotA, transA.origin);

        Transform transB = new Transform(Converter.convert(new Matrix3f()));
        Converter.convert(pivotB, transB.origin);

        constraint = new Generic6DofConstraint(nodeA.getObjectId(), nodeB.getObjectId(), transA, transB, useLinearReferenceFrameA);
        gatherMotors();
    }

    private void gatherMotors() {
        for (int i = 0; i < 3; i++) {
            RotationalLimitMotor rMotor = new RotationalLimitMotor(((Generic6DofConstraint) constraint).getRotationalLimitMotor(i));
            rotationalMotors.add(rMotor);
        }
        translationalMotor = new TranslationalLimitMotor(((Generic6DofConstraint) constraint).getTranslationalLimitMotor());
    }

    /**
     * returns the TranslationalLimitMotor of this 6DofJoint which allows
     * manipulating the translational axis
     * @return the TranslationalLimitMotor
     */
    public TranslationalLimitMotor getTranslationalLimitMotor() {
        return translationalMotor;
    }

    /**
     * returns one of the three RotationalLimitMotors of this 6DofJoint which
     * allow manipulating the rotational axes
     * @param index the index of the RotationalLimitMotor
     * @return the RotationalLimitMotor at the given index
     */
    public RotationalLimitMotor getRotationalLimitMotor(int index) {
        return rotationalMotors.get(index);
    }

    public void setLinearUpperLimit(Vector3f vector) {
        linearUpperLimit.set(vector);
        ((Generic6DofConstraint) constraint).setLinearUpperLimit(Converter.convert(vector));
    }

    public void setLinearLowerLimit(Vector3f vector) {
        linearLowerLimit.set(vector);
        ((Generic6DofConstraint) constraint).setLinearLowerLimit(Converter.convert(vector));
    }

    public void setAngularUpperLimit(Vector3f vector) {
        angularUpperLimit.set(vector);
        ((Generic6DofConstraint) constraint).setAngularUpperLimit(Converter.convert(vector));
    }

    public void setAngularLowerLimit(Vector3f vector) {
        angularLowerLimit.set(vector);
        ((Generic6DofConstraint) constraint).setAngularLowerLimit(Converter.convert(vector));
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);

        Transform transA = new Transform(Converter.convert(new Matrix3f()));
        Converter.convert(pivotA, transA.origin);

        Transform transB = new Transform(Converter.convert(new Matrix3f()));
        Converter.convert(pivotB, transB.origin);
        constraint = new Generic6DofConstraint(nodeA.getObjectId(), nodeB.getObjectId(), transA, transB, useLinearReferenceFrameA);
        gatherMotors();

        setAngularUpperLimit((Vector3f) capsule.readSavable("angularUpperLimit", new Vector3f(Vector3f.POSITIVE_INFINITY)));
        setAngularLowerLimit((Vector3f) capsule.readSavable("angularLowerLimit", new Vector3f(Vector3f.NEGATIVE_INFINITY)));
        setLinearUpperLimit((Vector3f) capsule.readSavable("linearUpperLimit", new Vector3f(Vector3f.POSITIVE_INFINITY)));
        setLinearLowerLimit((Vector3f) capsule.readSavable("linearLowerLimit", new Vector3f(Vector3f.NEGATIVE_INFINITY)));

        for (int i = 0; i < 3; i++) {
            RotationalLimitMotor rotationalLimitMotor = getRotationalLimitMotor(i);
            rotationalLimitMotor.setBounce(capsule.readFloat("rotMotor" + i + "_Bounce", 0.0f));
            rotationalLimitMotor.setDamping(capsule.readFloat("rotMotor" + i + "_Damping", 1.0f));
            rotationalLimitMotor.setERP(capsule.readFloat("rotMotor" + i + "_ERP", 0.5f));
            rotationalLimitMotor.setHiLimit(capsule.readFloat("rotMotor" + i + "_HiLimit", Float.POSITIVE_INFINITY));
            rotationalLimitMotor.setLimitSoftness(capsule.readFloat("rotMotor" + i + "_LimitSoftness", 0.5f));
            rotationalLimitMotor.setLoLimit(capsule.readFloat("rotMotor" + i + "_LoLimit", Float.NEGATIVE_INFINITY));
            rotationalLimitMotor.setMaxLimitForce(capsule.readFloat("rotMotor" + i + "_MaxLimitForce", 300.0f));
            rotationalLimitMotor.setMaxMotorForce(capsule.readFloat("rotMotor" + i + "_MaxMotorForce", 0.1f));
            rotationalLimitMotor.setTargetVelocity(capsule.readFloat("rotMotor" + i + "_TargetVelocity", 0));
            rotationalLimitMotor.setEnableMotor(capsule.readBoolean("rotMotor" + i + "_EnableMotor", false));
        }
        getTranslationalLimitMotor().setAccumulatedImpulse((Vector3f) capsule.readSavable("transMotor_AccumulatedImpulse", Vector3f.ZERO));
        getTranslationalLimitMotor().setDamping(capsule.readFloat("transMotor_Damping", 1.0f));
        getTranslationalLimitMotor().setLimitSoftness(capsule.readFloat("transMotor_LimitSoftness", 0.7f));
        getTranslationalLimitMotor().setLowerLimit((Vector3f) capsule.readSavable("transMotor_LowerLimit", Vector3f.ZERO));
        getTranslationalLimitMotor().setRestitution(capsule.readFloat("transMotor_Restitution", 0.5f));
        getTranslationalLimitMotor().setUpperLimit((Vector3f) capsule.readSavable("transMotor_UpperLimit", Vector3f.ZERO));
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(angularUpperLimit, "angularUpperLimit", new Vector3f(Vector3f.POSITIVE_INFINITY));
        capsule.write(angularLowerLimit, "angularLowerLimit", new Vector3f(Vector3f.NEGATIVE_INFINITY));
        capsule.write(linearUpperLimit, "linearUpperLimit", new Vector3f(Vector3f.POSITIVE_INFINITY));
        capsule.write(linearLowerLimit, "linearLowerLimit", new Vector3f(Vector3f.NEGATIVE_INFINITY));
        int i = 0;
        for (Iterator<RotationalLimitMotor> it = rotationalMotors.iterator(); it.hasNext();) {
            RotationalLimitMotor rotationalLimitMotor = it.next();
            capsule.write(rotationalLimitMotor.getBounce(), "rotMotor" + i + "_Bounce", 0.0f);
            capsule.write(rotationalLimitMotor.getDamping(), "rotMotor" + i + "_Damping", 1.0f);
            capsule.write(rotationalLimitMotor.getERP(), "rotMotor" + i + "_ERP", 0.5f);
            capsule.write(rotationalLimitMotor.getHiLimit(), "rotMotor" + i + "_HiLimit", Float.POSITIVE_INFINITY);
            capsule.write(rotationalLimitMotor.getLimitSoftness(), "rotMotor" + i + "_LimitSoftness", 0.5f);
            capsule.write(rotationalLimitMotor.getLoLimit(), "rotMotor" + i + "_LoLimit", Float.NEGATIVE_INFINITY);
            capsule.write(rotationalLimitMotor.getMaxLimitForce(), "rotMotor" + i + "_MaxLimitForce", 300.0f);
            capsule.write(rotationalLimitMotor.getMaxMotorForce(), "rotMotor" + i + "_MaxMotorForce", 0.1f);
            capsule.write(rotationalLimitMotor.getTargetVelocity(), "rotMotor" + i + "_TargetVelocity", 0);
            capsule.write(rotationalLimitMotor.isEnableMotor(), "rotMotor" + i + "_EnableMotor", false);
            i++;
        }
        capsule.write(getTranslationalLimitMotor().getAccumulatedImpulse(), "transMotor_AccumulatedImpulse", Vector3f.ZERO);
        capsule.write(getTranslationalLimitMotor().getDamping(), "transMotor_Damping", 1.0f);
        capsule.write(getTranslationalLimitMotor().getLimitSoftness(), "transMotor_LimitSoftness", 0.7f);
        capsule.write(getTranslationalLimitMotor().getLowerLimit(), "transMotor_LowerLimit", Vector3f.ZERO);
        capsule.write(getTranslationalLimitMotor().getRestitution(), "transMotor_Restitution", 0.5f);
        capsule.write(getTranslationalLimitMotor().getUpperLimit(), "transMotor_UpperLimit", Vector3f.ZERO);
    }
}
