
package com.jme3.light;

import com.jme3.scene.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * This strategy returns the 3 closest probe from the rendered object.
 * <p>
 * Image based lighting will be blended between those probes in the shader according to their distance and range.
 *
 * @author Nehon
 */
public class WeightedProbeBlendingStrategy implements LightProbeBlendingStrategy {

    private static final int MAX_PROBES = 3;
    List<LightProbe> lightProbes = new ArrayList<>();

    @Override
    public void registerProbe(LightProbe probe) {
        lightProbes.add(probe);
    }

    @Override
    public void populateProbes(Geometry g, LightList lightList) {
        if (!lightProbes.isEmpty()) {
            //The 3 first probes are the closest to the geometry since the
            //light list is sorted according to the distance to the geom.
            int addedProbes = 0;
            for (LightProbe p : lightProbes) {
                if (p.isReady() && p.isEnabled()) {
                    lightList.add(p);
                    addedProbes ++;
                }
                if (addedProbes == MAX_PROBES) {
                    break;
                }
            }

            //clearing the list for next pass.
            lightProbes.clear();
        }
    }

}
