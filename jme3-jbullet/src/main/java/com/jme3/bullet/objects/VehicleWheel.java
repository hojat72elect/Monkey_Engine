
package com.jme3.bullet.objects;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.util.Converter;
import com.jme3.export.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 * Stores info about one wheel of a PhysicsVehicle
 * @author normenhansen
 */
public class VehicleWheel implements Savable {

    protected com.bulletphysics.dynamics.vehicle.WheelInfo wheelInfo;
    protected boolean frontWheel;
    protected Vector3f location = new Vector3f();
    protected Vector3f direction = new Vector3f();
    protected Vector3f axle = new Vector3f();
    protected float suspensionStiffness = 20.0f;
    protected float wheelsDampingRelaxation = 2.3f;
    protected float wheelsDampingCompression = 4.4f;
    protected float frictionSlip = 10.5f;
    protected float rollInfluence = 1.0f;
    protected float maxSuspensionTravelCm = 500f;
    protected float maxSuspensionForce = 6000f;
    protected float radius = 0.5f;
    protected float restLength = 1f;
    protected Vector3f wheelWorldLocation = new Vector3f();
    protected Quaternion wheelWorldRotation = new Quaternion();
    protected Spatial wheelSpatial;
    protected com.jme3.math.Matrix3f tmp_Matrix = new com.jme3.math.Matrix3f();
    protected final Quaternion tmp_inverseWorldRotation = new Quaternion();
    private boolean applyLocal = false;

    protected VehicleWheel() {
    }

    public VehicleWheel(Spatial spat, Vector3f location, Vector3f direction, Vector3f axle,
            float restLength, float radius, boolean frontWheel) {
        this(location, direction, axle, restLength, radius, frontWheel);
        wheelSpatial = spat;
    }

    public VehicleWheel(Vector3f location, Vector3f direction, Vector3f axle,
            float restLength, float radius, boolean frontWheel) {
        this.location.set(location);
        this.direction.set(direction);
        this.axle.set(axle);
        this.frontWheel = frontWheel;
        this.restLength = restLength;
        this.radius = radius;
    }

    public void updatePhysicsState() {
        Converter.convert(wheelInfo.worldTransform.origin, wheelWorldLocation);
        Converter.convert(wheelInfo.worldTransform.basis, tmp_Matrix);
        wheelWorldRotation.fromRotationMatrix(tmp_Matrix);
    }

    public void applyWheelTransform() {
        if (wheelSpatial == null) {
            return;
        }
        Quaternion localRotationQuat = wheelSpatial.getLocalRotation();
        Vector3f localLocation = wheelSpatial.getLocalTranslation();
        if (!applyLocal && wheelSpatial.getParent() != null) {
            localLocation.set(wheelWorldLocation).subtractLocal(wheelSpatial.getParent().getWorldTranslation());
            localLocation.divideLocal(wheelSpatial.getParent().getWorldScale());
            tmp_inverseWorldRotation.set(wheelSpatial.getParent().getWorldRotation()).inverseLocal().multLocal(localLocation);

            localRotationQuat.set(wheelWorldRotation);
            tmp_inverseWorldRotation.set(wheelSpatial.getParent().getWorldRotation()).inverseLocal().mult(localRotationQuat, localRotationQuat);

            wheelSpatial.setLocalTranslation(localLocation);
            wheelSpatial.setLocalRotation(localRotationQuat);
        } else {
            wheelSpatial.setLocalTranslation(wheelWorldLocation);
            wheelSpatial.setLocalRotation(wheelWorldRotation);
        }
    }

    public com.bulletphysics.dynamics.vehicle.WheelInfo getWheelInfo() {
        return wheelInfo;
    }

    public void setWheelInfo(com.bulletphysics.dynamics.vehicle.WheelInfo wheelInfo) {
        this.wheelInfo = wheelInfo;
        applyInfo();
    }

    public boolean isFrontWheel() {
        return frontWheel;
    }

    public void setFrontWheel(boolean frontWheel) {
        this.frontWheel = frontWheel;
        applyInfo();
    }

    public Vector3f getLocation() {
        return location;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getAxle() {
        return axle;
    }

    public float getSuspensionStiffness() {
        return suspensionStiffness;
    }

    /**
     * the stiffness constant for the suspension.  10.0 - Offroad buggy, 50.0 - Sports car, 200.0 - F1 Car
     *
     * @param suspensionStiffness the desired stiffness coefficient
     * (10&rarr;off-road buggy, 50&rarr;sports car, 200&rarr;Formula-1 race car,
     * default=20)
     */
    public void setSuspensionStiffness(float suspensionStiffness) {
        this.suspensionStiffness = suspensionStiffness;
        applyInfo();
    }

    public float getWheelsDampingRelaxation() {
        return wheelsDampingRelaxation;
    }

    /**
     * the damping coefficient for when the suspension is expanding.
     * See the comments for setWheelsDampingCompression for how to set k.
     *
     * @param wheelsDampingRelaxation the desired damping coefficient
     * (default=2.3)
     */
    public void setWheelsDampingRelaxation(float wheelsDampingRelaxation) {
        this.wheelsDampingRelaxation = wheelsDampingRelaxation;
        applyInfo();
    }

    public float getWheelsDampingCompression() {
        return wheelsDampingCompression;
    }

    /**
     * the damping coefficient for when the suspension is compressed.
     * Set to k * 2.0 * FastMath.sqrt(m_suspensionStiffness) so k is proportional to critical damping.<br>
     * k = 0.0 undamped/bouncy, k = 1.0 critical damping<br>
     * 0.1 to 0.3 are good values
     *
     * @param wheelsDampingCompression the desired damping coefficient
     * (default=4.4)
     */
    public void setWheelsDampingCompression(float wheelsDampingCompression) {
        this.wheelsDampingCompression = wheelsDampingCompression;
        applyInfo();
    }

    public float getFrictionSlip() {
        return frictionSlip;
    }

    /**
     * the coefficient of friction between the tyre and the ground.
     * Should be about 0.8 for realistic cars, but can be increased for better handling.
     * Set large (10000.0) for kart racers
     *
     * @param frictionSlip the desired coefficient of friction between tyre and
     * ground (0.8&rarr;realistic car, 10000&rarr;kart racer, default=10.5)
     */
    public void setFrictionSlip(float frictionSlip) {
        this.frictionSlip = frictionSlip;
        applyInfo();
    }

    public float getRollInfluence() {
        return rollInfluence;
    }

    /**
     * reduces the rolling torque applied from the wheels that cause the vehicle to roll over.
     * This is a bit of a hack, but it's quite effective. 0.0 = no roll, 1.0 = physical behaviour.
     * If m_frictionSlip is too high, you'll need to reduce this to stop the vehicle rolling over.
     * You should also try lowering the vehicle's centre of mass
     * @param rollInfluence the rollInfluence to set
     */
    public void setRollInfluence(float rollInfluence) {
        this.rollInfluence = rollInfluence;
        applyInfo();
    }

    public float getMaxSuspensionTravelCm() {
        return maxSuspensionTravelCm;
    }

    /**
     * the maximum distance the suspension can be compressed (centimetres)
     *
     * @param maxSuspensionTravelCm the desired maximum amount the suspension
     * can be compressed or expanded, relative to its rest length (in hundredths
     * of a physics-space unit, default=500)
     */
    public void setMaxSuspensionTravelCm(float maxSuspensionTravelCm) {
        this.maxSuspensionTravelCm = maxSuspensionTravelCm;
        applyInfo();
    }

    public float getMaxSuspensionForce() {
        return maxSuspensionForce;
    }

    /**
     * The maximum suspension force, raise this above the default 6000 if your suspension cannot
     * handle the weight of your vehicle.
     *
     * @param maxSuspensionForce the desired maximum force per wheel
     * (default=6000)
     */
    public void setMaxSuspensionForce(float maxSuspensionForce) {
        this.maxSuspensionForce = maxSuspensionForce;
        applyInfo();
    }

    private void applyInfo() {
        if (wheelInfo == null) {
            return;
        }
        wheelInfo.suspensionStiffness = suspensionStiffness;
        wheelInfo.wheelsDampingRelaxation = wheelsDampingRelaxation;
        wheelInfo.wheelsDampingCompression = wheelsDampingCompression;
        wheelInfo.frictionSlip = frictionSlip;
        wheelInfo.rollInfluence = rollInfluence;
        wheelInfo.maxSuspensionTravelCm = maxSuspensionTravelCm;
        wheelInfo.maxSuspensionForce = maxSuspensionForce;
        wheelInfo.wheelsRadius = radius;
        wheelInfo.bIsFrontWheel = frontWheel;
        wheelInfo.suspensionRestLength1 = restLength;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        applyInfo();
    }

    public float getRestLength() {
        return restLength;
    }

    public void setRestLength(float restLength) {
        this.restLength = restLength;
        applyInfo();
    }

    /**
     * returns the object this wheel is in contact with or null if no contact
     * @return the PhysicsCollisionObject (PhysicsRigidBody, PhysicsGhostObject)
     */
    public PhysicsCollisionObject getGroundObject() {
        if (wheelInfo.raycastInfo.groundObject == null) {
            return null;
        } else if (wheelInfo.raycastInfo.groundObject instanceof RigidBody) {
            System.out.println("RigidBody");
            return (PhysicsRigidBody) ((RigidBody) wheelInfo.raycastInfo.groundObject).getUserPointer();
        } else {
            return null;
        }
    }

    /**
     * returns the location where the wheel collides with the ground (world space)
     *
     * @param vec storage for the result (not null, modified)
     * @return a location vector (in physics-space coordinates)
     */
    public Vector3f getCollisionLocation(Vector3f vec) {
        Converter.convert(wheelInfo.raycastInfo.contactPointWS, vec);
        return vec;
    }

    /**
     * returns the location where the wheel collides with the ground (world space)
     *
     * @return a new location vector (in physics-space coordinates)
     */
    public Vector3f getCollisionLocation() {
        return Converter.convert(wheelInfo.raycastInfo.contactPointWS);
    }

    /**
     * returns the normal where the wheel collides with the ground (world space)
     *
     * @param vec storage for the result (not null, modified)
     * @return a unit vector (in physics-space coordinates)
     */
    public Vector3f getCollisionNormal(Vector3f vec) {
        Converter.convert(wheelInfo.raycastInfo.contactNormalWS, vec);
        return vec;
    }

    /**
     * returns the normal where the wheel collides with the ground (world space)
     *
     * @return a new unit vector (in physics-space coordinates)
     */
    public Vector3f getCollisionNormal() {
        return Converter.convert(wheelInfo.raycastInfo.contactNormalWS);
    }

    /**
     * returns how much the wheel skids on the ground (for skid sounds/smoke etc.)<br>
     * 0.0 = wheels are sliding, 1.0 = wheels have traction.
     *
     * @return the relative amount of traction (0&rarr;wheel is sliding,
     * 1&rarr;wheel has full traction)
     */
    public float getSkidInfo() {
        return wheelInfo.skidInfo;
    }
    
    /**
     * returns how many degrees the wheel has turned since the last physics
     * step.
     *
     * @return the rotation angle (in radians)
     */
    public float getDeltaRotation() {
        return wheelInfo.deltaRotation;
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        wheelSpatial = (Spatial) capsule.readSavable("wheelSpatial", null);
        frontWheel = capsule.readBoolean("frontWheel", false);
        location = (Vector3f) capsule.readSavable("wheelLocation", new Vector3f());
        direction = (Vector3f) capsule.readSavable("wheelDirection", new Vector3f());
        axle = (Vector3f) capsule.readSavable("wheelAxle", new Vector3f());
        suspensionStiffness = capsule.readFloat("suspensionStiffness", 20.0f);
        wheelsDampingRelaxation = capsule.readFloat("wheelsDampingRelaxation", 2.3f);
        wheelsDampingCompression = capsule.readFloat("wheelsDampingCompression", 4.4f);
        frictionSlip = capsule.readFloat("frictionSlip", 10.5f);
        rollInfluence = capsule.readFloat("rollInfluence", 1.0f);
        maxSuspensionTravelCm = capsule.readFloat("maxSuspensionTravelCm", 500f);
        maxSuspensionForce = capsule.readFloat("maxSuspensionForce", 6000f);
        radius = capsule.readFloat("wheelRadius", 0.5f);
        restLength = capsule.readFloat("restLength", 1f);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(wheelSpatial, "wheelSpatial", null);
        capsule.write(frontWheel, "frontWheel", false);
        capsule.write(location, "wheelLocation", new Vector3f());
        capsule.write(direction, "wheelDirection", new Vector3f());
        capsule.write(axle, "wheelAxle", new Vector3f());
        capsule.write(suspensionStiffness, "suspensionStiffness", 20.0f);
        capsule.write(wheelsDampingRelaxation, "wheelsDampingRelaxation", 2.3f);
        capsule.write(wheelsDampingCompression, "wheelsDampingCompression", 4.4f);
        capsule.write(frictionSlip, "frictionSlip", 10.5f);
        capsule.write(rollInfluence, "rollInfluence", 1.0f);
        capsule.write(maxSuspensionTravelCm, "maxSuspensionTravelCm", 500f);
        capsule.write(maxSuspensionForce, "maxSuspensionForce", 6000f);
        capsule.write(radius, "wheelRadius", 0.5f);
        capsule.write(restLength, "restLength", 1f);
    }

    /**
     * @return the wheelSpatial
     */
    public Spatial getWheelSpatial() {
        return wheelSpatial;
    }

    /**
     * @param wheelSpatial the wheelSpatial to set
     */
    public void setWheelSpatial(Spatial wheelSpatial) {
        this.wheelSpatial = wheelSpatial;
    }

    public boolean isApplyLocal() {
        return applyLocal;
    }

    public void setApplyLocal(boolean applyLocal) {
        this.applyLocal = applyLocal;
    }

    /**
    * write the content of the wheelWorldRotation into the store
    * 
    * @param store storage for the result (not null, modified)
    */
    public void getWheelWorldRotation(final Quaternion store) {
        store.set(this.wheelWorldRotation);
    }

    /**
    * write the content of the wheelWorldLocation into the store
    * 
    * @param store storage for the result (not null, modified)
    */
    public void getWheelWorldLocation(final Vector3f store) {
        store.set(this.wheelWorldLocation);
    }

}
