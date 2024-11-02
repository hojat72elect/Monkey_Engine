
package com.jme3.scene;

import com.jme3.scene.control.UpdateControl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests selected methods of the Spatial class.
 *
 * @author Stephen Gold
 */
public class SpatialTest {

    /**
     * Tests addControlAt() with a duplicate Control.
     */
    @Test(expected = IllegalStateException.class)
    public void addControlAtDuplicate() {
        Spatial testSpatial = new Node("testSpatial");
        UpdateControl control1 = new UpdateControl();
        testSpatial.addControlAt(0, control1);
        testSpatial.addControlAt(1, control1);
    }

    /**
     * Tests addControlAt() with a negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void addControlAtNegativeIndex() {
        Spatial testSpatial = new Node("testSpatial");
        UpdateControl control1 = new UpdateControl();
        testSpatial.addControlAt(-1, control1);
    }

    /**
     * Tests addControlAt() with a null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addControlAtNullControl() {
        Spatial testSpatial = new Node("testSpatial");
        testSpatial.addControlAt(0, null);
    }

    /**
     * Tests addControlAt() with an out-of-range positive index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void addControlAtOutOfRange() {
        Spatial testSpatial = new Node("testSpatial");
        UpdateControl control1 = new UpdateControl();
        testSpatial.addControlAt(1, control1);
    }

    /**
     * Tests typical uses of addControlAt().
     */
    @Test
    public void testAddControlAt() {
        Spatial testSpatial = new Node("testSpatial");

        // Add to an empty list.
        UpdateControl control1 = new UpdateControl();
        testSpatial.addControlAt(0, control1);

        Assert.assertEquals(1, testSpatial.getNumControls());
        Assert.assertEquals(control1, testSpatial.getControl(0));
        Assert.assertEquals(testSpatial, control1.getSpatial());

        // Add at the end of a non-empty list.
        UpdateControl control2 = new UpdateControl();
        testSpatial.addControlAt(1, control2);

        Assert.assertEquals(2, testSpatial.getNumControls());
        Assert.assertEquals(control1, testSpatial.getControl(0));
        Assert.assertEquals(control2, testSpatial.getControl(1));
        Assert.assertEquals(testSpatial, control1.getSpatial());
        Assert.assertEquals(testSpatial, control2.getSpatial());

        // Add at the beginning of a non-empty list.
        UpdateControl control0 = new UpdateControl();
        testSpatial.addControlAt(0, control0);

        Assert.assertEquals(3, testSpatial.getNumControls());
        Assert.assertEquals(control0, testSpatial.getControl(0));
        Assert.assertEquals(control1, testSpatial.getControl(1));
        Assert.assertEquals(control2, testSpatial.getControl(2));
        Assert.assertEquals(testSpatial, control0.getSpatial());
        Assert.assertEquals(testSpatial, control1.getSpatial());
        Assert.assertEquals(testSpatial, control2.getSpatial());
    }
}
