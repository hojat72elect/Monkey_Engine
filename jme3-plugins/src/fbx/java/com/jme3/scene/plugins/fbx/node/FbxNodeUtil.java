
package com.jme3.scene.plugins.fbx.node;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

public class FbxNodeUtil {
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private FbxNodeUtil() {
    }

    public static Quaternion quatFromBoneAngles(float xAngle, float yAngle, float zAngle) {
        float angle;
        float sinY, sinZ, sinX, cosY, cosZ, cosX;
        angle = zAngle * 0.5f;
        sinZ = FastMath.sin(angle);
        cosZ = FastMath.cos(angle);
        angle = yAngle * 0.5f;
        sinY = FastMath.sin(angle);
        cosY = FastMath.cos(angle);
        angle = xAngle * 0.5f;
        sinX = FastMath.sin(angle);
        cosX = FastMath.cos(angle);
        float cosYXcosZ = cosY * cosZ;
        float sinYXsinZ = sinY * sinZ;
        float cosYXsinZ = cosY * sinZ;
        float sinYXcosZ = sinY * cosZ;
        // For some reason bone space is differ, this is modified formulas
        float w = (cosYXcosZ * cosX + sinYXsinZ * sinX);
        float x = (cosYXcosZ * sinX - sinYXsinZ * cosX);
        float y = (sinYXcosZ * cosX + cosYXsinZ * sinX);
        float z = (cosYXsinZ * cosX - sinYXcosZ * sinX);
        return new Quaternion(x, y, z, w).normalizeLocal();
    }
}
