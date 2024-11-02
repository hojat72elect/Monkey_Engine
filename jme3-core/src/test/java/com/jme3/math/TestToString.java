
package com.jme3.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test various toString() methods using JUnit. See also
 * {@link com.jme3.math.TestTransform}.
 *
 * @author Stephen Gold
 */
public class TestToString {
    /**
     * Test various {@code toString()} methods against their javadoc.
     */
    @Test
    public void testToString() {
        // Test data that's never modified:
        Line line = new Line(
                new Vector3f(1f, 0f, 0f),
                new Vector3f(0f, 1f, 0f));

        LineSegment segment = new LineSegment(
                new Vector3f(1f, 0f, 0f), new Vector3f(0f, 1f, 0f), 1f);

        Rectangle rectangle = new Rectangle(
                new Vector3f(1f, 0f, 0f),
                new Vector3f(2f, 0f, 0f),
                new Vector3f(1f, 2f, 0f));

        Triangle triangle = new Triangle(
                new Vector3f(1f, 0f, 0f),
                new Vector3f(0f, 1f, 0f),
                new Vector3f(0f, 0f, 1f));

        // Verify that the methods don't throw an exception:
        String lineString = line.toString();
        String segmentString = segment.toString();
        String rectangleString = rectangle.toString();
        String triangleString = triangle.toString();

        // Verify that the results match the javadoc:
        Assert.assertEquals(
                "Line [Origin: (1.0, 0.0, 0.0)  Direction: (0.0, 1.0, 0.0)]",
                lineString);
        Assert.assertEquals(
                "LineSegment [Origin: (1.0, 0.0, 0.0)  Direction: (0.0, 1.0, 0.0)  Extent: 1.0]",
                segmentString);
        Assert.assertEquals(
                "Rectangle [A: (1.0, 0.0, 0.0)  B: (2.0, 0.0, 0.0)  C: (1.0, 2.0, 0.0)]",
                rectangleString);
        Assert.assertEquals(
                "Triangle [V1: (1.0, 0.0, 0.0)  V2: (0.0, 1.0, 0.0)  V3: (0.0, 0.0, 1.0)]",
                triangleString);
    }
}
