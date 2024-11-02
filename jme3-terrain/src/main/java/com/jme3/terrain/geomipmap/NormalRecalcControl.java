
package com.jme3.terrain.geomipmap;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.clone.Cloner;
import java.io.IOException;


/**
 * Handles the normal vector updates when the terrain changes heights.
 * @author bowens
 */
public class NormalRecalcControl extends AbstractControl {

    private TerrainQuad terrain;

    public NormalRecalcControl(){}

    public NormalRecalcControl(TerrainQuad terrain) {
        this.terrain = terrain;
    }

    @Override
    protected void controlUpdate(float tpf) {
        terrain.updateNormals();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public Object jmeClone() {
        NormalRecalcControl control = (NormalRecalcControl)super.jmeClone();
        control.setEnabled(true);
        return control;
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields( Cloner cloner, Object original ) {
        super.cloneFields(cloner, original);
        this.terrain = cloner.clone(terrain);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (spatial instanceof TerrainQuad)
            this.terrain = (TerrainQuad)spatial;
    }

    public TerrainQuad getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainQuad terrain) {
        this.terrain = terrain;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(terrain, "terrain", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        terrain = (TerrainQuad) ic.readSavable("terrain", null);
    }

}
