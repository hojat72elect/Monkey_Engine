

package com.jme3.scene.mesh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jme3.scene.Mesh;

/**
 * Tests selected methods of the Mesh class.
 *
 * @author Melvyn Linke
 */
public class MeshTest {

    /**
     * Tests getVertexCount() on a empty Mesh.
     */
    @Test
    public void testVertexCountOfEmptyMesh() {
        final Mesh mesh = new Mesh();

        assertEquals(-1, mesh.getVertexCount());
    }
}
