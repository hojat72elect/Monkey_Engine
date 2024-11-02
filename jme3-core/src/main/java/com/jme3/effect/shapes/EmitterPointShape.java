
package com.jme3.effect.shapes;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

public class EmitterPointShape implements EmitterShape {

    private Vector3f point;

    public EmitterPointShape() {
    }

    public EmitterPointShape(Vector3f point) {
        this.point = point;
    }

    @Override
    public EmitterShape deepClone() {
        try {
            EmitterPointShape clone = (EmitterPointShape) super.clone();
            clone.point = point.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
        this.point = cloner.clone(point);
    }

    @Override
    public void getRandomPoint(Vector3f store) {
        store.set(point);
    }

    /**
     * This method fills the point with data.
     * It does not fill the normal.
     * @param store the variable to store the point data
     * @param normal not used in this class
     */
    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        store.set(point);
    }

    public Vector3f getPoint() {
        return point;
    }

    public void setPoint(Vector3f point) {
        this.point = point;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(point, "point", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        this.point = (Vector3f) im.getCapsule(this).readSavable("point", null);
    }
}
