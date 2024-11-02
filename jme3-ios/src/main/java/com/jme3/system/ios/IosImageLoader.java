
package com.jme3.system.ios;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author normenhansen
 */
public class IosImageLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo info) throws IOException {
        boolean flip = ((TextureKey) info.getKey()).isFlipY();
        Image img = null;
        InputStream in = null;
        try {
            in = info.openStream();
            img = loadImageData(Format.RGBA8, flip, in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return img;
    }

    /**
     * Loads images via iOS native API
     *
     * @param format has to be Image.Format.RGBA8
     * @param inputStream the InputStream to load the image data from
     * @return the loaded Image
     */
    private static native Image loadImageData(Format format, boolean flipY, InputStream inputStream);
}
