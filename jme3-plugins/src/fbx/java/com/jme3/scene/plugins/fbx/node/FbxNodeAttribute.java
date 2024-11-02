
package com.jme3.scene.plugins.fbx.node;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.obj.FbxObject;

public abstract class FbxNodeAttribute<JT> extends FbxObject<JT> {
    public FbxNodeAttribute(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
}
