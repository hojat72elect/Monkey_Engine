
package com.jme3.scene.debug;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

public class WireFrustum extends Mesh {

    /**
     * This constructor is for serialization only. Do not use.
     */
    protected WireFrustum() {
    }

    public WireFrustum(Vector3f[] points){
        initGeom(this, points);
    }

    public static Mesh makeFrustum(Vector3f[] points){
        Mesh m = new Mesh();
        initGeom(m, points);
        return m;
    }

    private static void initGeom(Mesh m, Vector3f[] points) {
        if (points != null)
            m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(points));

        m.setBuffer(Type.Index, 2,
                new short[]{
                        0, 1,
                        1, 2,
                        2, 3,
                        3, 0,

                        4, 5,
                        5, 6,
                        6, 7,
                        7, 4,

                        0, 4,
                        1, 5,
                        2, 6,
                        3, 7,
                }
        );
        m.getBuffer(Type.Index).setUsage(Usage.Static);
        m.setMode(Mode.Lines);
    }

    public void update(Vector3f[] points){
        VertexBuffer vb = getBuffer(Type.Position);
        if (vb == null){
            setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(points));
            return;
        }

        FloatBuffer b = BufferUtils.createFloatBuffer(points);
        FloatBuffer a = (FloatBuffer) vb.getData();
        b.rewind();
        a.rewind();
        a.put(b);
        a.rewind();

        vb.updateData(a);
        
        updateBound();
    }

}
