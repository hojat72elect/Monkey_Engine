
package com.jme3.scene.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;
import java.io.IOException;

/**
 * An abstract implementation of the Control interface.
 *
 * @author Kirill Vainer
 */
public abstract class AbstractControl implements Control, JmeCloneable {

    protected boolean enabled = true;
    protected Spatial spatial;

    public AbstractControl(){
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (this.spatial != null && spatial != null && spatial != this.spatial) {
            throw new IllegalStateException("This control has already been added to a Spatial");
        }
        this.spatial = spatial;
    }

    public Spatial getSpatial(){
        return spatial;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * To be implemented in subclass.
     *
     * @param tpf time per frame (in seconds)
     */
    protected abstract void controlUpdate(float tpf);

    /**
     * To be implemented in subclass.
     *
     * @param rm the RenderManager rendering the controlled Spatial (not null)
     * @param vp the ViewPort being rendered (not null)
     */
    protected abstract void controlRender(RenderManager rm, ViewPort vp);

    @Deprecated
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone control for spatial", e);
        }
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        this.spatial = cloner.clone(spatial);
    }

    @Override
    public void update(float tpf) {
        if (!enabled)
            return;

        controlUpdate(tpf);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        if (!enabled)
            return;

        controlRender(rm, vp);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
    }
}
