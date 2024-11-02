

package com.jme3.light;

import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
/**
 * NullLightFilter does nothing. Used when you want
 * to disable the light filter
 *
 * @author Michael Zuegg
 */
public class NullLightFilter implements LightFilter {
    @Override
    public void setCamera(Camera camera) {

    }

    @Override
    public void filterLights(Geometry geometry, LightList filteredLightList) {

    }
}
