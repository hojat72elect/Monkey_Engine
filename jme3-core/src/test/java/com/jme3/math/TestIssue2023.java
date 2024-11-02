
package com.jme3.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Verify that getRotationColumn() returns correct values for non-normalized
 * Quaternions. This was issue #2023 at GitHub.
 *
 * @author Stephen Gold
 */
public class TestIssue2023 {

    /**
     * Test a couple non-normalized quaternions.
     */
    @Test
    public void testIssue2023() {
        Quaternion test1 = new Quaternion(2f, 0.5f, 1f, -0.3f);

        Vector3f col0 = test1.getRotationColumn(0);
        Assert.assertEquals(0.5318352f, col0.x, 1e-6f);
        Assert.assertEquals(0.26217228f, col0.y, 1e-6f);
        Assert.assertEquals(0.80524343f, col0.z, 1e-6f);

        Vector3f col1 = test1.getRotationColumn(1);
        Assert.assertEquals(0.4868914f, col1.x, 1e-6f);
        Assert.assertEquals(-0.8726592f, col1.y, 1e-6f);
        Assert.assertEquals(-0.03745319f, col1.z, 1e-6f);

        Vector3f col2 = test1.getRotationColumn(2);
        Assert.assertEquals(0.6928839f, col2.x, 1e-6f);
        Assert.assertEquals(0.41198504f, col2.y, 1e-6f);
        Assert.assertEquals(-0.5917603f, col2.z, 1e-6f);

        Quaternion test2 = new Quaternion(0f, -0.2f, 0f, 0.6f);

        col0 = test2.getRotationColumn(0);
        Assert.assertEquals(0.8f, col0.x, 1e-6f);
        Assert.assertEquals(0f, col0.y, 1e-6f);
        Assert.assertEquals(0.6f, col0.z, 1e-6f);

        col1 = test2.getRotationColumn(1);
        Assert.assertEquals(0f, col1.x, 1e-6f);
        Assert.assertEquals(1f, col1.y, 1e-6f);
        Assert.assertEquals(0f, col1.z, 1e-6f);

        col2 = test2.getRotationColumn(2);
        Assert.assertEquals(-0.6f, col2.x, 1e-6f);
        Assert.assertEquals(0f, col2.y, 1e-6f);
        Assert.assertEquals(0.8f, col2.z, 1e-6f);
    }
}
