
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A filter to render a fog effect
 * @author Rémy Bouquet aka Nehon
 */
public class FogFilter extends Filter {

    private ColorRGBA fogColor = ColorRGBA.White.clone();
    private float fogDensity = 0.7f;
    private float fogDistance = 1000;

    /**
     * Creates a FogFilter
     */
    public FogFilter() {
        super("FogFilter");
    }

    /**
     * Create a fog filter 
     * @param fogColor the color of the fog (default is white)
     * @param fogDensity the density of the fog (default is 0.7)
     * @param fogDistance the distance of the fog (default is 1000)
     */
    public FogFilter(ColorRGBA fogColor, float fogDensity, float fogDistance) {
        this();
        this.fogColor = fogColor;
        this.fogDensity = fogDensity;
        this.fogDistance = fogDistance;
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Fog.j3md");
        material.setColor("FogColor", fogColor);
        material.setFloat("FogDensity", fogDensity);
        material.setFloat("FogDistance", fogDistance);
    }

    @Override
    protected Material getMaterial() {

        return material;
    }


    /**
     * returns the fog color
     * @return the pre-existing instance
     */
    public ColorRGBA getFogColor() {
        return fogColor;
    }

    /**
     * Sets the color of the fog
     *
     * @param fogColor the desired color (alias created, default=(1,1,1,1))
     */
    public void setFogColor(ColorRGBA fogColor) {
        if (material != null) {
            material.setColor("FogColor", fogColor);
        }
        this.fogColor = fogColor;
    }

    /**
     * returns the fog density
     * @return the density value
     */
    public float getFogDensity() {
        return fogDensity;
    }

    /**
     * Sets the density of the fog, a high value gives a thick fog
     *
     * @param fogDensity the desired density (default=0.7)
     */
    public void setFogDensity(float fogDensity) {
        if (material != null) {
            material.setFloat("FogDensity", fogDensity);
        }
        this.fogDensity = fogDensity;
    }

    /**
     * returns the fog distance
     * @return the distance
     */
    public float getFogDistance() {
        return fogDistance;
    }

    /**
     * the distance of the fog. the higher the value the distant the fog looks
     *
     * @param fogDistance the desired distance (in world units, default=1000)
     */
    public void setFogDistance(float fogDistance) {
        if (material != null) {
            material.setFloat("FogDistance", fogDistance);
        }
        this.fogDistance = fogDistance;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(fogColor, "fogColor", ColorRGBA.White.clone());
        oc.write(fogDensity, "fogDensity", 0.7f);
        oc.write(fogDistance, "fogDistance", 1000);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        fogColor = (ColorRGBA) ic.readSavable("fogColor", ColorRGBA.White.clone());
        fogDensity = ic.readFloat("fogDensity", 0.7f);
        fogDistance = ic.readFloat("fogDistance", 1000);
    }


}
