
package com.jme3.light;

import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;

/**
 * This strategy returns the closest probe from the rendered object.
 * 
 * This is the most basic strategy : The fastest and the easiest.
 * Though it has severe graphical drawbacks as there might be very visible seams
 * on static object and some "popping" on dynamic objects.
 *
 * @author Nehon
 */
public class BasicProbeBlendingStrategy implements LightProbeBlendingStrategy {

    List<LightProbe> lightProbes = new ArrayList<>();

    @Override
    public void registerProbe(LightProbe probe) {
        lightProbes.add(probe);
    }

    @Override
    public void populateProbes(Geometry g, LightList lightList) {
        if (!lightProbes.isEmpty()) {
            //The first probe is actually the closest to the geometry since the 
            //light list is sorted according to the distance to the geom.
            LightProbe p = lightProbes.get(0);
            if (p.isReady()) {
                lightList.add(p);
            }            
            //clearing the list for next pass.
            lightProbes.clear();
        }        
    }

}
