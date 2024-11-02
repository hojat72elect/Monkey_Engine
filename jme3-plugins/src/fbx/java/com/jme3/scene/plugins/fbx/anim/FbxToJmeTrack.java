
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.animation.BoneTrack;
import com.jme3.animation.SpatialTrack;
import com.jme3.animation.Track;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.fbx.node.FbxNode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps animation stacks to influenced nodes. 
 * Will be used later to create jME3 tracks.
 */
public final class FbxToJmeTrack {

    public FbxAnimStack animStack;
    public FbxAnimLayer animLayer;
    public FbxNode node;

    // These are not used in map lookups.
    public transient final Map<String, FbxAnimCurveNode> animCurves = new HashMap<String, FbxAnimCurveNode>();

    public long[] getKeyTimes() {
        Set<Long> keyFrameTimesSet = new HashSet<>();
        for (FbxAnimCurveNode curveNode : animCurves.values()) {
            for (FbxAnimCurve curve : curveNode.getCurves()) {
                for (long keyTime : curve.getKeyTimes()) {
                    keyFrameTimesSet.add(keyTime);
                }
            }
        }
        long[] keyFrameTimes = new long[keyFrameTimesSet.size()];
        int i = 0;
        for (Long keyFrameTime : keyFrameTimesSet) {
            keyFrameTimes[i++] = keyFrameTime;
        }
        Arrays.sort(keyFrameTimes);
        return keyFrameTimes;
    }
    
    /**
     * Generate a {@link BoneTrack} from the animation data, for the given
     * boneIndex.
     * 
     * @param boneIndex The bone index for which track data is generated for.
     * @param inverseBindPose Inverse bind pose of the bone (in world space).
     * @return A BoneTrack containing the animation data, for the specified
     * boneIndex.
     */
    public BoneTrack toJmeBoneTrack(int boneIndex, Transform inverseBindPose) {
        return (BoneTrack) toJmeTrackInternal(boneIndex, inverseBindPose);
    }
    
    public SpatialTrack toJmeSpatialTrack() {
        return (SpatialTrack) toJmeTrackInternal(-1, null);
    }

    /**
     * Counts how many keyframes there are in the included curves.
     *
     * @return the total number of keyframes (&ge;0)
     */
    public int countKeyframes() {
        int count = 0;
        for (FbxAnimCurveNode curveNode : animCurves.values()) {
            for (FbxAnimCurve curve : curveNode.getCurves()) {
                count += curve.getKeyTimes().length;
            }
        }

        return count;
    }

    public float getDuration() {
        long[] keyframes = getKeyTimes();
        return (float) (keyframes[keyframes.length - 1] * FbxAnimUtil.SECONDS_PER_UNIT);
    }
    
    private static void applyInverse(Vector3f translation, Quaternion rotation, Vector3f scale, Transform inverseBindPose) {
        Transform t = new Transform();
        t.setTranslation(translation);
        t.setRotation(rotation);
        if (scale != null) {
            t.setScale(scale);
        }
        t.combineWithParent(inverseBindPose);
        
        t.getTranslation(translation);
        t.getRotation(rotation);
        if (scale != null) {
            t.getScale(scale);
        }
    }
    
    private Track toJmeTrackInternal(int boneIndex, Transform inverseBindPose) {
        float duration = animStack.getDuration();
        
        FbxAnimCurveNode translationCurve = animCurves.get("Lcl Translation");
        FbxAnimCurveNode rotationCurve    = animCurves.get("Lcl Rotation");
        FbxAnimCurveNode scalingCurve     = animCurves.get("Lcl Scaling");

        long[] fbxTimes = getKeyTimes();
        float[] times = new float[fbxTimes.length];
        
        // Translations / Rotations must be set on all tracks.
        // (Required for jME3)
        Vector3f[]   translations = new Vector3f[fbxTimes.length];
        Quaternion[] rotations = new Quaternion[fbxTimes.length];
        
        Vector3f[] scales = null;
        if (scalingCurve != null) {
            scales = new Vector3f[fbxTimes.length];
        }
         
         for (int i = 0; i < fbxTimes.length; i++) {
            long fbxTime = fbxTimes[i];
            float time = (float) (fbxTime * FbxAnimUtil.SECONDS_PER_UNIT);

            if (time > duration) {
                // Expand animation duration to fit the curve.
                duration = time;
//                System.out.println("actual duration: " + duration);
            }

            times[i] = time;
            if (translationCurve != null) {
                translations[i] = translationCurve.getVector3Value(fbxTime);
            } else {
                translations[i] = new Vector3f();
            }
            if (rotationCurve != null) {
                rotations[i] = rotationCurve.getQuaternionValue(fbxTime);
                if (i > 0) {
                    if (rotations[i - 1].dot(rotations[i]) < 0) {
//                        System.out.println("rotation will go the long way, oh noes");
                        rotations[i - 1].negateLocal();
                    }
                }
            } else {
                rotations[i] = new Quaternion();
            }
            if (scalingCurve != null) {
                scales[i] = scalingCurve.getVector3Value(fbxTime);
            }
            
            if (inverseBindPose != null) {
                applyInverse(translations[i], rotations[i], scales != null ? scales[i] : null, inverseBindPose);
            }
        }
        
        if (boneIndex == -1) {
            return new SpatialTrack(times, translations, rotations, scales);
        } else {
            if (scales != null) {
                return new BoneTrack(boneIndex, times, translations, rotations, scales);
            } else {
                return new BoneTrack(boneIndex, times, translations, rotations);
            }
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.animStack.hashCode();
        hash = 79 * hash + this.animLayer.hashCode();
        hash = 79 * hash + this.node.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final FbxToJmeTrack other = (FbxToJmeTrack) obj;
        return this.node == other.node
                && this.animStack == other.animStack
                && this.animLayer == other.animLayer;
    }
}
