
package com.jme3.collision.bih;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public final class BIHTriangle {

    private final Vector3f pointA = new Vector3f();
    private final Vector3f pointB = new Vector3f();
    private final Vector3f pointC = new Vector3f();
    private final Vector3f center = new Vector3f();

    public BIHTriangle(Vector3f p1, Vector3f p2, Vector3f p3) {
        pointA.set(p1);
        pointB.set(p2);
        pointC.set(p3);
        center.set(pointA);
        center.addLocal(pointB).addLocal(pointC).multLocal(FastMath.ONE_THIRD);
    }

    public Vector3f get1() {
        return pointA;
    }

    public Vector3f get2() {
        return pointB;
    }

    public Vector3f get3() {
        return pointC;
    }

    public Vector3f getCenter() {
        return center;
    }

    public Vector3f getNormal() {
        Vector3f normal = new Vector3f(pointB);
        normal.subtractLocal(pointA)
                .crossLocal(pointC.x - pointA.x, pointC.y - pointA.y, pointC.z - pointA.z);
        normal.normalizeLocal();
        return normal;
    }

    public float getExtreme(int axis, boolean left) {
        float v1, v2, v3;
        switch (axis) {
            case 0:
                v1 = pointA.x;
                v2 = pointB.x;
                v3 = pointC.x;
                break;
            case 1:
                v1 = pointA.y;
                v2 = pointB.y;
                v3 = pointC.y;
                break;
            case 2:
                v1 = pointA.z;
                v2 = pointB.z;
                v3 = pointC.z;
                break;
            default:
                assert false;
                return 0;
        }
        if (left) {
            if (v1 < v2) {
                if (v1 < v3)
                    return v1;
                else
                    return v3;
            } else {
                if (v2 < v3)
                    return v2;
                else
                    return v3;
            }
        } else {
            if (v1 > v2) {
                if (v1 > v3)
                    return v1;
                else
                    return v3;
            } else {
                if (v2 > v3)
                    return v2;
                else
                    return v3;
            }
        }
    }
}
