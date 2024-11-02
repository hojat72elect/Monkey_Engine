
package com.jme3.light;

import com.jme3.scene.Geometry;

/**
 * This is the interface to implement if you want to make your own LightProbe blending strategy.
 * The strategy sets the way multiple LightProbes will be handled for a given object.
 *
 * @author Nehon
 */
public interface LightProbeBlendingStrategy {
    
    /**
     * Registers a probe with this strategy
     *
     * @param probe the probe to be registered
     */
    public void registerProbe(LightProbe probe);
    /**
     * Populates the resulting light probes into the given light list.
     * @param g the geometry for which the light list is computed
     * @param lightList the result light list
     */
    public void populateProbes(Geometry g, LightList lightList);
}
