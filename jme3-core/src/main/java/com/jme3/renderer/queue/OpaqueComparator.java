
package com.jme3.renderer.queue;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class OpaqueComparator implements GeometryComparator {

    private Camera cam;
    private final Vector3f tempVec  = new Vector3f();
    private final Vector3f tempVec2 = new Vector3f();

    @Override
    public void setCamera(Camera cam) {
        this.cam = cam;
    }

    public float distanceToCam(Geometry spat) {
        if (spat == null)
            return Float.NEGATIVE_INFINITY;

        if (spat.queueDistance != Float.NEGATIVE_INFINITY)
                return spat.queueDistance;

        Vector3f camPosition = cam.getLocation();
        Vector3f viewVector = cam.getDirection(tempVec2);
        Vector3f spatPosition = null;

        if (spat.getWorldBound() != null){
            spatPosition = spat.getWorldBound().getCenter();
        } else {
            spatPosition = spat.getWorldTranslation();
        }

        spatPosition.subtract(camPosition, tempVec);
        spat.queueDistance = tempVec.dot(viewVector);

        return spat.queueDistance;
    }

    @Override
    public int compare(Geometry o1, Geometry o2) {
        Material m1 = o1.getMaterial();
        Material m2 = o2.getMaterial();

        int compareResult = Integer.compare(m1.getSortId(), m2.getSortId());
        if (compareResult == 0){
            // use the same shader.
            // sort front-to-back then.
            float d1 = distanceToCam(o1);
            float d2 = distanceToCam(o2);

            if (d1 == d2)
                return 0;
            else if (d1 < d2)
                return -1;
            else
                return 1;
        } else {
            return compareResult;
        }
    }
}
