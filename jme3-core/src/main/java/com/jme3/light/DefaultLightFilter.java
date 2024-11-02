
package com.jme3.light;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.util.TempVars;
import java.util.HashSet;

public final class DefaultLightFilter implements LightFilter {

    private Camera camera;
    private final HashSet<Light> processedLights = new HashSet<>();
    private LightProbeBlendingStrategy probeBlendStrategy;

    public DefaultLightFilter() {
        probeBlendStrategy = new WeightedProbeBlendingStrategy();
    }

    public DefaultLightFilter(LightProbeBlendingStrategy probeBlendStrategy) {
        this.probeBlendStrategy = probeBlendStrategy;
    }

    @Override
    public void setCamera(Camera camera) {
        this.camera = camera;
        for (Light light : processedLights) {
            light.frustumCheckNeeded = true;
        }
        processedLights.clear();
    }

    @Override
    public void filterLights(Geometry geometry, LightList filteredLightList) {
        TempVars vars = TempVars.get();
        try {
            LightList worldLights = geometry.getWorldLightList();

            for (int i = 0; i < worldLights.size(); i++) {
                Light light = worldLights.get(i);

                // If this light is not enabled it will be ignored.
                if (!light.isEnabled()) {
                    continue;
                }

                if (light.frustumCheckNeeded) {
                    processedLights.add(light);
                    light.frustumCheckNeeded = false;
                    light.intersectsFrustum = light.intersectsFrustum(camera, vars);
                }

                if (!light.intersectsFrustum) {
                    continue;
                }

                BoundingVolume bv = geometry.getWorldBound();

                if (bv instanceof BoundingBox) {
                    if (!light.intersectsBox((BoundingBox) bv, vars)) {
                        continue;
                    }
                } else if (bv instanceof BoundingSphere) {
                    if (!Float.isInfinite(((BoundingSphere) bv).getRadius())) {
                        if (!light.intersectsSphere((BoundingSphere) bv, vars)) {
                            continue;
                        }
                    }
                }

                if (light.getType() == Light.Type.Probe) {
                    probeBlendStrategy.registerProbe((LightProbe) light);
                } else {
                    filteredLightList.add(light);
                }

            }

            probeBlendStrategy.populateProbes(geometry, filteredLightList);

        } finally {
            vars.release();
        }
    }

    public void setLightProbeBlendingStrategy(LightProbeBlendingStrategy strategy) {
        probeBlendStrategy = strategy;
    }

}
