
package com.jme3.util;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;
import java.nio.FloatBuffer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Verifies that tangents can be generated without an index buffer. This was
 * issue #1909 at GitHub.
 *
 * @author Stephen Gold
 */
public class TestIssue1909 {
    /**
     * Tests MikktspaceTangentGenerator.generate() without index buffers.
     */
    @Test
    public void testIssue1909() {
        /*
         * Generate normals, texture coordinates, and vertex positions
         * for a large square in the X-Z plane.
         */
        FloatBuffer normals = BufferUtils.createFloatBuffer(
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f
        );
        float uvDiameter = 5f;
        FloatBuffer uvs = BufferUtils.createFloatBuffer(
                uvDiameter, uvDiameter,
                0f, 0f,
                uvDiameter, 0f,
                uvDiameter, uvDiameter,
                0f, uvDiameter,
                0f, 0f
        );
        float posRadius = 500f;
        FloatBuffer positions = BufferUtils.createFloatBuffer(
                +posRadius, 0f, +posRadius,
                -posRadius, 0f, -posRadius,
                -posRadius, 0f, +posRadius,
                +posRadius, 0f, +posRadius,
                +posRadius, 0f, -posRadius,
                -posRadius, 0f, -posRadius
        );
        Mesh mesh = new Mesh();
        int numAxes = 3;
        mesh.setBuffer(VertexBuffer.Type.Normal, numAxes, normals);
        mesh.setBuffer(VertexBuffer.Type.Position, numAxes, positions);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, uvs);
        mesh.updateBound();

        Geometry testGeometry = new Geometry("testGeometry", mesh);
        MikktspaceTangentGenerator.generate(testGeometry);

        VertexBuffer tangents = mesh.getBuffer(VertexBuffer.Type.Tangent);
        Assert.assertNotNull(tangents);
    }
}
