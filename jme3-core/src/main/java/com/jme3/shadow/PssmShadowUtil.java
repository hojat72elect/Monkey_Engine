
package com.jme3.shadow;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.GeometryList;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Includes various useful shadow mapping functions.
 *
 * See
 * <ul>
 * <li><a href="http://appsrv.cse.cuhk.edu.hk/~fzhang/pssm_vrcia/">http://appsrv.cse.cuhk.edu.hk/~fzhang/pssm_vrcia/</a></li>
 * <li><a href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a></li>
 * </ul>
 * for more info.
 */
public final class PssmShadowUtil {

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private PssmShadowUtil() {
    }

    /**
     * Updates the frustum splits stores in <code>splits</code> using PSSM.
     *
     * @param splits the array of splits (modified)
     * @param near the distance to the camera's near plane
     * @param far the distance to the camera's far plane
     * @param lambda the mixing parameter (0&rarr;purely linear,
     * 1&rarr;purely logarithmic) 
     */
    public static void updateFrustumSplits(float[] splits, float near, float far, float lambda) {
        for (int i = 0; i < splits.length; i++) {
            float IDM = i / (float) splits.length;
            float log = near * FastMath.pow((far / near), IDM);
            float uniform = near + (far - near) * IDM;
            splits[i] = log * lambda + uniform * (1.0f - lambda);
        }

        // This is used to improve the correctness of the calculations. The near and far planes
        // of the camera always stay the same, no matter what happens.
        splits[0] = near;
        splits[splits.length - 1] = far;
    }

    /**
     * Compute the Zfar in the model view to adjust the Zfar distance for the splits calculation
     *
     * @param occ a list of occluders
     * @param recv a list of receivers
     * @param cam the Camera (not null, unaffected)
     * @return the Z-far distance
     */
    public static float computeZFar(GeometryList occ, GeometryList recv, Camera cam) {
        Matrix4f mat = cam.getViewMatrix();
        BoundingBox bbOcc = ShadowUtil.computeUnionBound(occ, mat);
        BoundingBox bbRecv = ShadowUtil.computeUnionBound(recv, mat);

        return min(max(bbOcc.getZExtent() - bbOcc.getCenter().z, bbRecv.getZExtent() - bbRecv.getCenter().z), cam.getFrustumFar());
    }
}
