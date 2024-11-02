
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Phate666
 * @version 1.0 initial version
 * @deprecated use the Gamma Correction setting instead.
 */
@Deprecated
public class GammaCorrectionFilter extends Filter {

    private float gamma = 2.2f;   

    public GammaCorrectionFilter() {
        super("GammaCorrectionFilter");
    }

    public GammaCorrectionFilter(float gamma) {
        this();
        this.setGamma(gamma);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager,
            RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/GammaCorrection.j3md");
        material.setFloat("InvGamma", 1.0f/gamma);        
    }

    public float getGamma() {
        return gamma;
    }

    /**
     * set to 0.0 to disable gamma correction
     *
     * @param gamma the desired exponent (&gt;0, default=2.2)
     */
    public final void setGamma(float gamma) {
        if(gamma<=0){
            throw new IllegalArgumentException("Gamma value can't be below or equal 0.");
        }
        if (material != null) {
            material.setFloat("InvGamma",1.0f/ gamma);
        }
        this.gamma = gamma;
    }
}
