
package com.jme3.scene.debug;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

public class WireBox extends Mesh {

    public WireBox() {
        this(1,1,1);
    }

    public WireBox(float xExt, float yExt, float zExt) {
        updatePositions(xExt,yExt,zExt);
        setBuffer(Type.Index, 2,
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
        setMode(Mode.Lines);

        updateCounts();
    }

    public void updatePositions(float xExt, float yExt, float zExt) {
        VertexBuffer pvb = getBuffer(Type.Position);
        FloatBuffer pb;
        if (pvb == null) {
            pvb = new VertexBuffer(Type.Position);
            pb = BufferUtils.createVector3Buffer(8);
            pvb.setupData(Usage.Dynamic, 3, Format.Float, pb);
            setBuffer(pvb);
        } else {
            pb = (FloatBuffer) pvb.getData();
            pvb.updateData(pb);
        }
        pb.rewind();
        pb.put(
            new float[]{
                -xExt, -yExt,  zExt,
                 xExt, -yExt,  zExt,
                 xExt,  yExt,  zExt,
                -xExt,  yExt,  zExt,

                -xExt, -yExt, -zExt,
                 xExt, -yExt, -zExt,
                 xExt,  yExt, -zExt,
                -xExt,  yExt, -zExt,
            }
        );
        updateBound();
    }

    /**
     * Create a geometry suitable for visualizing the specified bounding box.
     *
     * @param bbox the bounding box (not null)
     * @return a new Geometry instance in world space
     */
    public static Geometry makeGeometry(BoundingBox bbox) {
        float xExtent = bbox.getXExtent();
        float yExtent = bbox.getYExtent();
        float zExtent = bbox.getZExtent();
        WireBox mesh = new WireBox(xExtent, yExtent, zExtent);
        Geometry result = new Geometry("bounding box", mesh);

        Vector3f center = bbox.getCenter();
        result.setLocalTranslation(center);

        return result;
    }
}