
package com.jme3.bounding;

import com.jme3.math.Vector3f;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the BoundingSphere class.
 *
 * @author Stephen Gold
 */
public class TestBoundingSphere {
    /**
     * Verify that equals() behaves as expected.
     */
    @Test
    public void testEquals() {
        BoundingSphere bs1 = new BoundingSphere(0f, new Vector3f(3f, 4f, 5f));
        BoundingSphere bs2 = new BoundingSphere(-0f, new Vector3f(3f, 4f, 5f));

        BoundingSphere bs3 = new BoundingSphere(1f, new Vector3f(3f, 0f, 2f));
        BoundingSphere bs4 = new BoundingSphere(1f, new Vector3f(3f, -0f, 2f));

        BoundingSphere bs5 = new BoundingSphere(2f, new Vector3f(4f, 5f, 6f));
        BoundingSphere bs6 = (BoundingSphere) bs5.clone();
        bs6.setCheckPlane(1);

        // Clones are equal to their base instances:
        Assert.assertEquals(bs1, bs1.clone());
        Assert.assertEquals(bs2, bs2.clone());
        Assert.assertEquals(bs3, bs3.clone());
        Assert.assertEquals(bs4, bs4.clone());
        Assert.assertEquals(bs5, bs5.clone());
        Assert.assertEquals(bs6, bs6.clone());

        Assert.assertNotEquals(bs1, bs2); // because their radii differ
        Assert.assertNotEquals(bs3, bs4); // because their centers differ
        Assert.assertEquals(bs5, bs6); // because check planes are ignored
    }

    /**
     * Verify that isSimilar() behaves as expected.
     */
    @Test
    public void testIsSimilar() {
        BoundingSphere bs1 = new BoundingSphere(0f, new Vector3f(3f, 4f, 5f));
        BoundingSphere bs2 = new BoundingSphere(0.1f, new Vector3f(3f, 4f, 5f));

        BoundingSphere bs3 = new BoundingSphere(1f, new Vector3f(3f, 4f, 2f));
        BoundingSphere bs4 = new BoundingSphere(1f, new Vector3f(3f, 3.9f, 2f));

        BoundingSphere bs5 = new BoundingSphere(2f, new Vector3f(4f, 5f, 6f));
        BoundingSphere bs6 = (BoundingSphere) bs5.clone();
        bs6.setCheckPlane(1);

        Assert.assertFalse(bs1.isSimilar(bs2, 0.09999f));
        Assert.assertTrue(bs1.isSimilar(bs2, 0.10001f));

        Assert.assertFalse(bs3.isSimilar(bs4, 0.09999f));
        Assert.assertTrue(bs3.isSimilar(bs4, 0.10001f));

        Assert.assertTrue(bs5.isSimilar(bs6, 0f)); // check planes are ignored
    }

    /**
     * Verify that an infinite bounding sphere can be merged with a very
     * eccentric bounding box without producing NaNs. This was issue #1459 at
     * GitHub.
     */
    @Test
    public void testIssue1459() {
        Vector3f boxCenter = new Vector3f(-92f, 3.3194322e29f, 674.89886f);
        BoundingBox boundingBox = new BoundingBox(boxCenter,
                1.0685959f, 3.3194322e29f, 2.705017f);

        Vector3f sphCenter = new Vector3f(0f, 0f, 0f);
        float radius = Float.POSITIVE_INFINITY;
        BoundingSphere boundingSphere = new BoundingSphere(radius, sphCenter);

        boundingSphere.mergeLocal(boundingBox);

        Vector3f copyCenter = new Vector3f();
        boundingSphere.getCenter(copyCenter);
        Assert.assertTrue(Vector3f.isValidVector(copyCenter));
    }
}
