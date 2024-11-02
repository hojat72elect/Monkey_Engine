
package com.jme3.scene.plugins.fbx.node;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.obj.FbxObject;

public class FbxNullAttribute extends FbxNodeAttribute<Object> {

    public FbxNullAttribute(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }

    @Override
    protected Object toJmeObject() {
        // No data in a "Null" attribute.
        return new Object();
    }

    @Override
    public void connectObject(FbxObject object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        throw new UnsupportedOperationException();
    }
    
}
