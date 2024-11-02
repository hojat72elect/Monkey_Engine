
package com.jme3.renderer.queue;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class TransparentComparator implements GeometryComparator {

    private Camera cam;
    private final Vector3f tempVec = new Vector3f();

    @Override
    public void setCamera(Camera cam){
        this.cam = cam;
    }

    private float distanceToCam(Geometry spat){
        // NOTE: It is best to check the distance
        // to the bound's closest edge vs. the bound's center here.
        return spat.getWorldBound().distanceToEdge(cam.getLocation());
    }

    @Override
    public int compare(Geometry o1, Geometry o2) {
        float d1 = distanceToCam(o1);
        float d2 = distanceToCam(o2);

        if (d1 == d2)
            return 0;
        else if (d1 < d2)
            return 1;
        else
            return -1;
    }
}
