
package com.jme3.scene.plugins.gltf;

import com.jme3.asset.AssetKey;
import com.jme3.asset.cache.AssetCache;

/**
 * Created by Nehon on 09/09/2017.
 */
class BinDataKey extends AssetKey<Object> {
    public BinDataKey(String name) {
        super(name);
    }

    @Override
    public Class<? extends AssetCache> getCacheType() {
        return null;
    }
}
