
package jme3test.bullet;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.objects.PhysicsVehicle;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;
import java.io.IOException;

/**
 * PhysicsHoverControl uses a RayCast Vehicle with "slippery wheels" to simulate a hovering tank
 * @author normenhansen
 */
public class PhysicsHoverControl extends PhysicsVehicle implements PhysicsControl, PhysicsTickListener, JmeCloneable {

    protected Spatial spatial;
    protected boolean enabled = true;
    protected PhysicsSpace space = null;
    protected float steeringValue = 0;
    protected float accelerationValue = 0;
    protected int xw = 3;
    protected int zw = 5;
    protected int yw = 2;
    protected Vector3f HOVER_HEIGHT_LF_START = new Vector3f(xw, 1, zw);
    protected Vector3f HOVER_HEIGHT_RF_START = new Vector3f(-xw, 1, zw);
    protected Vector3f HOVER_HEIGHT_LR_START = new Vector3f(xw, 1, -zw);
    protected Vector3f HOVER_HEIGHT_RR_START = new Vector3f(-xw, 1, -zw);
    protected Vector3f HOVER_HEIGHT_LF = new Vector3f(xw, -yw, zw);
    protected Vector3f HOVER_HEIGHT_RF = new Vector3f(-xw, -yw, zw);
    protected Vector3f HOVER_HEIGHT_LR = new Vector3f(xw, -yw, -zw);
    protected Vector3f HOVER_HEIGHT_RR = new Vector3f(-xw, -yw, -zw);
    protected Vector3f tempVect1 = new Vector3f(0, 0, 0);
    protected Vector3f tempVect2 = new Vector3f(0, 0, 0);
    protected Vector3f tempVect3 = new Vector3f(0, 0, 0);
//    protected float rotationCounterForce = 10000f;
//    protected float speedCounterMult = 2000f;
//    protected float multiplier = 1000f;

    public PhysicsHoverControl() {
    }

    /**
     * Creates a new PhysicsNode with the supplied collision shape
     * @param shape the desired collision shape
     */
    public PhysicsHoverControl(CollisionShape shape) {
        super(shape);
        createWheels();
    }

    public PhysicsHoverControl(CollisionShape shape, float mass) {
        super(shape, mass);
        createWheels();
    }

    @Deprecated
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException();
    }

    @Override   
    public Object jmeClone() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }     

    @Override   
    public void cloneFields( Cloner cloner, Object original ) { 
        throw new UnsupportedOperationException("Not yet implemented.");
    }
         
    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        setUserObject(spatial);
        if (spatial == null) {
            return;
        }
        setPhysicsLocation(spatial.getWorldTranslation());
        setPhysicsRotation(spatial.getWorldRotation().toRotationMatrix());
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    private void createWheels() {
        addWheel(HOVER_HEIGHT_LF_START, new Vector3f(0, -1, 0), new Vector3f(-1, 0, 0), yw, yw, false);
        addWheel(HOVER_HEIGHT_RF_START, new Vector3f(0, -1, 0), new Vector3f(-1, 0, 0), yw, yw, false);
        addWheel(HOVER_HEIGHT_LR_START, new Vector3f(0, -1, 0), new Vector3f(-1, 0, 0), yw, yw, false);
        addWheel(HOVER_HEIGHT_RR_START, new Vector3f(0, -1, 0), new Vector3f(-1, 0, 0), yw, yw, false);
        for (int i = 0; i < 4; i++) {
            getWheel(i).setFrictionSlip(0.001f);
        }
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float f) {
        Vector3f angVel = getAngularVelocity();
        float rotationVelocity = angVel.getY();
        Vector3f dir = getForwardVector(tempVect2).multLocal(1, 0, 1).normalizeLocal();
        getLinearVelocity(tempVect3);
        Vector3f linearVelocity = tempVect3.multLocal(1, 0, 1);
        float groundSpeed = linearVelocity.length();

        if (steeringValue != 0) {
            if (rotationVelocity < 1 && rotationVelocity > -1) {
                applyTorque(tempVect1.set(0, steeringValue, 0));
            }
        } else {
            // counter the steering value!
            if (rotationVelocity > 0.2f) {
                applyTorque(tempVect1.set(0, -mass * 20, 0));
            } else if (rotationVelocity < -0.2f) {
                applyTorque(tempVect1.set(0, mass * 20, 0));
            }
        }
        if (accelerationValue > 0) {
            // counter force that will adjust velocity
            // if we are not going where we want to go.
            // this will prevent "drifting" and thus improve control
            // of the vehicle
            if (groundSpeed > FastMath.ZERO_TOLERANCE) {
                float d = dir.dot(linearVelocity.normalize());
                Vector3f counter = dir.project(linearVelocity).normalizeLocal().negateLocal().multLocal(1 - d);
                applyForce(counter.multLocal(mass * 10), Vector3f.ZERO);
            }

            if (linearVelocity.length() < 30) {
                applyForce(dir.multLocal(accelerationValue), Vector3f.ZERO);
            }
        } else {
            // counter the acceleration value
            if (groundSpeed > FastMath.ZERO_TOLERANCE) {
                linearVelocity.normalizeLocal().negateLocal();
                applyForce(linearVelocity.mult(mass * 10), Vector3f.ZERO);
            }
        }
    }

    @Override
    public void physicsTick(PhysicsSpace space, float f) {
    }

    @Override
    public void update(float tpf) {
        if (enabled && spatial != null) {
            getMotionState().applyTransform(spatial);
        }
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        createVehicle(space);
        if (space == null) {
            if (this.space != null) {
                this.space.removeCollisionObject(this);
                this.space.removeTickListener(this);
            }
            this.space = space;
        } else {
            space.addCollisionObject(this);
            space.addTickListener(this);
        }
        this.space = space;
    }

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return space;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
    }

    /**
     * @param steeringValue the steeringValue to set
     */
    @Override
    public void steer(float steeringValue) {
        this.steeringValue = steeringValue * getMass();
    }

    /**
     * @param accelerationValue the accelerationValue to set
     */
    @Override
    public void accelerate(float accelerationValue) {
        this.accelerationValue = accelerationValue * getMass();
    }

}
