
package com.jme3.scene.plugins.fbx.node;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.file.FbxId;

public class FbxRootNode extends FbxNode {
    public FbxRootNode(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
        this.id = FbxId.ROOT;
        this.className = "Model";
        this.name = "Scene";
        this.subclassName = "";
    }
}
