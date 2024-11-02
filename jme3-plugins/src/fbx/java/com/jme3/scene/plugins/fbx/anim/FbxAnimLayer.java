
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class FbxAnimLayer extends FbxObject {

    private static final Logger logger = Logger.getLogger(FbxAnimLayer.class.getName());
    
    private final List<FbxAnimCurveNode> animCurves = new ArrayList<>();
    
    public FbxAnimLayer(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        // No known properties for layers.
        // Anyway, jME3 doesn't support multiple layers.
    }
    
    public List<FbxAnimCurveNode> getAnimationCurveNodes() {
        return Collections.unmodifiableList(animCurves);
    }
    
    @Override
    protected Object toJmeObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectObject(FbxObject object) {
        if (!(object instanceof FbxAnimCurveNode)) {
            unsupportedConnectObject(object);
        }
        
        animCurves.add((FbxAnimCurveNode) object);
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
}
    