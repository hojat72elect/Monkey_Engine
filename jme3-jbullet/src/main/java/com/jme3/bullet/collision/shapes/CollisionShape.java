
package com.jme3.bullet.collision.shapes;

import com.jme3.bullet.util.Converter;
import com.jme3.export.*;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 * This Object holds information about a jbullet CollisionShape to be able to reuse
 * CollisionShapes (as suggested in bullet manuals)
 * TODO: add static methods to create shapes from nodes (like jbullet-jme constructor)
 * @author normenhansen
 */
public abstract class CollisionShape implements Savable {

    /**
     * default margin for new shapes (in physics-space units, &gt;0,
     * default=0.04)
     */
    private static float defaultMargin = 0.04f;
    protected com.bulletphysics.collision.shapes.CollisionShape cShape;
    protected Vector3f scale = new Vector3f(1, 1, 1);
    /**
     * copy of collision margin (in physics-space units, &gt;0, default=0)
     */
    protected float margin = defaultMargin;

    protected CollisionShape() {
    }

    /**
     * used internally, not safe
     * 
     * @param mass the desired mass for the body
     * @param vector storage for the result (not null, modified)
     */
    public void calculateLocalInertia(float mass, javax.vecmath.Vector3f vector) {
        if (cShape == null) {
            return;
        }
        if (this instanceof MeshCollisionShape) {
            vector.set(0, 0, 0);
        } else {
            cShape.calculateLocalInertia(mass, vector);
        }
    }

    /**
     * used internally
     *
     * @return the pre-existing instance
     */
    public com.bulletphysics.collision.shapes.CollisionShape getCShape() {
        return cShape;
    }

    /**
     * used internally
     *
     * @param cShape the shape to use (alias created)
     */
    public void setCShape(com.bulletphysics.collision.shapes.CollisionShape cShape) {
        this.cShape = cShape;
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
        cShape.setLocalScaling(Converter.convert(scale));
    }

    public float getMargin() {
        return cShape.getMargin();
    }

    /**
     * Alter the default margin for new shapes.
     *
     * @param margin the desired margin distance (in physics-space units, &gt;0,
     * default=0.04)
     */
    public static void setDefaultMargin(float margin) {
        defaultMargin = margin;
    }

    /**
     * Read the default margin for new shapes.
     *
     * @return margin the default margin distance (in physics-space units,
     * &gt;0)
     */
    public static float getDefaultMargin() {
        return defaultMargin;
    }

    public void setMargin(float margin) {
        cShape.setMargin(margin);
        this.margin = margin;
    }

    public Vector3f getScale() {
        return scale;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(scale, "scale", new Vector3f(1, 1, 1));
        capsule.write(getMargin(), "margin", 0.0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        this.scale = (Vector3f) capsule.readSavable("scale", new Vector3f(1, 1, 1));
        this.margin = capsule.readFloat("margin", 0.0f);
    }
}
