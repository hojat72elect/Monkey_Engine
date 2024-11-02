 
package com.jme3.environment.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 
 * A debugging shape for a BoundingSphere 
 * Consists of 3 axis aligned circles.
 * 
 * @author nehon
 */
public class BoundingSphereDebug extends Mesh {

    protected int vertCount;
    protected int triCount;
    protected int radialSamples = 32;
    protected boolean useEvenSlices;
    protected boolean interior;

    public BoundingSphereDebug() {
        setGeometryData();
        setIndexData();
    }

    /**
     * builds the vertices based on the radius
     */
    private void setGeometryData() {
        setMode(Mode.Lines);

        FloatBuffer posBuf = BufferUtils.createVector3Buffer((radialSamples + 1) * 3);
        FloatBuffer colBuf = BufferUtils.createVector3Buffer((radialSamples + 1) * 4);

        setBuffer(Type.Position, 3, posBuf);
        setBuffer(Type.Color, 4, colBuf);

        // generate geometry
        float fInvRS = 1.0f / radialSamples;

        // Generate points on the unit circle to be used in computing the mesh
        // points on a sphere slice.
        float[] afSin = new float[(radialSamples + 1)];
        float[] afCos = new float[(radialSamples + 1)];
        for (int iR = 0; iR < radialSamples; iR++) {
            float fAngle = FastMath.TWO_PI * fInvRS * iR;
            afCos[iR] = FastMath.cos(fAngle);
            afSin[iR] = FastMath.sin(fAngle);
        }
        afSin[radialSamples] = afSin[0];
        afCos[radialSamples] = afCos[0];

        for (int iR = 0; iR <= radialSamples; iR++) {
            posBuf.put(afCos[iR])
                    .put(afSin[iR])
                    .put(0);
            colBuf.put(ColorRGBA.Blue.r)
                    .put(ColorRGBA.Blue.g)
                    .put(ColorRGBA.Blue.b)
                    .put(ColorRGBA.Blue.a);

        }
        for (int iR = 0; iR <= radialSamples; iR++) {
            posBuf.put(afCos[iR])
                    .put(0)
                    .put(afSin[iR]);
            colBuf.put(ColorRGBA.Green.r)
                    .put(ColorRGBA.Green.g)
                    .put(ColorRGBA.Green.b)
                    .put(ColorRGBA.Green.a);
        }
        for (int iR = 0; iR <= radialSamples; iR++) {
            posBuf.put(0)
                    .put(afCos[iR])
                    .put(afSin[iR]);
            colBuf.put(ColorRGBA.Yellow.r)
                    .put(ColorRGBA.Yellow.g)
                    .put(ColorRGBA.Yellow.b)
                    .put(ColorRGBA.Yellow.a);
        }

        updateBound();
        setStatic();
    }

    /**
     * sets the indices for rendering the sphere.
     */
    private void setIndexData() {

        // allocate connectivity
        int nbSegments = (radialSamples) * 3;

        ShortBuffer idxBuf = BufferUtils.createShortBuffer(2 * nbSegments);
        setBuffer(Type.Index, 2, idxBuf);

        int idx = 0;
        int segDone = 0;
        while (segDone < nbSegments) {
            idxBuf.put((short) idx);
            idxBuf.put((short) (idx + 1));
            idx++;
            segDone++;
            if (segDone == radialSamples || segDone == radialSamples * 2) {
                idx++;
            }
        }
    }
    
    /**
     * Convenience factory method that creates a debug bounding-sphere geometry
     * 
     * @param assetManager the assetManager
     * @return the bounding sphere debug geometry.
     */
    public static Geometry createDebugSphere(AssetManager assetManager) {
        BoundingSphereDebug mesh = new BoundingSphereDebug();
        Geometry geom = new Geometry("BoundingDebug", mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);
        return geom;
    }
    
}
