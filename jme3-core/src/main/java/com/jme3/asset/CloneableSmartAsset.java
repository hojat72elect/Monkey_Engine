
package com.jme3.asset;

import com.jme3.asset.cache.WeakRefCloneAssetCache;

/**
 * Implementing the <code>CloneableSmartAsset</code> interface allows use 
 * of cloneable smart asset management.
 * <p>
 * Smart asset management requires cooperation from the {@link AssetKey}. 
 * In particular, the AssetKey should return {@link WeakRefCloneAssetCache} in its 
 * {@link AssetKey#getCacheType()} method. Also smart assets MUST
 * create a clone of the asset and cannot return the same reference,
 * e.g. {@link AssetProcessor#createClone(java.lang.Object) createClone(someAsset)} <code>!= someAsset</code>.
 * <p>
 * If the {@link AssetManager#loadAsset(com.jme3.asset.AssetKey) } method
 * is called twice with the same asset key (equals() wise, not necessarily reference wise)
 * then both assets will have the same asset key set (reference wise) via
 * {@link AssetKey#AssetKey() }, then this asset key
 * is used to track all instances of that asset. Once all clones of the asset 
 * are garbage collected, the shared asset key becomes unreachable and at that 
 * point it is removed from the smart asset cache. 
 */
public interface CloneableSmartAsset extends Cloneable {
    
    /**
     * Creates a clone of the asset. 
     * 
     * Please see {@link Object#clone() } for more info on how this method
     * should be implemented. 
     * 
     * @return A clone of this asset. 
     * The cloned asset cannot reference equal this asset.
     */
    public CloneableSmartAsset clone();
    
    /**
     * Assigns the specified AssetKey to the asset.
     * 
     * This is invoked by the {@link AssetManager}.
     * Only clones of the asset have non-null keys. The original copy that
     * was loaded has no key assigned. Only the clones are tracked
     * for garbage collection.
     * 
     * @param key The AssetKey to assign
     */
    public void setKey(AssetKey key);
    
    /**
     * Returns the asset key that is used to track this asset for garbage
     * collection.
     * 
     * @return the asset key that is used to track this asset for garbage
     * collection.
     */
    public AssetKey getKey();
}
