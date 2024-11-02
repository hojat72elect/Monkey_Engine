
package com.jme3.shadow;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.DirectionalLight;
import java.io.IOException;

/**
 * This Filter does basically the same as a DirectionalLightShadowRenderer
 * except it renders the post shadow pass as a fullscreen quad pass instead of a
 * geometry pass. It's mostly faster than PssmShadowRenderer as long as you have
 * more than about ten shadow receiving objects. The expense is the drawback
 * that the shadow Receive mode set on spatial is ignored. So basically all and
 * only objects that render depth in the scene receive shadows.
 *
 * API is basically the same as the PssmShadowRenderer.
 *
 * @author Rémy Bouquet aka Nehon
 */
public class DirectionalLightShadowFilter extends AbstractShadowFilter<DirectionalLightShadowRenderer> {

    /**
     * For serialization only. Do not use.
     * 
     * @see #DirectionalLightShadowFilter(AssetManager assetManager, int shadowMapSize, int nbSplits)
     */
    public DirectionalLightShadowFilter() {
        super();
    }
    
    /**
     * Creates a DirectionalLightShadowFilter.
     * 
     * @param assetManager  the application's asset manager
     * @param shadowMapSize the size of the rendered shadow maps (512, 1024, 2048, etc...)
     * @param nbSplits      the number of shadow maps rendered (more shadow maps = better quality, but slower)
     * 
     * @throws IllegalArgumentException if the provided 'nbSplits' is not within the valid range of 1 to 4.
     */
    public DirectionalLightShadowFilter(AssetManager assetManager, int shadowMapSize, int nbSplits) {
        super(assetManager, shadowMapSize, new DirectionalLightShadowRenderer(assetManager, shadowMapSize, nbSplits));
    }

    /**
     * Returns the light used to cast shadows.
     *
     * @return the DirectionalLight
     */
    public DirectionalLight getLight() {
        return shadowRenderer.getLight();
    }

    /**
     * Sets the light to use to cast shadows.
     *
     * @param light a DirectionalLight
     */
    public void setLight(DirectionalLight light) {
        shadowRenderer.setLight(light);
    }

    /**
     * Returns the lambda parameter.
     *
     * @see #setLambda(float lambda)
     * @return lambda
     */
    public float getLambda() {
        return shadowRenderer.getLambda();
    }

    /**
     * Adjusts the partition of the shadow extend into shadow maps. Lambda is
     * usually between 0 and 1.
     * <p>
     * A low value gives a more linear partition, resulting in consistent shadow
     * quality over the extend, but near shadows could look very jagged. A high
     * value gives a more logarithmic partition, resulting in high quality for near
     * shadows, but quality decreases rapidly with distance.
     * <p>
     * The default value is 0.65 (the theoretical optimum).
     *
     * @param lambda the lambda value
     */
    public void setLambda(float lambda) {
        shadowRenderer.setLambda(lambda);
    }

    /**
     * Returns true if stabilization is enabled.
     * 
     * @return true if stabilization is enabled
     */
    public boolean isEnabledStabilization() {
        return shadowRenderer.isEnabledStabilization();
    }
    
    /**
     * Enables the stabilization of the shadow's edges. (default is true)
     * This prevents shadow edges from flickering when the camera moves.
     * However, it can lead to some loss of shadow quality in particular scenes.
     *
     * @param stabilize true to stabilize, false to disable stabilization
     */
    public void setEnabledStabilization(boolean stabilize) {
        shadowRenderer.setEnabledStabilization(stabilize);        
    }    

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(shadowRenderer, "shadowRenderer", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        shadowRenderer = (DirectionalLightShadowRenderer) ic.readSavable("shadowRenderer", null);
    }
    
}
