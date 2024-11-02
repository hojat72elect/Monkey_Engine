
package com.jme3.bullet.joints.motors;

import com.jme3.bullet.util.Converter;
import com.jme3.math.Vector3f;

/**
 *
 * @author normenhansen
 */
public class TranslationalLimitMotor {

    private com.bulletphysics.dynamics.constraintsolver.TranslationalLimitMotor motor;

    public TranslationalLimitMotor(com.bulletphysics.dynamics.constraintsolver.TranslationalLimitMotor motor) {
        this.motor = motor;
    }

    public com.bulletphysics.dynamics.constraintsolver.TranslationalLimitMotor getMotor() {
        return motor;
    }

    public Vector3f getLowerLimit() {
        return Converter.convert(motor.lowerLimit);
    }

    public void setLowerLimit(Vector3f lowerLimit) {
        Converter.convert(lowerLimit, motor.lowerLimit);
    }

    public Vector3f getUpperLimit() {
        return Converter.convert(motor.upperLimit);
    }

    public void setUpperLimit(Vector3f upperLimit) {
        Converter.convert(upperLimit, motor.upperLimit);
    }

    public Vector3f getAccumulatedImpulse() {
        return Converter.convert(motor.accumulatedImpulse);
    }

    public void setAccumulatedImpulse(Vector3f accumulatedImpulse) {
        Converter.convert(accumulatedImpulse, motor.accumulatedImpulse);
    }

    public float getLimitSoftness() {
        return motor.limitSoftness;
    }

    public void setLimitSoftness(float limitSoftness) {
        motor.limitSoftness = limitSoftness;
    }

    public float getDamping() {
        return motor.damping;
    }

    public void setDamping(float damping) {
        motor.damping = damping;
    }

    public float getRestitution() {
        return motor.restitution;
    }

    public void setRestitution(float restitution) {
        motor.restitution = restitution;
    }
}
