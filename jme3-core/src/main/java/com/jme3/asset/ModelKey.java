
package com.jme3.asset;

import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefCloneAssetCache;
import com.jme3.scene.Spatial;

/**
 * Used to load model files, such as OBJ or Blender models.
 * This uses cloneable smart asset management, so that when all clones of
 * this model become unreachable, the original asset is purged from the cache,
 * allowing textures, materials, shaders, etc referenced by the model to
 * become collected.
 *
 * @author Kirill Vainer
 */
public class ModelKey extends AssetKey<Spatial> {

    public ModelKey(String name) {
        super(name);
    }

    public ModelKey() {
        super();
    }

    @Override
    public Class<? extends AssetCache> getCacheType() {
        return WeakRefCloneAssetCache.class;
    }

    @Override
    public Class<? extends AssetProcessor> getProcessorType() {
        return CloneableAssetProcessor.class;
    }
}
