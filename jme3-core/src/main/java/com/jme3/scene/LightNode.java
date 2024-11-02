
package com.jme3.scene;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.light.Light;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.control.LightControl.ControlDirection;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

/**
 * <code>LightNode</code> is used to link together a {@link Light} object
 * with a {@link Node} object.
 *
 * @author Tim8Dev
 * @deprecated Use a {@link LightControl} attached to a {@link Node} directly.
 */
@Deprecated
public class LightNode extends Node {

    private LightControl lightControl;

    /**
     * Serialization only. Do not use.
     */
    protected LightNode() {
    }

    public LightNode(String name, Light light) {
        this(name, new LightControl(light));
    }

    public LightNode(String name, LightControl control) {
        super(name);
        addControl(control);
        lightControl = control;
    }

    /**
     * Enable or disable the <code>LightNode</code> functionality.
     *
     * @param enabled If false, the functionality of LightNode will
     * be disabled.
     */
    public void setEnabled(boolean enabled) {
        lightControl.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return lightControl.isEnabled();
    }

    public void setControlDir(ControlDirection controlDir) {
        lightControl.setControlDir(controlDir);
    }

    public void setLight(Light light) {
        lightControl.setLight(light);
    }

    public ControlDirection getControlDir() {
        return lightControl.getControlDir();
    }

    public Light getLight() {
        return lightControl.getLight();
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
        super.cloneFields(cloner, original);

        // A change in behavior... I think previously LightNode was probably
        // not really cloneable... or at least its lightControl would be pointing
        // to the wrong control. -pspeed
        this.lightControl = cloner.clone(lightControl);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        lightControl = (LightControl)im.getCapsule(this).readSavable("lightControl", null);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        ex.getCapsule(this).write(lightControl, "lightControl", null);
    }
}
