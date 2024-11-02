
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
import com.jme3.texture.Texture2D;
import java.io.IOException;

/**
 * This filter composes a texture with the viewport texture. This is used to
 * compose post-processed texture from another viewport.
 *
 * the compositing is done using the alpha value of the viewportTexture :
 * mix(compositeTextureColor, viewPortColor, viewportColor.alpha);
 *
 * It's important for a good result that the viewport clear color alpha be 0.
 *
 * @author Rémy Bouquet aka Nehon
 */
public class ComposeFilter extends Filter {

    private Texture2D compositeTexture;

    /**
     * creates a ComposeFilter
     */
    public ComposeFilter() {
        super("Compose Filter");
    }

    /**
     * creates a ComposeFilter with the given texture
     *
     * @param compositeTexture the texture to use (alias created)
     */
    public ComposeFilter(Texture2D compositeTexture) {
        this();
        this.compositeTexture = compositeTexture;
    }

    @Override
    protected Material getMaterial() {

        material.setTexture("CompositeTexture", compositeTexture);
        return material;
    }

    /**
     *
     * @return the compositeTexture
     */
    public Texture2D getCompositeTexture() {
        return compositeTexture;
    }

    /**
     * sets the compositeTexture
     *
     * @param compositeTexture the desired texture (alias created)
     */
    public void setCompositeTexture(Texture2D compositeTexture) {
        this.compositeTexture = compositeTexture;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Compose.j3md");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
    }
}
