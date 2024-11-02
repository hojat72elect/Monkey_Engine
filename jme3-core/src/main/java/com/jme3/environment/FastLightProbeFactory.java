
package com.jme3.environment;

import com.jme3.asset.AssetManager;
import com.jme3.environment.baker.IBLGLEnvBakerLight;
import com.jme3.environment.util.EnvMapUtils;
import com.jme3.light.LightProbe;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image.Format;

/**
 * A faster LightProbeFactory that uses GPU accelerated algorithms.
 * This is the GPU version of @{link LightProbeFactory} and should be generally preferred.
 * 
 * For common use cases where the probe is baking the scene or part of the scene around it, it
 * is advised to use the @{link EnvironmentProbeControl} instead since it does automatically most of the 
 * boilerplate work.
 * 
 * 
 * @author Riccardo Balbo
 */
public class FastLightProbeFactory {

    /**
     * Creates a LightProbe with the given EnvironmentCamera in the given scene.
     * 
     * @param rm
     *            The RenderManager
     * @param am
     *            The AssetManager
     * @param size
     *            The size of the probe
     * @param pos
     *            The position of the probe
     * @param frustumNear
     *            The near frustum of the probe
     * @param frustumFar
     *            The far frustum of the probe
     * @param scene
     *            The scene to bake
     * @return The baked LightProbe
     */
    public static LightProbe makeProbe(RenderManager rm, AssetManager am, int size, Vector3f pos, float frustumNear, float frustumFar, Spatial scene) {
        IBLGLEnvBakerLight baker = new IBLGLEnvBakerLight(rm, am, Format.RGB16F, Format.Depth, size, size);

        baker.setTexturePulling(true);
        baker.bakeEnvironment(scene, pos, frustumNear, frustumFar, null);
        baker.bakeSpecularIBL();
        baker.bakeSphericalHarmonicsCoefficients();

        LightProbe probe = new LightProbe();

        probe.setPosition(pos);
        probe.setPrefilteredMap(baker.getSpecularIBL());

        int[] mipSizes = probe.getPrefilteredEnvMap().getImage().getMipMapSizes();
        probe.setNbMipMaps(mipSizes != null ? mipSizes.length : 1);

        probe.setShCoeffs(baker.getSphericalHarmonicsCoefficients());
        probe.setReady(true);

        baker.clean();

        return probe;

    }

    /**
     * For debuging purposes only Will return a Node meant to be added to a GUI
     * presenting the 2 cube maps in a cross pattern with all the mip maps.
     *
     * @param manager
     *            the asset manager
     * @return a debug node
     */
    public static Node getDebugGui(AssetManager manager, LightProbe probe) {
        if (!probe.isReady()) {
            throw new UnsupportedOperationException("This EnvProbe is not ready yet, try to test isReady()");
        }

        Node debugNode = new Node("debug gui probe");
        Node debugPfemCm = EnvMapUtils.getCubeMapCrossDebugViewWithMipMaps(probe.getPrefilteredEnvMap(), manager);
        debugNode.attachChild(debugPfemCm);
        debugPfemCm.setLocalTranslation(520, 0, 0);

        return debugNode;
    }

}
