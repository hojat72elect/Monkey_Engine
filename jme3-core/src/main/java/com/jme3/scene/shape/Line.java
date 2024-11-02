
package com.jme3.scene.shape;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * A simple line implementation with a start and an end.
 * 
 * @author Brent Owens
 */
public class Line extends Mesh {

    private Vector3f start = new Vector3f();
    private Vector3f end = new Vector3f();

    /**
     * No-argument constructor needed by SavableClassUtil.
     */
    protected Line() {
    }

    public Line(Vector3f start, Vector3f end) {
        setMode(Mode.Lines);
        updateGeometry(start, end);
    }

    protected void updateGeometry(Vector3f start, Vector3f end) {
        this.start.set(start);
        this.end.set(end);
        setBuffer(Type.Position, 3, new float[]{start.x,    start.y,    start.z,
                                                end.x,      end.y,      end.z,});

        setBuffer(Type.TexCoord, 2, new float[]{0, 0,
                                                1, 1});

        setBuffer(Type.Normal, 3, new float[]{0, 0, 1,
                                              0, 0, 1});

        setBuffer(Type.Index, 2, new short[]{0, 1});

        updateBound();
    }

    /**
     * Alter the start and end.
     *
     * @param start the desired mesh location of the start (not null,
     * unaffected)
     * @param end the desired mesh location of the end (not null, unaffected)
     */
    public void updatePoints(Vector3f start, Vector3f end) {
        this.start.set(start);
        this.end.set(end);

        VertexBuffer posBuf = getBuffer(Type.Position);
        
        FloatBuffer fb = (FloatBuffer) posBuf.getData();
        fb.rewind();
        fb.put(start.x).put(start.y).put(start.z);
        fb.put(end.x).put(end.y).put(end.z);
        
        posBuf.updateData(fb);
        
        updateBound();
    }

    public Vector3f getEnd() {
        return end;
    }

    public Vector3f getStart() {
        return start;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);

        out.write(start, "startVertex", null);
        out.write(end, "endVertex", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);

        start = (Vector3f) in.readSavable("startVertex", start);
        end = (Vector3f) in.readSavable("endVertex", end);
    }
}
