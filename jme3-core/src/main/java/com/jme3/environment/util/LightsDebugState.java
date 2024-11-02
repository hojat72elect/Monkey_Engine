
package com.jme3.environment.util;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A debug state that will display Light gizmos on screen.
 * Still a wip and for now it only displays light probes.
 *
 * @author nehon
 */
public class LightsDebugState extends BaseAppState {

    private Node debugNode;
    private final Map<LightProbe, Node> probeMapping = new HashMap<>();
    private final List<LightProbe> garbage = new ArrayList<>();
    private Geometry debugGeom;
    private Geometry debugBounds;
    private Material debugMaterial;
    private float probeScale = 1.0f;
    private Spatial scene = null;
    private final List<LightProbe> probes = new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        debugNode = new Node("Environment debug Node");
        Sphere s = new Sphere(16, 16, 0.15f);
        debugGeom = new Geometry("debugEnvProbe", s);
        debugMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/reflect.j3md");
        debugGeom.setMaterial(debugMaterial);
        debugBounds = BoundingSphereDebug.createDebugSphere(app.getAssetManager());
        if (scene == null) {
            scene = app.getViewPort().getScenes().get(0);
        }
    }

    @Override
    public void update(float tpf) {
        if (!isEnabled()) {
            return;
        }
        updateLights(scene);
        debugNode.updateLogicalState(tpf);
        debugNode.updateGeometricState();
        cleanProbes();
    }

    public void updateLights(Spatial scene) {
        for (Light light : scene.getWorldLightList()) {
            switch (light.getType()) {

                case Probe:
                    LightProbe probe = (LightProbe) light;
                    probes.add(probe);
                    Node n = probeMapping.get(probe);
                    if (n == null) {
                        n = new Node("DebugProbe");
                        n.attachChild(debugGeom.clone(true));
                        n.attachChild(debugBounds.clone(false));
                        debugNode.attachChild(n);
                        probeMapping.put(probe, n);
                    }
                    Geometry probeGeom = ((Geometry) n.getChild(0));
                    Material m = probeGeom.getMaterial();
                    probeGeom.setLocalScale(probeScale);
                    if (probe.isReady()) {
                        m.setTexture("CubeMap", probe.getPrefilteredEnvMap());
                    }
                    n.setLocalTranslation(probe.getPosition());
                    n.getChild(1).setLocalScale(probe.getArea().getRadius());
                    break;
                default:
                    break;
            }
        }
        if (scene instanceof Node) {
            Node n = (Node)scene;
            for (Spatial spatial : n.getChildren()) {
                updateLights(spatial);
            }
        }
    }

    /**
     * Set the scenes for which to render light gizmos.
     *
     * @param scene the root of the desired scene (alias created)
     */
    public void setScene(Spatial scene) {
        this.scene = scene;
    }

    private void cleanProbes() {
        if (probes.size() != probeMapping.size()) {
            for (LightProbe probe : probeMapping.keySet()) {
                if (!probes.contains(probe)) {
                    garbage.add(probe);
                }
            }
            for (LightProbe probe : garbage) {
                probeMapping.remove(probe);
            }
            garbage.clear();
            probes.clear();
        }
    }

    @Override
    public void render(RenderManager rm) {
        if (!isEnabled()) {
            return;
        }
        rm.renderScene(debugNode, getApplication().getViewPort());
    }

    /**
     * returns the scale of the probe's debug sphere
     * @return the scale factor
     */
    public float getProbeScale() {
        return probeScale;
    }

    /**
     * sets the scale of the probe's debug sphere
     *
     * @param probeScale the scale factor (default=1)
     */
    public void setProbeScale(float probeScale) {
        this.probeScale = probeScale;
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
