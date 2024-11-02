
package com.jme3.scene;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

/**
 * <code>CameraNode</code> simply uses {@link CameraControl} to implement
 * linking of camera and node data.
 *
 * @author Tim8Dev
 */
public class CameraNode extends Node {

    private CameraControl camControl;

    /**
     * Serialization only. Do not use.
     */
    protected CameraNode() {
        super();
    }

    public CameraNode(String name, Camera camera) {
        this(name, new CameraControl(camera));
    }

    public CameraNode(String name, CameraControl control) {
        super(name);
        addControl(control);
        camControl = control;
    }

    public void setEnabled(boolean enabled) {
        camControl.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return camControl.isEnabled();
    }

    public void setControlDir(ControlDirection controlDir) {
        camControl.setControlDir(controlDir);
    }

    public void setCamera(Camera camera) {
        camControl.setCamera(camera);
    }

    public ControlDirection getControlDir() {
        return camControl.getControlDir();
    }

    public Camera getCamera() {
        return camControl.getCamera();
    }

//    @Override
//    public void lookAt(Vector3f position, Vector3f upVector) {
//        this.lookAt(position, upVector);
//        camControl.getCamera().lookAt(position, upVector);
//    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
        super.cloneFields(cloner, original);

        // A change in behavior... I think previously CameraNode was probably
        // not really cloneable... or at least its camControl would be pointing
        // to the wrong control. -pspeed
        this.camControl = cloner.clone(camControl);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        camControl = (CameraControl)im.getCapsule(this).readSavable("camControl", null);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        ex.getCapsule(this).write(camControl, "camControl", null);
    }
}
