
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FbxCluster extends FbxObject {

    private static final Logger logger = Logger.getLogger(FbxCluster.class.getName());
    
    private int[] indexes;
    private double[] weights;
    private FbxLimbNode limb;
    
    public FbxCluster(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        for (FbxElement e : element.children) {
            if (e.id.equals("Indexes")) {
                int numProperties = e.propertiesTypes.length;
                if (numProperties == 1 && e.propertiesTypes[0] == 'i') {
                    this.indexes = (int[]) e.properties.get(0);

                } else {
                    this.indexes = new int[numProperties];
                    for (int i = 0; i < numProperties; ++i) {
                        char propertyType = e.propertiesTypes[i];
                        if (propertyType != 'I') {
                            throw new UnsupportedOperationException(
                                    "Indexes should have properties of type 'I',"
                                    + "but found '" + propertyType + "'");
                        }
                        int index = (Integer) e.properties.get(i);
                        this.indexes[i] = index;
                    }
                }

            } else if (e.id.equals("Weights")) {
                int numTypes = e.propertiesTypes.length;
                if (numTypes == 1 && e.propertiesTypes[0] == 'd') {
                    this.weights = (double[]) e.properties.get(0);

                } else {
                    int numElements = numTypes;
                    this.weights = new double[numElements];
                    for (int i = 0; i < numElements; ++i) {
                        int propertyType = e.propertiesTypes[i];
                        if (propertyType != 'D') {
                            throw new UnsupportedOperationException(
                                    "Weights should have properties of type 'D',"
                                    + "but found '" + propertyType + "'");
                        }
                        double weight = (Double) e.properties.get(i);
                        this.weights[i] = weight;
                    }
                }
            }
        }

        if (indexes == null && weights == null) {
            // The cluster doesn't contain any keyframes!
            this.indexes = new int[0];
            this.weights = new double[0];
        }
    }

    public int[] getVertexIndices() {
        return indexes;
    }

    public double[] getWeights() {
        return weights;
    }

    public FbxLimbNode getLimb() {
        return limb;
    }
    
    @Override
    protected Object toJmeObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectObject(FbxObject object) {
        if (object instanceof FbxLimbNode) {
            if (limb != null) {
                logger.log(Level.WARNING, "This cluster already has a limb attached. Ignoring.");
                return;
            }
            limb = (FbxLimbNode) object;
        } else {
            unsupportedConnectObject(object);
        }
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
}
