
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.node.FbxNode;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FbxAnimCurveNode extends FbxObject {

    private static final Logger logger = Logger.getLogger(FbxAnimCurveNode.class.getName());
    
    private final Map<FbxNode, String> influencedNodePropertiesMap = new HashMap<>();
    private final Map<String, FbxAnimCurve> propertyToCurveMap = new HashMap<>();
    private final Map<String, Float> propertyToDefaultMap = new HashMap<>();
    
    public FbxAnimCurveNode(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        for (FbxElement prop : element.getFbxProperties()) {
            String propName = (String) prop.properties.get(0);
            String propType = (String) prop.properties.get(1);
            if (propType.equals("Number")) {
                float propValue = ((Double) prop.properties.get(4)).floatValue();
                propertyToDefaultMap.put(propName, propValue);
            }
        }
    }
    
    public void addInfluencedNode(FbxNode node, String property) {
        influencedNodePropertiesMap.put(node, property);
    }
    
    public Map<FbxNode, String> getInfluencedNodeProperties() {
        return influencedNodePropertiesMap;
    }
    
    public Collection<FbxAnimCurve> getCurves() {
        return propertyToCurveMap.values();
    }
    
    public Vector3f getVector3Value(long time) {
        Vector3f value = new Vector3f();
        FbxAnimCurve xCurve = propertyToCurveMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_X);
        FbxAnimCurve yCurve = propertyToCurveMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_Y);
        FbxAnimCurve zCurve = propertyToCurveMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_Z);
        Float xDefault      = propertyToDefaultMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_X);
        Float yDefault      = propertyToDefaultMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_Y);
        Float zDefault      = propertyToDefaultMap.get(FbxAnimUtil.CURVE_NODE_PROPERTY_Z);
        value.x = xCurve != null ? xCurve.getValueAtTime(time) : xDefault;
        value.y = yCurve != null ? yCurve.getValueAtTime(time) : yDefault;
        value.z = zCurve != null ? zCurve.getValueAtTime(time) : zDefault;
        return value;
    }
    
    /**
     * Converts the euler angles from {@link #getVector3Value(long)} to
     * a quaternion rotation.
     * @param time Time at which to get the euler angles.
     * @return The rotation at time
     */
    public Quaternion getQuaternionValue(long time) {
        Vector3f eulerAngles = getVector3Value(time);
//        System.out.println("\tT: " + time + ". Rotation: " +
//                                eulerAngles.x + ", " +
//                                eulerAngles.y + ", " + eulerAngles.z);
        Quaternion q = new Quaternion();
        q.fromAngles(eulerAngles.x * FastMath.DEG_TO_RAD, 
                     eulerAngles.y * FastMath.DEG_TO_RAD, 
                     eulerAngles.z * FastMath.DEG_TO_RAD);
        return q;
    }
    
    @Override
    protected Object toJmeObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectObject(FbxObject object) {
        unsupportedConnectObject(object);
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        if (!(object instanceof FbxAnimCurve)) {
            unsupportedConnectObjectProperty(object, property);
        }
        if (!property.equals(FbxAnimUtil.CURVE_NODE_PROPERTY_X) &&
            !property.equals(FbxAnimUtil.CURVE_NODE_PROPERTY_Y) &&
            !property.equals(FbxAnimUtil.CURVE_NODE_PROPERTY_Z) &&
            !property.equals(FbxAnimUtil.CURVE_NODE_PROPERTY_VISIBILITY)) {
            logger.log(Level.WARNING, "Animating the dimension ''{0}'' is not "
                                    + "supported yet. Ignoring.", property);
            return;
        }
        
        if (propertyToCurveMap.containsKey(property)) {
            throw new UnsupportedOperationException("!");
        }
        
        propertyToCurveMap.put(property, (FbxAnimCurve) object);
    }
    
}
