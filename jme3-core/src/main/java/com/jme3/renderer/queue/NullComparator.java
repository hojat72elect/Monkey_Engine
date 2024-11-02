
package com.jme3.renderer.queue;

import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

/**
 * <code>NullComparator</code> does not sort geometries. They will be in
 * arbitrary order.
 * 
 * @author Kirill Vainer
 */
public class NullComparator implements GeometryComparator {
    @Override
    public int compare(Geometry o1, Geometry o2) {
        return 0;
    }

    @Override
    public void setCamera(Camera cam) {
    }
}
