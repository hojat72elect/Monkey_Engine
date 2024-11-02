

package com.jme3.scene.debug.custom;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class JointShape extends Mesh {

    /**
     * Serialization only. Do not use.
     */
    protected JointShape() {
        float width = 1;
        float height = 1;
        setBuffer(Type.Position, 3, new float[]{-width * 0.5f, -width * 0.5f, 0,
                width * 0.5f, -width * 0.5f, 0,
                width * 0.5f, height * 0.5f, 0,
                -width * 0.5f, height * 0.5f, 0
        });


        setBuffer(Type.TexCoord, 2, new float[]{0, 0,
                1, 0,
                1, 1,
                0, 1});

        setBuffer(Type.Normal, 3, new float[]{0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1});

        setBuffer(Type.Color, 4, new float[]{1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1});

        setBuffer(Type.Index, 3, new short[]{0, 1, 2,
                0, 2, 3});


        updateBound();
        setStatic();
    }
}
