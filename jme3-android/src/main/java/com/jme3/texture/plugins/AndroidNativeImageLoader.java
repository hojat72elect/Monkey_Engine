
package com.jme3.texture.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Image;
import java.io.IOException;
import java.io.InputStream;

/**
 * Native image loader to deal with filetypes that support alpha channels.
 *
 * @author iwgeric
 * @author Kirill Vainer
 */
public class AndroidNativeImageLoader  implements AssetLoader {
    
    private final byte[] tmpArray = new byte[10 * 1024];
    
    static {
         System.loadLibrary("decodejme");
    }
    
    private static native Image load(InputStream in, boolean flipY, byte[] tmpArray) throws IOException;
    
    @Override
    public Image load(AssetInfo info) throws IOException {
        boolean flip = ((TextureKey) info.getKey()).isFlipY();
        try (final InputStream in = info.openStream()) {
            return load(in, flip, tmpArray);
        }
    }
}
