
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A Post Processing filter to change colors appear with sharp edges as if the
 * available amount of colors available was not enough to draw the true image.
 * Possibly useful in cartoon styled games. Use the strength variable to lessen
 * influence of this filter on the total result. Values from 0.2 to 0.7 appear
 * to give nice results.
 *
 * Based on an article from Geeks3D:
 *    <a href="http://www.geeks3d.com/20091027/shader-library-posterization-post-processing-effect-glsl/">http://www.geeks3d.com/20091027/shader-library-posterization-post-processing-effect-glsl/</a>
 *
 * @author Roy Straver a.k.a. Baal Garnaal
 */
public class PosterizationFilter extends Filter {

    private int numColors = 8;
    private float gamma = 0.6f;
    private float strength = 1.0f;

    /**
     * Creates a posterization Filter
     */
    public PosterizationFilter() {
        super("PosterizationFilter");
    }

    /**
     * Creates a posterization Filter with the given number of colors
     *
     * @param numColors the desired number of colors (&gt;0, default=8)
     */
    public PosterizationFilter(int numColors) {
        this();
        this.numColors = numColors;
    }

    /**
     * Creates a posterization Filter with the given number of colors and gamma
     *
     * @param numColors the desired number of colors (&gt;0, default=8)
     * @param gamma the desired exponent (default=0.6)
     */
    public PosterizationFilter(int numColors, float gamma) {
        this(numColors);
        this.gamma = gamma;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Posterization.j3md");
        material.setInt("NumColors", numColors);
        material.setFloat("Gamma", gamma);
        material.setFloat("Strength", strength);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Sets number of color levels used to draw the screen
     * 
     * @param numColors the desired number of colors (&gt;0, default=8)
     */
    public void setNumColors(int numColors) {
        this.numColors = numColors;
        if (material != null) {
            material.setInt("NumColors", numColors);
        }
    }

    /**
     * Sets gamma level used to enhance visual quality
     * 
     * @param gamma the desired exponent (default=0.6)
     */
    public void setGamma(float gamma) {
        this.gamma = gamma;
        if (material != null) {
            material.setFloat("Gamma", gamma);
        }
    }

    /**
     * Sets current strength value, i.e. influence on final image
     *
     * @param strength the desired influence factor (default=1)
     */
    public void setStrength(float strength) {
        this.strength = strength;
        if (material != null) {
            material.setFloat("Strength", strength);
        }
    }

    /**
     * Returns number of color levels used
     *
     * @return the count (&gt;0)
     */
    public int getNumColors() {
        return numColors;
    }

    /**
     * Returns current gamma value
     *
     * @return the exponent
     */
    public float getGamma() {
        return gamma;
    }

    /**
     * Returns current strength value, i.e. influence on final image
     *
     * @return the influence factor
     */
    public float getStrength() {
        return strength;
    }

    /**
     * Load properties when the filter is de-serialized, for example when
     * loading from a J3O file.
     *
     * @param importer the importer to use (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);

        this.gamma = capsule.readFloat("gamma", 0.6f);
        this.numColors = capsule.readInt("numColors", 8);
        this.strength = capsule.readFloat("strength", 1f);
    }

    /**
     * Save properties when the filter is serialized, for example when saving to
     * a J3O file.
     *
     * @param exporter the exporter to use (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);

        capsule.write(gamma, "gamma", 0.6f);
        capsule.write(numColors, "numColors", 8);
        capsule.write(strength, "strength", 1f);
    }
}
