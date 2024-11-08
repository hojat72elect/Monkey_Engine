
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
import com.jme3.shader.VarType;
import java.io.IOException;

/**
 * Radially blurs the scene from the center of it
 * @author Rémy Bouquet aka Nehon
 */
public class RadialBlurFilter extends Filter {

    private float sampleDist = 1.0f;
    private float sampleStrength = 2.2f;
    private float[] samples = {-0.08f, -0.05f, -0.03f, -0.02f, -0.01f, 0.01f, 0.02f, 0.03f, 0.05f, 0.08f};

    /**
     * Creates a RadialBlurFilter
     */
    public RadialBlurFilter() {
        super("Radial blur");
    }

    /**
     * Creates a RadialBlurFilter
     * @param sampleDist the distance between samples
     * @param sampleStrength the strength of each sample
     */
    public RadialBlurFilter(float sampleDist, float sampleStrength) {
        this();
        this.sampleDist = sampleDist;
        this.sampleStrength = sampleStrength;
    }

    @Override
    protected Material getMaterial() {
        material.setFloat("SampleDist", sampleDist);
        material.setFloat("SampleStrength", sampleStrength);
        material.setParam("Samples", VarType.FloatArray, samples);
        return material;
    }

    /**
     * return the sample distance
     * @return the distance
     */
    public float getSampleDistance() {
        return sampleDist;
    }

    /**
     * sets the samples distances default is 1
     *
     * @param sampleDist the desired distance (default=1)
     */
    public void setSampleDistance(float sampleDist) {
        this.sampleDist = sampleDist;
    }

    /**
     * 
     * @return the distance
     * @deprecated use {@link #getSampleDistance()}
     */
    @Deprecated
    public float getSampleDist() {
        return sampleDist;
    }

    /**
     * 
     * @param sampleDist the desired distance (default=1)
     * @deprecated use {@link #setSampleDistance(float sampleDist)}
     */
    @Deprecated
    public void setSampleDist(float sampleDist) {
        this.sampleDist = sampleDist;
    }

    /**
     * Returns the sample Strength
     * @return the strength value
     */
    public float getSampleStrength() {
        return sampleStrength;
    }

    /**
     * sets the sample strength default is 2.2
     *
     * @param sampleStrength the desired strength (default=2.2)
     */
    public void setSampleStrength(float sampleStrength) {
        this.sampleStrength = sampleStrength;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Blur/RadialBlur.j3md");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(sampleDist, "sampleDist", 1.0f);
        oc.write(sampleStrength, "sampleStrength", 2.2f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        sampleDist = ic.readFloat("sampleDist", 1.0f);
        sampleStrength = ic.readFloat("sampleStrength", 2.2f);
    }
}
