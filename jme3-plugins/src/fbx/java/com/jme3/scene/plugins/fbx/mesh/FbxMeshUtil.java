
package com.jme3.scene.plugins.fbx.mesh;

import com.jme3.scene.plugins.fbx.file.FbxElement;

public class FbxMeshUtil {
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private FbxMeshUtil() {
    }

    public static double[] getDoubleArray(FbxElement el) {
        if (el.propertiesTypes[0] == 'd') {
            // FBX 7.x
            return (double[]) el.properties.get(0);
        } else if (el.propertiesTypes[0] == 'D') {
            // FBX 6.x
            double[] doubles = new double[el.propertiesTypes.length];
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = (Double) el.properties.get(i);
            }
            return doubles;
        } else {
            return null;
        }
    }
    
    public static int[] getIntArray(FbxElement el) {
        if (el.propertiesTypes[0] == 'i') {
            // FBX 7.x
            return (int[]) el.properties.get(0);
        } else if (el.propertiesTypes[0] == 'I') {
            // FBX 6.x
            int[] ints = new int[el.propertiesTypes.length];
            for (int i = 0; i < ints.length; i++) {
                ints[i] = (Integer) el.properties.get(i);
            }
            return ints;
        } else {
            return null;
        }
    }
}
