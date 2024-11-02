
package com.jme3.math;

import com.jme3.export.*;
import java.io.IOException;

/**
 * <code>Ring</code> defines a flat ring or disc in three-dimensional
 * space that is specified via the ring's center point, an up vector, an inner
 * radius, and an outer radius.
 *
 * @author Andrzej Kapolka
 * @author Joshua Slack
 */
public final class Ring implements Savable, Cloneable, java.io.Serializable {
    static final long serialVersionUID = 1;

    private Vector3f center, up;
    private float innerRadius, outerRadius;
    private static transient Vector3f b1 = new Vector3f(), b2 = new Vector3f();

    /**
     * Constructor creates a new <code>Ring</code> lying on the XZ plane,
     * centered at the origin, with an inner radius of zero and an outer radius
     * of one (a unit disk).
     */
    public Ring() {
        center = new Vector3f();
        up = Vector3f.UNIT_Y.clone();
        innerRadius = 0f;
        outerRadius = 1f;
    }

    /**
     * Constructor creates a new <code>Ring</code> with defined center point,
     * up vector, and inner and outer radii.
     *
     * @param center
     *            the center of the ring.
     * @param up
     *            the unit up vector defining the ring's orientation.
     * @param innerRadius
     *            the ring's inner radius.
     * @param outerRadius
     *            the ring's outer radius.
     */
    public Ring(Vector3f center, Vector3f up, float innerRadius,
            float outerRadius) {
        this.center = center;
        this.up = up;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

    /**
     * <code>getCenter</code> returns the center of the ring.
     *
     * @return the center of the ring.
     */
    public Vector3f getCenter() {
        return center;
    }

    /**
     * <code>setCenter</code> sets the center of the ring.
     *
     * @param center
     *            the center of the ring.
     */
    public void setCenter(Vector3f center) {
        this.center = center;
    }

    /**
     * <code>getUp</code> returns the ring's up vector.
     *
     * @return the ring's up vector.
     */
    public Vector3f getUp() {
        return up;
    }

    /**
     * <code>setUp</code> sets the ring's up vector.
     *
     * @param up
     *            the ring's up vector.
     */
    public void setUp(Vector3f up) {
        this.up = up;
    }

    /**
     * <code>getInnerRadius</code> returns the ring's inner radius.
     *
     * @return the ring's inner radius.
     */
    public float getInnerRadius() {
        return innerRadius;
    }

    /**
     * <code>setInnerRadius</code> sets the ring's inner radius.
     *
     * @param innerRadius
     *            the ring's inner radius.
     */
    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
    }

    /**
     * <code>getOuterRadius</code> returns the ring's outer radius.
     *
     * @return the ring's outer radius.
     */
    public float getOuterRadius() {
        return outerRadius;
    }

    /**
     * <code>setOuterRadius</code> sets the ring's outer radius.
     *
     * @param outerRadius
     *            the ring's outer radius.
     */
    public void setOuterRadius(float outerRadius) {
        this.outerRadius = outerRadius;
    }

    /**
     * <code>random</code> returns a random point within the ring.
     *
     * @return a random point within the ring.
     */
    public Vector3f random() {
        return random(null);
    }

    /**
     * <code>random</code> returns a random point within the ring.
     *
     * @param result Vector to store result in
     * @return a random point within the ring.
     */
    public Vector3f random(Vector3f result) {
        if (result == null) {
            result = new Vector3f();
        }

        // compute a random radius according to the ring area distribution
        float inner2 = innerRadius * innerRadius,
              outer2 = outerRadius * outerRadius,
              r = FastMath.sqrt(inner2 + FastMath.nextRandomFloat() * (outer2 - inner2)),
              theta = FastMath.nextRandomFloat() * FastMath.TWO_PI;
        up.cross(Vector3f.UNIT_X, b1);
        if (b1.lengthSquared() < FastMath.FLT_EPSILON) {
            up.cross(Vector3f.UNIT_Y, b1);
        }
        b1.normalizeLocal();
        up.cross(b1, b2);
        result.set(b1).multLocal(r * FastMath.cos(theta)).addLocal(center);
        result.scaleAdd(r * FastMath.sin(theta), b2, result);
        return result;
    }

    /**
     * Serialize this ring to the specified exporter, for example when
     * saving to a J3O file.
     *
     * @param e (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(center, "center", Vector3f.ZERO);
        capsule.write(up, "up", Vector3f.UNIT_Z);
        capsule.write(innerRadius, "innerRadius", 0f);
        capsule.write(outerRadius, "outerRadius", 1f);
    }

    /**
     * De-serialize this ring from the specified importer, for example
     * when loading from a J3O file.
     *
     * @param importer (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter importer) throws IOException {
        InputCapsule capsule = importer.getCapsule(this);
        center = (Vector3f) capsule.readSavable("center", Vector3f.ZERO.clone());
        up = (Vector3f) capsule.readSavable("up", Vector3f.UNIT_Z.clone());
        innerRadius = capsule.readFloat("innerRadius", 0f);
        outerRadius = capsule.readFloat("outerRadius", 1f);
    }

    /**
     * Create a copy of this ring.
     *
     * @return a new instance, equivalent to this one
     */
    @Override
    public Ring clone() {
        try {
            Ring r = (Ring) super.clone();
            r.center = center.clone();
            r.up = up.clone();
            return r;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
