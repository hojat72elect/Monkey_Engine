
package com.jme3.effect.shapes;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

public class EmitterBoxShape implements EmitterShape {

    private Vector3f min, len;

    public EmitterBoxShape() {
    }

    public EmitterBoxShape(Vector3f min, Vector3f max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("min or max cannot be null");
        }

        this.min = min;
        this.len = new Vector3f();
        this.len.set(max).subtractLocal(min);
    }

    @Override
    public void getRandomPoint(Vector3f store) {
        store.x = min.x + len.x * FastMath.nextRandomFloat();
        store.y = min.y + len.y * FastMath.nextRandomFloat();
        store.z = min.z + len.z * FastMath.nextRandomFloat();
    }

    /**
     * This method fills the point with data.
     * It does not fill the normal.
     * @param store the variable to store the point data
     * @param normal not used in this class
     */
    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        this.getRandomPoint(store);
    }

    @Override
    public EmitterShape deepClone() {
        try {
            EmitterBoxShape clone = (EmitterBoxShape) super.clone();
            clone.min = min.clone();
            clone.len = len.clone();
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
        this.min = cloner.clone(min);
        this.len = cloner.clone(len);
    }

    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        this.min = min;
    }

    public Vector3f getLen() {
        return len;
    }

    public void setLen(Vector3f len) {
        this.len = len;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(min, "min", null);
        oc.write(len, "length", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        min = (Vector3f) ic.readSavable("min", null);
        len = (Vector3f) ic.readSavable("length", null);
    }
}
