

package com.jme3.tools;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.TestUtil;

import jme3tools.optimize.LodGenerator;

/**
 * Tests the result of the LodGenerator.
 *
 * @author Melvyn Linke
 */
public class LodGeneratorTest {
    AssetManager assetManager = TestUtil.createAssetManager();

    float[] REDUCTION_VALUES = { 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.80f };

    /**
     * Tests the construction of the LodGenerator.
     */
    @Test
    public void testInit() {
        LodGenerator lod = new LodGenerator(sphere());
        assert true;
    }

    /**
     * Returns a List of the sizes of the VertexBuff.
     */
    private int[] getBufferSizes(VertexBuffer[] buffers) {
        int[] result = new int[buffers.length];

        for (int i = 0; i < buffers.length; i++) {
            result[i] = buffers[i].getNumElements();
        }

        return result;
    }

    /**
     * Tests the LodGenerator with proportional reduction on a sphere(see sphere()).
     */
    @Test
    public void testSphereReductionProportional() {
        LodGenerator lod = new LodGenerator(sphere());
        VertexBuffer[] buffer = lod.computeLods(LodGenerator.TriangleReductionMethod.PROPORTIONAL,
                REDUCTION_VALUES);

        int[] expected = { 240, 120, 108, 96, 84, 72, 60, 48 };
        int[] actual = getBufferSizes(buffer);

        assertArrayEquals(expected, actual);
    }

    /**
     * Tests the LodGenerator with collapse cost reduction on a sphere(see sphere()).
     */
    @Test
    public void testSphereReductionCollapsCost() {
        LodGenerator lod = new LodGenerator(sphere());
        VertexBuffer[] buffer = lod.computeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST,
                REDUCTION_VALUES);

        int[] expected = { 240, 6, 2, 1 };
        int[] actual = getBufferSizes(buffer);
        assert buffer != null;
        assertArrayEquals(expected, actual);

    }

    /**
     * Returns the mesh of a node.
     */
    private Mesh getMesh(Node node) {
        Mesh m = null;
        for (Spatial spatial : node.getChildren()) {
            if (spatial instanceof Geometry) {
                m = ((Geometry) spatial).getMesh();
                if (m.getVertexCount() == 5108) {

                }

            }
        }
        return m;
    }

    /**
     * Returns the Monkey mesh used in the TestLodGeneration stresstest. Note: Doesn't work durring gradle
     * build.
     */
    private Mesh monkey() {
        Node model = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        return getMesh(model);
    }

    /**
     * Returns a 12x12 Sphere mesh.
     */
    private Mesh sphere() {
        return new Sphere(12, 12, 1, false, false);
    }

    /**
     * Tests the LodGenerator with constnat reduction on a monkey(see monkey()).
     */
    // @Test
    public void testMonkeyReductionConstant() {

        LodGenerator lod = new LodGenerator(monkey());
        VertexBuffer[] buffer = lod.computeLods(LodGenerator.TriangleReductionMethod.CONSTANT,
                REDUCTION_VALUES);

        int[] expected = { 5108 };
        int[] actual = getBufferSizes(buffer);

        assertArrayEquals(expected, actual);
    }

    /**
     * Tests the LodGenerator with proportional reduction on a sphere(see sphere()).
     */
    // @Test
    public void testMonkeyReductionProportional() {

        LodGenerator lod = new LodGenerator(monkey());
        VertexBuffer[] buffer = lod.computeLods(LodGenerator.TriangleReductionMethod.PROPORTIONAL,
                REDUCTION_VALUES);

        int[] expected = { 5108, 2553, 2298, 2043, 1787, 1531, 1276, 1021 };
        int[] actual = getBufferSizes(buffer);

        assertArrayEquals(expected, actual);
    }

    /**
     * Tests the LodGenerator with collapse cost reduction on a monkey(see monkey()).
     */
    // @Test
    public void testMonkeyReductionCollapsCost() {
        LodGenerator lod = new LodGenerator(monkey());
        VertexBuffer[] buffer = lod.computeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST,
                REDUCTION_VALUES);

        int[] expected = { 5108, 16 };
        int[] actual = getBufferSizes(buffer);

        assertArrayEquals(expected, actual);
    }
}