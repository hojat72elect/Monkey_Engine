
package com.jme3.scene.plugins;

public class IrPolygon {
    
    public IrVertex[] vertices;
    
    public IrPolygon deepClone() {
        IrPolygon p = new IrPolygon();
        p.vertices = new IrVertex[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            p.vertices[i] = vertices[i].deepClone();
        }
        return p;
    }
}
