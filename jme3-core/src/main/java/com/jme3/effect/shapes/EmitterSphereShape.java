
package com.jme3.effect.shapes;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

public class EmitterSphereShape implements EmitterShape {

    private Vector3f center;
    private float radius;

    public EmitterSphereShape() {
    }

    public EmitterSphereShape(Vector3f center, float radius) {
        if (center == null) {
            throw new IllegalArgumentException("center cannot be null");
        }

        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }

        this.center = center;
        this.radius = radius;
    }

    @Override
    public EmitterShape deepClone() {
        try {
            EmitterSphereShape clone = (EmitterSphereShape) super.clone();
            clone.center = center.clone();
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
        this.center = cloner.clone(center);
    }

    @Override
    public void getRandomPoint(Vector3f store) {
        do {
            store.x = (FastMath.nextRandomFloat() * 2f - 1f);
            store.y = (FastMath.nextRandomFloat() * 2f - 1f);
            store.z = (FastMath.nextRandomFloat() * 2f - 1f);
        } while (store.lengthSquared() > 1);
        store.multLocal(radius);
        store.addLocal(center);
    }

    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        this.getRandomPoint(store);
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(center, "center", null);
        oc.write(radius, "radius", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        center = (Vector3f) ic.readSavable("center", null);
        radius = ic.readFloat("radius", 0);
    }
}
