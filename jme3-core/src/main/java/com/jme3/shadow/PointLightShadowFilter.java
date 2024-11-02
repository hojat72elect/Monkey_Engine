
package com.jme3.shadow;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.PointLight;
import java.io.IOException;

/**
 * This Filter does basically the same as a PointLightShadowRenderer except it
 * renders the post shadow pass as a fullscreen quad pass instead of a geometry
 * pass. It's mostly faster than PointLightShadowRenderer as long as you have
 * more than about ten shadow receiving objects. The expense is the drawback
 * that the shadow Receive mode set on spatial is ignored. So basically all and
 * only objects that render depth in the scene receive shadows.
 *
 * API is basically the same as the PssmShadowRenderer.
 *
 * @author Rémy Bouquet aka Nehon
 */
public class PointLightShadowFilter extends AbstractShadowFilter<PointLightShadowRenderer> {

    /**
     * For serialization only. Do not use.
     * 
     * @see #PointLightShadowFilter(AssetManager assetManager, int shadowMapSize)
     */
    protected PointLightShadowFilter() {
        super();
    }

    /**
     * Creates a PointLightShadowFilter.
     *
     * @param assetManager  the application's asset manager
     * @param shadowMapSize the size of the rendered shadow maps (512, 1024, 2048, etc...)
     */
    public PointLightShadowFilter(AssetManager assetManager, int shadowMapSize) {
        super(assetManager, shadowMapSize, new PointLightShadowRenderer(assetManager, shadowMapSize));
    }

    /**
     * Returns the light used to cast shadows.
     *
     * @return the PointLight
     */
    public PointLight getLight() {
        return shadowRenderer.getLight();
    }

    /**
     * Sets the light to use to cast shadows.
     *
     * @param light the PointLight
     */
    public void setLight(PointLight light) {
        shadowRenderer.setLight(light);
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
        shadowRenderer = (PointLightShadowRenderer) ic.readSavable("shadowRenderer", null);
    }
    
}
