
package com.jme3.asset.cache;

import com.jme3.asset.AssetKey;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <code>SimpleAssetCache</code> is an asset cache
 * that caches assets without any automatic removal policy. The user
 * is expected to manually call {@link #deleteFromCache(com.jme3.asset.AssetKey) }
 * to delete any assets.
 * 
 * @author Kirill Vainer
 */
public class SimpleAssetCache implements AssetCache {

    private final ConcurrentHashMap<AssetKey, Object> keyToAssetMap = new ConcurrentHashMap<>();
    
    @Override
    public <T> void addToCache(AssetKey<T> key, T obj) {
        keyToAssetMap.put(key, obj);
    }

    @Override
    public <T> void registerAssetClone(AssetKey<T> key, T clone) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFromCache(AssetKey<T> key) {
        return (T) keyToAssetMap.get(key);
    }

    @Override
    public boolean deleteFromCache(AssetKey key) {
        return keyToAssetMap.remove(key) != null;
    }

    @Override
    public void clearCache() {
        keyToAssetMap.clear();
    }

    @Override
    public void notifyNoAssetClone() {
    }
    
}
