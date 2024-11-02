
package com.jme3.bullet.debug;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.bullet.util.DebugShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * A physics-debug control used to visualize a PhysicsGhostObject.
 * <p>
 * This class is shared between JBullet and Native Bullet.
 *
 * @author normenhansen
 */
public class BulletGhostObjectDebugControl extends AbstractPhysicsDebugControl {

    /**
     * ghost object to visualize (not null)
     */
    protected final PhysicsGhostObject body;
    /**
     * temporary storage for physics location
     */
    protected final Vector3f location = new Vector3f();
    /**
     * temporary storage for physics rotation
     */
    protected final Quaternion rotation = new Quaternion();
    /**
     * shape for which geom was generated (not null)
     */
    protected CollisionShape myShape;
    /**
     * geometry to visualize myShape (not null)
     */
    protected Spatial geom;
    /**
     * physics scale for which geom was generated
     */
    final private Vector3f oldScale = new Vector3f();
    /**
     * Instantiate an enabled control to visualize the specified ghost object.
     *
     * @param debugAppState which app state (not null, alias created)
     * @param body which object to visualize (not null, alias created)
     */
    public BulletGhostObjectDebugControl(BulletDebugAppState debugAppState, PhysicsGhostObject body) {
        super(debugAppState);
        this.body = body;
        myShape = body.getCollisionShape();
        oldScale.set(myShape.getScale());
        
        this.geom = DebugShapeFactory.getDebugShape(myShape);
        this.geom.setName(body.toString());
        geom.setMaterial(debugAppState.DEBUG_YELLOW);
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
            node.attachChild(geom);
        } else if (spatial == null && this.spatial != null) {
            Node node = (Node) this.spatial;
            node.detachChild(geom);
        }
        super.setSpatial(spatial);
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
        CollisionShape newShape = body.getCollisionShape();
        Vector3f newScale = newShape.getScale();
        if (myShape != newShape || !oldScale.equals(newScale)) {
            myShape = newShape;
            oldScale.set(newScale);

            Node node = (Node) spatial;
            node.detachChild(geom);

            geom = DebugShapeFactory.getDebugShape(myShape);
            geom.setName(body.toString());

            node.attachChild(geom);
        }
        geom.setMaterial(debugAppState.DEBUG_YELLOW);

        body.getPhysicsLocation(location);
        body.getPhysicsRotation(rotation);
        applyPhysicsTransform(location, rotation);
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
