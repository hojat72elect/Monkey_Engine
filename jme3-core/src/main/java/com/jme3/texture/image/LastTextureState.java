
package com.jme3.texture.image;

import com.jme3.renderer.Renderer;
import com.jme3.texture.Texture;

/**
 * Stores/caches texture-state parameters so the {@link Renderer} doesn't have to
 * set them each time.
 * 
 * @author Kirill Vainer
 */
public final class LastTextureState {
    
    public Texture.WrapMode sWrap, tWrap, rWrap;
    public Texture.MagFilter magFilter;
    public Texture.MinFilter minFilter;
    public int anisoFilter;
    public Texture.ShadowCompareMode shadowCompareMode;
    
    public LastTextureState() {
        reset();
    }
    
    public void reset() {
        sWrap = null;
        tWrap = null;
        rWrap = null;
        magFilter = null;
        minFilter = null;
        anisoFilter = 1;
        
        // The default in OpenGL is OFF, so we avoid setting this per texture
        // if it's not used.
        shadowCompareMode = Texture.ShadowCompareMode.Off;
    }
}
