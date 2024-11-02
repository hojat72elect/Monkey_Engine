
package com.jme3.scene.shape;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that all shapes have had a world bound calculated, and that vertices are within those bounds. Test
 * added for issue #1121. This is a clear failure: BoundingBox [Center: (0.0, 0.0, 0.0) xExtent: 0.0 yExtent:
 * 0.0 zExtent: 0.0]
 *
 * @author lou
 */
public class ShapeBoundsTest {

    @Test
    public void testBox() {
        Box shape = new Box(2f, 3f, 0.8f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testCurve() {
        Vector3f[] controlPoints = new Vector3f[4];
        controlPoints[0] = new Vector3f(0, 0, 0);
        controlPoints[1] = new Vector3f(1, 1, 1);
        controlPoints[2] = new Vector3f(2, 1, 1);
        controlPoints[3] = new Vector3f(3, 2, 1);
        Curve shape = new Curve(controlPoints, 32);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testCylinder() {
        Cylinder shape = new Cylinder(16, 16, 2f, 3f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testDome() {
        Dome shape = new Dome(16, 16, 5f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testLine() {
        Line shape = new Line(new Vector3f(0, 0, 0), new Vector3f(1, 2, 3));
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testPqTorus() {
        PQTorus shape = new PQTorus(2f, 3f, 0.8f, 0.2f, 64, 16);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void testQuad() {
        Quad shape = new Quad(64, 16);
        Geometry geometry = new Geometry("geom", shape);
        BoundingVolume bv = geometry.getWorldBound();
        BoundingBox bb = (BoundingBox) bv;
        //Quad z extent 0 is normal, so not using testBounds() here.
        Assert.assertTrue(bb.getXExtent() > 0 && bb.getYExtent() > 0);
        testVertices(geometry);
    }

    @Test
    public void Sphere() {
        Sphere shape = new Sphere(32, 32, 5f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void StripBox() {
        StripBox shape = new StripBox(0.2f, 64f, 16f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    @Test
    public void Torus() {
        Torus shape = new Torus(32, 32, 2f, 3f);
        Geometry geometry = new Geometry("geom", shape);
        testBounds(geometry);
    }

    private void testBounds(Geometry geometry) {
        BoundingVolume bv = geometry.getWorldBound();

        if (bv instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) bv;
            Assert.assertTrue(bb.getXExtent() > 0 && bb.getYExtent() > 0 && bb.getZExtent() > 0);
        } else if (bv instanceof BoundingSphere) {
            BoundingSphere bs = (BoundingSphere) bv;
            Assert.assertTrue(bs.getRadius() > 1f);
        }

        testVertices(geometry);
    }

    private void testVertices(Geometry geometry) {
        BoundingVolume bv = geometry.getWorldBound();
        Assert.assertNotNull(bv);

        for (int e = 0; e < geometry.getVertexCount(); e++) {
            float x = (Float) geometry.getMesh().getBuffer(VertexBuffer.Type.Position).getElementComponent(e, 0);
            float y = (Float) geometry.getMesh().getBuffer(VertexBuffer.Type.Position).getElementComponent(e, 1);
            float z = (Float) geometry.getMesh().getBuffer(VertexBuffer.Type.Position).getElementComponent(e, 2);
            Vector3f vertex = new Vector3f(x, y, z);
            Assert.assertTrue("Vertex outside world bound: " + vertex, bv.intersects(vertex));
        }
    }
}
