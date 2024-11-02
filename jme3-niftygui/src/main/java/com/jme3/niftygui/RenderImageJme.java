
package com.jme3.niftygui;

import com.jme3.asset.TextureKey;
import com.jme3.texture.Image;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;
import de.lessvoid.nifty.spi.render.RenderImage;

public class RenderImageJme implements RenderImage {

    private Texture2D texture;
    private Image image;
    private int width;
    private int height;

    public RenderImageJme(String filename, boolean linear, NiftyJmeDisplay display) {
        TextureKey key = new TextureKey(filename, true);

        key.setAnisotropy(0);
        key.setGenerateMips(false);

        texture = (Texture2D) display.getAssetManager().loadTexture(key);
        texture.setMagFilter(linear ? MagFilter.Bilinear : MagFilter.Nearest);
        texture.setMinFilter(linear ? MinFilter.BilinearNoMipMaps : MinFilter.NearestNoMipMaps);
        image = texture.getImage();

        width = image.getWidth();
        height = image.getHeight();
    }

    public RenderImageJme(Texture2D texture) {
        if (texture.getImage() == null) {
            throw new IllegalArgumentException("texture.getImage() cannot be null");
        }

        this.texture = texture;
        this.image = texture.getImage();
        width = image.getWidth();
        height = image.getHeight();
    }

    public Texture2D getTexture() {
        return texture;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void dispose() {
    }
}
