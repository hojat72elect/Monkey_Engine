
package com.jme3.bullet.debug;

import com.jme3.bullet.objects.PhysicsVehicle;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;

/**
 * A physics-debug control used to visualize a PhysicsVehicle.
 * <p>
 * This class is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public class BulletVehicleDebugControl extends AbstractPhysicsDebugControl {

    protected final PhysicsVehicle body;
    protected final Node suspensionNode;
    protected final Vector3f location = new Vector3f();
    protected final Quaternion rotation = new Quaternion();

    /**
     * Instantiate an enabled control to visualize the specified vehicle.
     *
     * @param debugAppState which app state (not null, alias created)
     * @param body which vehicle to visualize (not null, alias created)
     */
    public BulletVehicleDebugControl(BulletDebugAppState debugAppState, PhysicsVehicle body) {
        super(debugAppState);
        this.body = body;
        suspensionNode = new Node("Suspension");
        createVehicle();
    }

    /**
     * Alter which spatial is controlled. Invoked when the control is added to
     * or removed from a spatial. Should be invoked only by a subclass or from
     * Spatial. Do not invoke directly from user code.
     *
     * @param spatial the spatial to control (or null)
     */
    @Override
    public void setSpatial(Spatial spatial) {
        if (spatial != null && spatial instanceof Node) {
            Node node = (Node) spatial;
            node.attachChild(suspensionNode);
        } else if (spatial == null && this.spatial != null) {
            Node node = (Node) this.spatial;
            node.detachChild(suspensionNode);
        }
        super.setSpatial(spatial);
    }

    private void createVehicle() {
        suspensionNode.detachAllChildren();
        for (int i = 0; i < body.getNumWheels(); i++) {
            VehicleWheel physicsVehicleWheel = body.getWheel(i);
            Vector3f location = physicsVehicleWheel.getLocation().clone();
            Vector3f direction = physicsVehicleWheel.getDirection().clone();
            Vector3f axle = physicsVehicleWheel.getAxle().clone();
            float restLength = physicsVehicleWheel.getRestLength();
            float radius = physicsVehicleWheel.getRadius();

            Arrow locArrow = new Arrow(location);
            Arrow axleArrow = new Arrow(axle.normalizeLocal().multLocal(0.3f));
            Arrow wheelArrow = new Arrow(direction.normalizeLocal().multLocal(radius));
            Arrow dirArrow = new Arrow(direction.normalizeLocal().multLocal(restLength));
            Geometry locGeom = new Geometry("WheelLocationDebugShape" + i, locArrow);
            Geometry dirGeom = new Geometry("WheelDirectionDebugShape" + i, dirArrow);
            Geometry axleGeom = new Geometry("WheelAxleDebugShape" + i, axleArrow);
            Geometry wheelGeom = new Geometry("WheelRadiusDebugShape" + i, wheelArrow);
            dirGeom.setLocalTranslation(location);
            axleGeom.setLocalTranslation(location.add(direction));
            wheelGeom.setLocalTranslation(location.add(direction));
            locGeom.setMaterial(debugAppState.DEBUG_MAGENTA);
            dirGeom.setMaterial(debugAppState.DEBUG_MAGENTA);
            axleGeom.setMaterial(debugAppState.DEBUG_MAGENTA);
            wheelGeom.setMaterial(debugAppState.DEBUG_MAGENTA);
            suspensionNode.attachChild(locGeom);
            suspensionNode.attachChild(dirGeom);
            suspensionNode.attachChild(axleGeom);
            suspensionNode.attachChild(wheelGeom);
        }
    }

    /**
     * Update this control. Invoked once per frame during the logical-state
     * update, provided the control is enabled and added to a scene. Should be
     * invoked only by a subclass or by AbstractControl.
     *
     * @param tpf the time interval between frames (in seconds, &ge;0)
     */
    @Override
    protected void controlUpdate(float tpf) {
        for (int i = 0; i < body.getNumWheels(); i++) {
            VehicleWheel physicsVehicleWheel = body.getWheel(i);
            Vector3f location = physicsVehicleWheel.getLocation().clone();
            Vector3f direction = physicsVehicleWheel.getDirection().clone();
            Vector3f axle = physicsVehicleWheel.getAxle().clone();
            float restLength = physicsVehicleWheel.getRestLength();
            float radius = physicsVehicleWheel.getRadius();

            Geometry locGeom = (Geometry) suspensionNode.getChild("WheelLocationDebugShape" + i);
            Geometry dirGeom = (Geometry) suspensionNode.getChild("WheelDirectionDebugShape" + i);
            Geometry axleGeom = (Geometry) suspensionNode.getChild("WheelAxleDebugShape" + i);
            Geometry wheelGeom = (Geometry) suspensionNode.getChild("WheelRadiusDebugShape" + i);

            Arrow locArrow = (Arrow) locGeom.getMesh();
            locArrow.setArrowExtent(location);
            Arrow axleArrow = (Arrow) axleGeom.getMesh();
            axleArrow.setArrowExtent(axle.normalizeLocal().multLocal(0.3f));
            Arrow wheelArrow = (Arrow) wheelGeom.getMesh();
            wheelArrow.setArrowExtent(direction.normalizeLocal().multLocal(radius));
            Arrow dirArrow = (Arrow) dirGeom.getMesh();
            dirArrow.setArrowExtent(direction.normalizeLocal().multLocal(restLength));

            dirGeom.setLocalTranslation(location);
            axleGeom.setLocalTranslation(location.addLocal(direction));
            wheelGeom.setLocalTranslation(location);
            i++;
        }
        applyPhysicsTransform(body.getPhysicsLocation(location), body.getPhysicsRotation(rotation));
    }

    /**
     * Render this control. Invoked once per frame, provided the
     * control is enabled and added to a scene. Should be invoked only by a
     * subclass or by AbstractControl.
     *
     * @param rm the render manager (not null)
     * @param vp the view port to render (not null)
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
