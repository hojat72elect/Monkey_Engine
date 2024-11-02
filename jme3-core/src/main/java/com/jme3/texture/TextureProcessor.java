
package com.jme3.texture;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetProcessor;
import com.jme3.asset.TextureKey;
import java.nio.ByteBuffer;

public class TextureProcessor implements AssetProcessor {

    @Override
    public Object postProcess(AssetKey key, Object obj) {
        TextureKey texKey = (TextureKey) key;
        Image img = (Image) obj;
        if (img == null) {
            return null;
        }

        Texture tex;
        if (texKey.getTextureTypeHint() == Texture.Type.CubeMap) {
            if (texKey.isFlipY()) {
                // also flip -y and +y image in cubemap
                ByteBuffer pos_y = img.getData(2);
                img.setData(2, img.getData(3));
                img.setData(3, pos_y);
            }
            tex = new TextureCubeMap();
        } else if (texKey.getTextureTypeHint() == Texture.Type.ThreeDimensional) {
            tex = new Texture3D();
        } else {
            tex = new Texture2D();
        }

        // enable mipmaps if image has them
        // or generate them if requested by user
        if (img.hasMipmaps() || texKey.isGenerateMips()) {
            tex.setMinFilter(Texture.MinFilter.Trilinear);
        }

        tex.setAnisotropicFilter(texKey.getAnisotropy());
        tex.setName(texKey.getName());
        tex.setImage(img);
        return tex;
    }

    @Override
    public Object createClone(Object obj) {
        Texture tex = (Texture) obj;
        return tex.clone();
    }
    
}
