
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.ArrayList;
import java.util.List;

public class FbxSkinDeformer extends FbxObject<List<FbxCluster>> {

    private final List<FbxCluster> clusters = new ArrayList<>();
    
    public FbxSkinDeformer(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    protected List<FbxCluster> toJmeObject() {
        return clusters;
    }

    @Override
    public void connectObject(FbxObject object) {
        if (object instanceof FbxCluster) {
            clusters.add((FbxCluster) object);
        } else {
            unsupportedConnectObject(object);
        }
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
    
}
