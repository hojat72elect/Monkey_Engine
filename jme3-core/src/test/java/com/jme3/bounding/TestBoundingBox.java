
package com.jme3.bounding;

import com.jme3.math.Vector3f;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the BoundingBox class.
 *
 * @author Stephen Gold
 */
public class TestBoundingBox {
    /**
     * Verify that equals() behaves as expected.
     */
    @Test
    public void testEquals() {
        BoundingBox bb1 = new BoundingBox(new Vector3f(3f, 4f, 5f), 0f, 1f, 2f);
        BoundingBox bb2
                = new BoundingBox(new Vector3f(3f, 4f, 5f), -0f, 1f, 2f);

        BoundingBox bb3 = new BoundingBox(new Vector3f(3f, 0f, 2f), 9f, 8f, 7f);
        BoundingBox bb4
                = new BoundingBox(new Vector3f(3f, -0f, 2f), 9f, 8f, 7f);

        BoundingBox bb5 = new BoundingBox(new Vector3f(4f, 5f, 6f), 9f, 8f, 7f);
        BoundingBox bb6 = (BoundingBox) bb5.clone();
        bb6.setCheckPlane(1);

        // Clones are equal to their base instances:
        Assert.assertEquals(bb1, bb1.clone());
        Assert.assertEquals(bb2, bb2.clone());
        Assert.assertEquals(bb3, bb3.clone());
        Assert.assertEquals(bb4, bb4.clone());
        Assert.assertEquals(bb5, bb5.clone());
        Assert.assertEquals(bb6, bb6.clone());

        Assert.assertNotEquals(bb1, bb2); // because their extents differ
        Assert.assertNotEquals(bb3, bb4); // because their centers differ
        Assert.assertEquals(bb5, bb6); // because check planes are ignored
    }

    /**
     * Verify that isSimilar() behaves as expected.
     */
    @Test
    public void testIsSimilar() {
        BoundingBox bb1 = new BoundingBox(new Vector3f(3f, 4f, 5f), 0f, 1f, 2f);
        BoundingBox bb2
                = new BoundingBox(new Vector3f(3f, 4f, 5f), 0f, 1.1f, 2f);

        BoundingBox bb3 = new BoundingBox(new Vector3f(3f, 4f, 2f), 9f, 8f, 7f);
        BoundingBox bb4
                = new BoundingBox(new Vector3f(3f, 3.9f, 2f), 9f, 8f, 7f);

        BoundingBox bb5 = new BoundingBox(new Vector3f(4f, 5f, 6f), 9f, 8f, 7f);
        BoundingBox bb6 = (BoundingBox) bb5.clone();
        bb6.setCheckPlane(1);

        Assert.assertFalse(bb1.isSimilar(bb2, 0.09999f));
        Assert.assertTrue(bb1.isSimilar(bb2, 0.10001f));

        Assert.assertFalse(bb3.isSimilar(bb4, 0.09999f));
        Assert.assertTrue(bb3.isSimilar(bb4, 0.10001f));

        Assert.assertTrue(bb5.isSimilar(bb6, 0f)); // check planes are ignored
    }
}
