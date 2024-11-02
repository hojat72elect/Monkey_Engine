
package com.jme3.scene.plugins.fbx.obj;

import com.jme3.asset.AssetManager;

public class FbxUnknownObject extends FbxObject<Void> {

    public FbxUnknownObject(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    protected Void toJmeObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectObject(FbxObject object) {
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
    }
}
