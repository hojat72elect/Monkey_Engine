
package com.jme3.scene.plugins;

public class IrMesh {
    
    public IrPolygon[] polygons;
    
    public IrMesh deepClone() {
        IrMesh m = new IrMesh();
        m.polygons = new IrPolygon[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            m.polygons[i] = polygons[i].deepClone();
        }
        return m;
    }
}
