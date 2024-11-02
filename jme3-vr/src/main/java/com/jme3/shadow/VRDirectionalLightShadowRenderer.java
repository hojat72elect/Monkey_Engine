
package com.jme3.shadow;

import com.jme3.asset.AssetManager;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * DirectionalLightShadowRenderer renderer use Parallel Split Shadow Mapping
 * technique (pssm)<br> It splits the view frustum in several parts and compute
 * a shadow map for each one.<br> splits are distributed so that the closer they
 * are from the camera, the smaller they are to maximize the resolution used of
 * the shadow map.<br> This results in a better quality shadow than standard
 * shadow mapping.<br> for more information on this read <a
 * href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a><br>
 *
 * @author Rémy Bouquet aka Nehon
 * @deprecated The jme3-vr module is deprecated and will be removed in a future version (as it only supports OpenVR).
 *             For new Virtual Reality projects, use user libraries that provide OpenXR support.
 *             See <a href = "https://wiki.jmonkeyengine.org/docs/3.4/core/vr/virtualreality.html">Virtual Reality JME wiki section</a>
 *             for more information.
 */
@Deprecated
public class VRDirectionalLightShadowRenderer extends DirectionalLightShadowRenderer {

    /**
     * Create an OculusDirectionalLightShadowRenderer More info on the technique at <a
     * href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a>
     *
     * @param assetManager the application asset manager
     * @param shadowMapSize the size of the rendered shadowmaps (512,1024,2048,
     * etc...)
     * @param nbSplits the number of shadow maps rendered (More shadow maps
     * result in higher quality, fewer fps.)
     */
    public VRDirectionalLightShadowRenderer(AssetManager assetManager, int shadowMapSize, int nbSplits) {
        super(assetManager, shadowMapSize, nbSplits);
    }
    
    @Override
    public VRDirectionalLightShadowRenderer clone() {
        VRDirectionalLightShadowRenderer clone = new VRDirectionalLightShadowRenderer(assetManager, (int)shadowMapSize, nbShadowMaps);
        clone.setEdgeFilteringMode(getEdgeFilteringMode());
        clone.setEdgesThickness(getEdgesThickness());
        clone.setEnabledStabilization(isEnabledStabilization());
        clone.setLambda(getLambda());
        clone.setLight(getLight());
        clone.setShadowCompareMode(getShadowCompareMode());
        clone.setShadowIntensity(getShadowIntensity());
        clone.setShadowZExtend(getShadowZExtend());
        clone.setShadowZFadeLength(getShadowZFadeLength());
        return clone;
    }
}
