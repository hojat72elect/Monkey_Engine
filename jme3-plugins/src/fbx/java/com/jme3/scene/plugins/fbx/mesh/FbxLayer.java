
package com.jme3.scene.plugins.fbx.mesh;

import com.jme3.scene.plugins.fbx.file.FbxElement;
import java.util.Collection;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FbxLayer {
    
    private static final Logger logger = Logger.getLogger(FbxLayer.class.getName());
    
    public static class FbxLayerElementRef {
        FbxLayerElement.Type layerElementType;
        int layerElementIndex;
        FbxLayerElement layerElement;
    }
    
    int layer;
    final EnumMap<FbxLayerElement.Type, FbxLayerElementRef> references = 
            new EnumMap<>(FbxLayerElement.Type.class);
    
    private FbxLayer() { }
    
    public Object getVertexData(FbxLayerElement.Type type, int polygonIndex, 
                                int polygonVertexIndex, int positionIndex, int edgeIndex) {
        FbxLayerElementRef reference = references.get(type);
        if (reference == null) { 
            return null;
        } else {
            return reference.layerElement.getVertexData(polygonIndex, polygonVertexIndex, positionIndex, edgeIndex);
        }
    }
    
    public FbxLayerElement.Type[] getLayerElementTypes() {
        FbxLayerElement.Type[] types = new FbxLayerElement.Type[references.size()];
        references.keySet().toArray(types);
        return types;
    }
    
    public void setLayerElements(Collection<FbxLayerElement> layerElements) {
        for (FbxLayerElement layerElement : layerElements) {
            FbxLayerElementRef reference = references.get(layerElement.type);
            if (reference != null && reference.layerElementIndex == layerElement.index) {
                reference.layerElement = layerElement;
            }
        }
    }
    
    public static FbxLayer fromElement(FbxElement element) {
        FbxLayer layer = new FbxLayer();
        layer.layer = (Integer)element.properties.get(0);
        next_element: for (FbxElement child : element.children) {
            if (!child.id.equals("LayerElement")) {
                continue;
            }
            FbxLayerElementRef ref = new FbxLayerElementRef();
            for (FbxElement child2 : child.children) {                
                if (child2.id.equals("Type")) {
                    String layerElementTypeStr = (String) child2.properties.get(0);
                    layerElementTypeStr = layerElementTypeStr.substring("LayerElement".length());
                    try {
                        ref.layerElementType = FbxLayerElement.Type.valueOf(layerElementTypeStr);
                    } catch (IllegalArgumentException ex) {
                        logger.log(Level.WARNING, "Unsupported layer type: {0}. Ignoring.", layerElementTypeStr);
                        continue next_element;
                    }
                } else if (child2.id.equals("TypedIndex")) {
                    ref.layerElementIndex = (Integer) child2.properties.get(0);
                }
            }
            layer.references.put(ref.layerElementType, ref);
        }
        return layer;
    }
}
