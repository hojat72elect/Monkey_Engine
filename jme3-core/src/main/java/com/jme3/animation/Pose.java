
package com.jme3.animation;

import com.jme3.export.*;
import com.jme3.math.Vector3f;
import com.jme3.util.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * A pose is a list of offsets that say where a mesh vertices should be for this pose.
 */
@Deprecated
public final class Pose implements Savable, Cloneable {

    private String name;
    private int targetMeshIndex;

    private Vector3f[] offsets;
    private int[] indices;

    private transient final Vector3f tempVec = new Vector3f();
    private transient final Vector3f tempVec2 = new Vector3f();

    public Pose(String name, int targetMeshIndex, Vector3f[] offsets, int[] indices) {
        this.name = name;
        this.targetMeshIndex = targetMeshIndex;
        this.offsets = offsets;
        this.indices = indices;
    }

    /**
     * Serialization-only. Do not use.
     */
    protected Pose() {
    }

    public int getTargetMeshIndex() {
        return targetMeshIndex;
    }

    /**
     * Applies the offsets of this pose to the vertex buffer given by the blend factor.
     *
     * @param blend Blend factor, 0 = no change to vertex buffer, 1 = apply full offsets
     * @param vertexBuffer Vertex buffer to apply this pose to
     */
    public void apply(float blend, FloatBuffer vertexBuffer) {
        for (int i = 0; i < indices.length; i++) {
            Vector3f offset = offsets[i];
            int vertIndex = indices[i];

            tempVec.set(offset).multLocal(blend);

            // acquire vertex
            BufferUtils.populateFromBuffer(tempVec2, vertexBuffer, vertIndex);

            // add offset multiplied by factor
            tempVec2.addLocal(tempVec);

            // write modified vertex
            BufferUtils.setInBuffer(tempVec2, vertexBuffer, vertIndex);
        }
    }

    /**
     * This method creates a clone of the current object.
     * @return a clone of the current object
     */
    @Override
    public Pose clone() {
        try {
            Pose result = (Pose) super.clone();
            result.indices = this.indices.clone();
            if (this.offsets != null) {
                result.offsets = new Vector3f[this.offsets.length];
                for (int i = 0; i < this.offsets.length; ++i) {
                    result.offsets[i] = this.offsets[i].clone();
                }
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        OutputCapsule out = e.getCapsule(this);
        out.write(name, "name", "");
        out.write(targetMeshIndex, "meshIndex", -1);
        out.write(offsets, "offsets", null);
        out.write(indices, "indices", null);
    }

    @Override
    public void read(JmeImporter i) throws IOException {
        InputCapsule in = i.getCapsule(this);
        name = in.readString("name", "");
        targetMeshIndex = in.readInt("meshIndex", -1);
        indices = in.readIntArray("indices", null);

        Savable[] readSavableArray = in.readSavableArray("offsets", null);
        if (readSavableArray != null) {
            offsets = new Vector3f[readSavableArray.length];
            System.arraycopy(readSavableArray, 0, offsets, 0, readSavableArray.length);
        }
    }
}
