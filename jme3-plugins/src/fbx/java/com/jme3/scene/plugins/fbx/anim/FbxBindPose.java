
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.math.Matrix4f;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.file.FbxId;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.HashMap;
import java.util.Map;

public class FbxBindPose extends FbxObject<Map<FbxId, Matrix4f>> {

    private final Map<FbxId, Matrix4f> bindPose = new HashMap<>();
    
    public FbxBindPose(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        for (FbxElement child : element.children) {
            if (!child.id.equals("PoseNode")) {
                continue;
            }
            
            FbxId node = null;
            float[] matData = null;
            
            for (FbxElement e : child.children) {
                if (e.id.equals("Node")) {
                    node = FbxId.create(e.properties.get(0));
                } else if (e.id.equals("Matrix")) {
                    matData = new float[16];
                    int numProperties = e.propertiesTypes.length;
                    if (numProperties == 1) {
                        char propertyType = e.propertiesTypes[0];
                        if (propertyType != 'd') {
                            throw new UnsupportedOperationException(
                                    "Bind-pose matrix should have property type 'd',"
                                    + "but found '" + propertyType + "'");
                        }
                        double[] array = (double[]) e.properties.get(0);
                        int length = array.length;
                        if (length != 16) {
                            throw new UnsupportedOperationException(
                                    "Bind-pose matrix should have 16 elements,"
                                    + "but found " + length);
                        }
                        for (int i = 0; i < length; ++i) {
                            matData[i] = (float) array[i];
                        }

                    } else if (numProperties == 16) {
                        for (int i = 0; i < numProperties; ++i) {
                            char propertyType = e.propertiesTypes[i];
                            if (propertyType != 'D') {
                                throw new UnsupportedOperationException(
                                        "Bind-pose matrix should have properties of type 'D',"
                                        + "but found '" + propertyType + "'");
                            }
                            double d = (Double) e.properties.get(i);
                            matData[i] = (float) d;
                        }

                    } else {
                        throw new UnsupportedOperationException(
                                "Bind pose matrix should have either "
                                + "1 or 16 properties, but found "
                                + numProperties);
                    }
                }
            }
            
            if (node != null && matData != null) {
                Matrix4f matrix = new Matrix4f(matData);
                bindPose.put(node, matrix);
            }
        }
    }
    
    @Override
    protected Map<FbxId, Matrix4f> toJmeObject() {
        return bindPose;
    }

    @Override
    public void connectObject(FbxObject object) {
        unsupportedConnectObject(object);
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
    
}
