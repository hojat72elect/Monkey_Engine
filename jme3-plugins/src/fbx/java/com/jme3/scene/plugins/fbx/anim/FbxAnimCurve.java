
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.obj.FbxObject;

public class FbxAnimCurve extends FbxObject {

    private long[] keyTimes;
    private float[] keyValues;
    
    public FbxAnimCurve(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        
        for (FbxElement e : element.children) {
            if (e.id.equals("KeyTime")) {
                keyTimes = (long[]) e.properties.get(0);
            } else if (e.id.equals("KeyValueFloat")) {
                keyValues = (float[]) e.properties.get(0);
            }
        }
        
        long time = -1;
        for (int i = 0; i < keyTimes.length; i++) {
            if (time >= keyTimes[i]) {
                throw new UnsupportedOperationException("Keyframe times must be sequential, but they are not.");
            }
            time = keyTimes[i];
        }
    }

    /**
     * Get the times for the keyframes.
     * @return Keyframe times. 
     */
    public long[] getKeyTimes() {
        return keyTimes;
    }
    
    /**
     * Retrieve the curve value at the given time.
     * If the curve has no data, 0 is returned.
     * If the time is outside the curve, then the closest value is returned.
     * If the time isn't on an exact keyframe, linear interpolation is used
     * to determine the value between the keyframes at the given time.
     * @param time The time to get the curve value at (in FBX time units).
     * @return The value at the given time.
     */
    public float getValueAtTime(long time) {
        if (keyTimes.length == 0) {
            return 0;
        }
        
        // If the time is outside the range, 
        // we just return the closest value. (No extrapolation)
        if (time <= keyTimes[0]) {
            return keyValues[0];
        } else if (time >= keyTimes[keyTimes.length - 1]) {
            return keyValues[keyValues.length - 1];
        }
        
        

        int startFrame = 0;
        int endFrame = 1;
        int lastFrame = keyTimes.length - 1;
        
        for (int i = 0; i < lastFrame && keyTimes[i] < time; ++i) {
            startFrame = i;
            endFrame = i + 1;
        }
        
        long keyTime1    = keyTimes[startFrame];
        float keyValue1  = keyValues[startFrame];
        long keyTime2    = keyTimes[endFrame];
        float keyValue2  = keyValues[endFrame];
        
        if (keyTime2 == time) {
            return keyValue2;
        }
        
        long prevToNextDelta    = keyTime2 - keyTime1;
        long prevToCurrentDelta = time     - keyTime1;
        float lerpAmount = (float)prevToCurrentDelta / prevToNextDelta;
        
        return FastMath.interpolateLinear(lerpAmount, keyValue1, keyValue2);
    }

    @Override
    protected Object toJmeObject() {
        // An AnimCurve has no jME3 representation.
        // The parent AnimCurveNode is responsible to create the jME3 
        // representation.
        throw new UnsupportedOperationException("No jME3 object conversion available");
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
