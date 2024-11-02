
package com.jme3.scene.plugins.fbx.file;

import java.util.ArrayList;
import java.util.List;

public class FbxElement {

    public String id;
    public List<Object> properties;
    /*
     * Y - signed short
     * C - boolean
     * I - signed integer
     * F - float
     * D - double
     * L - long
     * R - byte array
     * S - string
     * f - array of floats
     * i - array of ints
     * d - array of doubles
     * l - array of longs
     * b - array of booleans
     * c - array of unsigned bytes (represented as array of ints)
     */
    public char[] propertiesTypes;
    public List<FbxElement> children = new ArrayList<>();

    public FbxElement(int propsCount) {
        this.properties = new ArrayList<Object>(propsCount);
        this.propertiesTypes = new char[propsCount];
    }
        
        public FbxElement getChildById(String name) {
            for (FbxElement element : children) {
                if (element.id.equals(name)) {
                    return element;
                }
            }
            return null;
        }
        
        public List<FbxElement> getFbxProperties() {
            List<FbxElement> props = new ArrayList<>();
            FbxElement propsElement = null;
            boolean legacy = false;
            
            for (FbxElement element : children) {
                if (element.id.equals("Properties70")) {
                    propsElement = element;
                    break;
                } else if (element.id.equals("Properties60")) {
                    legacy = true;
                    propsElement = element;
                    break;
                }
            }
            
            if (propsElement == null) { 
                return props;
            }
            
            for (FbxElement prop : propsElement.children) {
                if (prop.id.equals("P") || prop.id.equals("Property")) {
                    if (legacy) {
                        char[] types = new char[prop.propertiesTypes.length + 1];
                        types[0] = prop.propertiesTypes[0];
                        types[1] = prop.propertiesTypes[0];
                        System.arraycopy(prop.propertiesTypes, 1, types, 2, types.length - 2);
                        
                        List<Object> values = new ArrayList<>(prop.properties);
                        values.add(1, values.get(0));
                        
                        FbxElement dummyProp = new FbxElement(types.length);
                        dummyProp.children = prop.children;
                        dummyProp.id = prop.id;
                        dummyProp.propertiesTypes = types;
                        dummyProp.properties = values;
                        props.add(dummyProp);
                    } else {
                        props.add(prop);
                    }
                }
            }
            
            return props;
        }
        
        @Override
        public String toString() {
            return "FBXElement[id=" + id + ", numProps=" + properties.size() + ", numChildren=" + children.size() + "]";
        }
}
