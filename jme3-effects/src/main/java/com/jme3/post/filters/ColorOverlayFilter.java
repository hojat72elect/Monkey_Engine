
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
 * This filter simply multiply the whole scene by a color
 * @author Rémy Bouquet aka Nehon
 */
public class ColorOverlayFilter extends Filter {

    private ColorRGBA color = ColorRGBA.White;

    /**
     * creates a colorOverlayFilter with a white color (transparent)
     */
    public ColorOverlayFilter() {
        super("Color Overlay");
    }

    /**
     * creates a colorOverlayFilter with the given color
     *
     * @param color the desired color (default=(1,1,1,1), alias created)
     */
    public ColorOverlayFilter(ColorRGBA color) {
        this();
        this.color = color;
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * returns the color
     * @return color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * sets the color
     *
     * @param color the desired color (default=(1,1,1,1), alias created)
     */
    public void setColor(final ColorRGBA color) {
        this.color = color;
        if (material != null) {
            material.setColor("Color", color);
        }
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Overlay.j3md");
        material.setColor("Color", color);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(color, "color", ColorRGBA.White);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        color = (ColorRGBA) ic.readSavable("color", ColorRGBA.White);
    }
}
