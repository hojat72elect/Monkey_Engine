
package com.jme3.bullet.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;
import java.io.IOException;

/**
 * You might want to try <code>BetterCharacterControl</code> as well.
 * @author normenhansen
 */
public class CharacterControl extends PhysicsCharacter implements PhysicsControl, JmeCloneable {

    protected Spatial spatial;
    protected boolean enabled = true;
    protected boolean added = false;
    protected PhysicsSpace space = null;
    protected Vector3f viewDirection = new Vector3f(Vector3f.UNIT_Z);
    protected boolean useViewDirection = true;
    protected boolean applyLocal = false;

    public CharacterControl() {
    }

    public CharacterControl(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
    }

    public boolean isApplyPhysicsLocal() {
        return applyLocal;
    }

    /**
     * When set to true, the physics coordinates will be applied to the local
     * translation of the Spatial
     *
     * @param applyPhysicsLocal true&rarr;match local coordinates,
     * false&rarr;match world coordinates (default=false)
     */
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        applyLocal = applyPhysicsLocal;
    }

    private Vector3f getSpatialTranslation() {
        if (applyLocal) {
            return spatial.getLocalTranslation();
        }
        return spatial.getWorldTranslation();
    }

    @Deprecated
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object jmeClone() {
        CharacterControl control = new CharacterControl(collisionShape, stepHeight);
        control.setCcdMotionThreshold(getCcdMotionThreshold());
        control.setCcdSweptSphereRadius(getCcdSweptSphereRadius());
        control.setCollideWithGroups(getCollideWithGroups());
        control.setCollisionGroup(getCollisionGroup());
        control.setContactResponse(isContactResponse());
        control.setFallSpeed(getFallSpeed());
        control.setGravity(getGravity());
        control.setJumpSpeed(getJumpSpeed());
        control.setMaxSlope(getMaxSlope());
        control.setPhysicsLocation(getPhysicsLocation());
        control.setUpAxis(getUpAxis());
        control.setApplyPhysicsLocal(isApplyPhysicsLocal());
        control.spatial = this.spatial;
        control.setEnabled(isEnabled());
        return control;
    }     

    @Override
    public void cloneFields( Cloner cloner, Object original ) { 
        this.spatial = cloner.clone(spatial);
    }
         
    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        setUserObject(spatial);
        if (spatial == null) {
            return;
        }
        setPhysicsLocation(getSpatialTranslation());
    }

    /**
     * @return returns the spatial the control is added to, or null if the control is not attached to a spatial yet.
     */
    public Spatial getSpatial(){
        return this.spatial;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (space != null) {
            if (enabled && !added) {
                if (spatial != null) {
                    warp(getSpatialTranslation());
                }
                space.addCollisionObject(this);
                added = true;
            } else if (!enabled && added) {
                space.removeCollisionObject(this);
                added = false;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setViewDirection(Vector3f vec) {
        viewDirection.set(vec);
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public boolean isUseViewDirection() {
        return useViewDirection;
    }

    public void setUseViewDirection(boolean viewDirectionEnabled) {
        this.useViewDirection = viewDirectionEnabled;
    }

    @Override
    public void update(float tpf) {
        if (enabled && spatial != null) {
            Quaternion localRotationQuat = spatial.getLocalRotation();
            Vector3f localLocation = spatial.getLocalTranslation();
            if (!applyLocal && spatial.getParent() != null) {
                getPhysicsLocation(localLocation);
                localLocation.subtractLocal(spatial.getParent().getWorldTranslation());
                localLocation.divideLocal(spatial.getParent().getWorldScale());
                tmp_inverseWorldRotation.set(spatial.getParent().getWorldRotation()).inverseLocal().multLocal(localLocation);
                spatial.setLocalTranslation(localLocation);

                if (useViewDirection) {
                    localRotationQuat.lookAt(viewDirection, Vector3f.UNIT_Y);
                    spatial.setLocalRotation(localRotationQuat);
                }
            } else {
                spatial.setLocalTranslation(getPhysicsLocation());
                localRotationQuat.lookAt(viewDirection, Vector3f.UNIT_Y);
                spatial.setLocalRotation(localRotationQuat);
            }
        }
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    /**
     * If enabled, add this control's physics object to the specified physics
     * space. If not enabled, alter where the object would be added. The object
     * is removed from any other space it's currently in.
     *
     * @param newSpace where to add, or null to simply remove
     */
    @Override
    public void setPhysicsSpace(PhysicsSpace newSpace) {
        if (space == newSpace) {
            return;
        }
        if (added) {
            space.removeCollisionObject(this);
            added = false;
        }
        if (newSpace != null && isEnabled()) {
            newSpace.addCollisionObject(this);
            added = true;
        }
        /*
         * If this control isn't enabled, its physics object will be
         * added to the new space when the control becomes enabled.
         */
        space = newSpace;
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
        oc.write(applyLocal, "applyLocalPhysics", false);
        oc.write(useViewDirection, "viewDirectionEnabled", true);
        oc.write(viewDirection, "viewDirection", new Vector3f(Vector3f.UNIT_Z));
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        useViewDirection = ic.readBoolean("viewDirectionEnabled", true);
        viewDirection = (Vector3f) ic.readSavable("viewDirection", new Vector3f(Vector3f.UNIT_Z));
        applyLocal = ic.readBoolean("applyLocalPhysics", false);
        spatial = (Spatial) ic.readSavable("spatial", null);
        setUserObject(spatial);
    }
}
