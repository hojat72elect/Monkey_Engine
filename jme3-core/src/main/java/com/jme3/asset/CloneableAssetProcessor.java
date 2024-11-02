
package com.jme3.asset;

/**
 * <code>CloneableAssetProcessor</code> simply calls {@link Object#clone() }
 * on assets to clone them. No processing is applied.
 * 
 * @author Kirill Vainer
 */
public class CloneableAssetProcessor implements AssetProcessor {

    @Override
    public Object postProcess(AssetKey key, Object obj) {
        return obj;
    }

    @Override
    public Object createClone(Object obj) {
        CloneableSmartAsset asset = (CloneableSmartAsset) obj;
        return asset.clone();
    }
    
}
