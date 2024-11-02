
package com.jme3.renderer.queue;

import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import java.util.Comparator;

/**
 * <code>GeometryComparator</code> is a special version of {@link Comparator}
 * that is used to sort geometries for rendering in the {@link RenderQueue}.
 * 
 * @author Kirill Vainer
 */
public interface GeometryComparator extends Comparator<Geometry> {
    
    /**
     * Set the camera to use for sorting.
     * 
     * @param cam The camera to use for sorting
     */
    public void setCamera(Camera cam);
}
