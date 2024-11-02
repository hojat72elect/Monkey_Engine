
package com.jme3.scene.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.TempVars;
import java.io.IOException;

/**
 * This Control maintains a reference to a Camera,
 * which will be synched with the position (worldTranslation)
 * of the current spatial.
 * @author tim
 */
public class CameraControl extends AbstractControl {

    public static enum ControlDirection {

        /**
         * Means, that the Camera's transform is "copied"
         * to the Transform of the Spatial.
         */
        CameraToSpatial,
        /**
         * Means, that the Spatial's transform is "copied"
         * to the Transform of the Camera.
         */
        SpatialToCamera;
    }
    private Camera camera;
    private ControlDirection controlDir = ControlDirection.SpatialToCamera;

    /**
     * Constructor used for Serialization.
     */
    public CameraControl() {
    }

    /**
     * @param camera The Camera to be synced.
     */
    public CameraControl(Camera camera) {
        this.camera = camera;
    }

    /**
     * @param camera The Camera to be synced.
     * @param controlDir SpatialToCamera or CameraToSpatial
     */
    public CameraControl(Camera camera, ControlDirection controlDir) {
        this.camera = camera;
        this.controlDir = controlDir;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public ControlDirection getControlDir() {
        return controlDir;
    }

    public void setControlDir(ControlDirection controlDir) {
        this.controlDir = controlDir;
    }

    // fields used, when inverting ControlDirection:
    @Override
    protected void controlUpdate(float tpf) {
        if (spatial != null && camera != null) {
            switch (controlDir) {
                case SpatialToCamera:
                    camera.setLocation(spatial.getWorldTranslation());
                    camera.setRotation(spatial.getWorldRotation());
                    break;
                case CameraToSpatial:
                    // Set the local transform so that the world transform would be equal to the camera's transform.
                    // Location:
                    TempVars vars = TempVars.get();

                    Vector3f vecDiff = vars.vect1.set(camera.getLocation()).subtractLocal(spatial.getWorldTranslation());
                    spatial.setLocalTranslation(vecDiff.addLocal(spatial.getLocalTranslation()));

                    // Rotation:
                    Quaternion worldDiff = vars.quat1.set(camera.getRotation()).subtractLocal(spatial.getWorldRotation());
                    spatial.setLocalRotation(worldDiff.addLocal(spatial.getLocalRotation()));
                    vars.release();
                    break;
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // nothing to do
    }

    // default implementation from AbstractControl is equivalent
    //@Override
    //public Control cloneForSpatial(Spatial newSpatial) {
    //    CameraControl control = new CameraControl(camera, controlDir);
    //    control.setSpatial(newSpatial);
    //    control.setEnabled(isEnabled());
    //    return control;
    //}
    private static final String CONTROL_DIR_NAME = "controlDir";
    private static final String CAMERA_NAME = "camera";
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        controlDir = ic.readEnum(CONTROL_DIR_NAME, ControlDirection.class, ControlDirection.SpatialToCamera);
        camera = (Camera)ic.readSavable(CAMERA_NAME, null);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(controlDir, CONTROL_DIR_NAME, ControlDirection.SpatialToCamera);
        oc.write(camera, CAMERA_NAME, null);
    }
}