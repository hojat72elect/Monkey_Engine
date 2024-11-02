
package com.jme3.asset;

import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefCloneAssetCache;
import com.jme3.material.Material;

/**
 * Used for loading {@link Material materials} only (not material definitions!).
 * Material instances use cloneable smart asset management so that they and any
 * referenced textures will be collected when all instances of the material
 * become unreachable.
 *
 * @author Kirill Vainer
 */
public class MaterialKey extends AssetKey<Material> {

    public MaterialKey(String name) {
        super(name);
    }

    public MaterialKey() {
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
