
package com.jme3.cinematic.events;

import com.jme3.app.Application;
import com.jme3.cinematic.Cinematic;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 *
 * @author Rickard (neph1 @ github)
 */
public class CameraEvent extends AbstractCinematicEvent {

    private String cameraName;
    private Cinematic cinematic;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public CameraEvent() {
    }

    public CameraEvent(Cinematic parentEvent, String cameraName) {
        this.cinematic = parentEvent;
        this.cameraName = cameraName;
    }

    @Override
    public void initEvent(Application app, Cinematic cinematic) {
        super.initEvent(app, cinematic);
        this.cinematic = cinematic;
    }

    @Override
    public void play() {
        super.play();
        stop();
    }

    @Override
    public void onPlay() {
        cinematic.setActiveCamera(cameraName);
    }

    @Override
    public void onUpdate(float tpf) {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void forceStop() {
    }

    @Override
    public void setTime(float time) {
        play();
    }

    public Cinematic getCinematic() {
        return cinematic;
    }

    public void setCinematic(Cinematic cinematic) {
        this.cinematic = cinematic;
    }

    /**
     * used internally for serialization
     *
     * @param ex the exporter (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(cameraName, "cameraName", null);

    }

    /**
     * used internally for serialization
     *
     * @param im the importer (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        cameraName = ic.readString("cameraName", null);
    }
}
