
package com.jme3.scene.plugins.fbx.mesh;

import java.util.Arrays;
import java.util.List;

public final class FbxPolygon {

    int[] indices;

    @Override
    public String toString() {
        return Arrays.toString(indices);
    }
    
    private static int[] listToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }
    
    public static FbxPolygon fromIndices(List<Integer> indices) {
        FbxPolygon poly = new FbxPolygon();
        poly.indices = listToArray(indices);
        return poly;
    }
}
