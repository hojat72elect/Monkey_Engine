
package com.jme3.collision;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 * A <code>CollisionResult</code> represents a single collision instance
 * between two {@link Collidable}. A collision check can result in many
 * collision instances (places where collision has occurred).
 *
 * @author Kirill Vainer
 */
public class CollisionResult implements Comparable<CollisionResult> {

    private Geometry geometry;
    private Vector3f contactPoint;
    private Vector3f contactNormal;
    private float distance;
    private int triangleIndex;

    public CollisionResult(Geometry geometry, Vector3f contactPoint, float distance, int triangleIndex) {
        this.geometry = geometry;
        this.contactPoint = contactPoint;
        this.distance = distance;
        this.triangleIndex = triangleIndex;
    }

    public CollisionResult(Vector3f contactPoint, float distance) {
        this.contactPoint = contactPoint;
        this.distance = distance;
    }

    public CollisionResult() {
    }

    public void setGeometry(Geometry geom) {
        this.geometry = geom;
    }

    public void setContactNormal(Vector3f norm) {
        this.contactNormal = norm;
    }

    public void setContactPoint(Vector3f point) {
        this.contactPoint = point;
    }

    public void setDistance(float dist) {
        this.distance = dist;
    }

    public void setTriangleIndex(int index) {
        this.triangleIndex = index;
    }

    public Triangle getTriangle(Triangle store) {
        if (store == null)
            store = new Triangle();

        Mesh m = geometry.getMesh();
        m.getTriangle(triangleIndex, store);
        store.calculateCenter();
        store.calculateNormal();
        return store;
    }

    @Override
    public int compareTo(CollisionResult other) {
        return Float.compare(distance, other.distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CollisionResult) {
            return ((CollisionResult) obj).compareTo(this) == 0;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(distance);
    }

    public Vector3f getContactPoint() {
        return contactPoint;
    }

    public Vector3f getContactNormal() {
        return contactNormal;
    }

    public float getDistance() {
        return distance;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public int getTriangleIndex() {
        return triangleIndex;
    }

    @Override
    public String toString() {
        return "CollisionResult[geometry=" + geometry
                                + ", contactPoint=" + contactPoint
                                + ", contactNormal=" + contactNormal
                                + ", distance=" + distance
                                + ", triangleIndex=" + triangleIndex
                                + "]";
    }
}
