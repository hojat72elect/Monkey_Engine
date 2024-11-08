
package com.jme3.scene;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Quad;
import com.jme3.system.JmeSystem;
import com.jme3.system.MockJmeSystemDelegate;
import org.junit.Test;

/**
 * Verify that collideWith() doesn't report collisions with phantom triangles.
 * This was issue #710 at GitHub.
 *
 * @author Stephen Gold
 */
public class PhantomTrianglesTest {

    AssetManager assetManager;

    /**
     * ray in the -Z direction, starting from (0.1, 0.2, 10)
     */
    final private Ray ray = new Ray(/* origin */new Vector3f(0.1f, 0.2f, 10f),
            /* direction */ new Vector3f(0f, 0f, -1f));
    Node rootNode;

    /**
     * Cast a ray at the geometries and report all collisions.
     */
    void castRay() {
        CollisionResults results = new CollisionResults();
        rootNode.collideWith(ray, results);
        int numResults = results.size();
        for (int resultI = 0; resultI < numResults; resultI++) {
            CollisionResult result = results.getCollision(resultI);
            Geometry geometry = result.getGeometry();
            String name = geometry.getName();
            if (name.equals("white lines")) {
                assert false; // phantom triangle
            }
        }
    }

    /**
     * Attach a red square in the XY plane with its lower left corner at (0, 0,
     * 0). It is composed of 2 triangles.
     */
    void createRedSquare() {
        Mesh quadMesh = new Quad(1f, 1f);
        Geometry redSquare = new Geometry("red square", quadMesh);
        Material red = assetManager.loadMaterial("Common/Materials/RedColor.j3m");
        redSquare.setMaterial(red);
        rootNode.attachChild(redSquare);
    }

    /**
     * Attach a pair of parallel white lines in the z=1 plane.
     */
    void createWhiteLines() {
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        float[] corners = new float[]{
            -1f, -1f, 0f,
            -1f, 1f, 0f,
            1f, 1f, 0f,
            1f, -1f, 0f
        };
        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, corners);
        short[] indices = new short[]{0, 1, 2, 3};
        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, indices);
        lineMesh.updateBound();
        Geometry whiteLines = new Geometry("white lines", lineMesh);
        Material white = assetManager.loadMaterial("Common/Materials/WhiteColor.j3m");
        whiteLines.setMaterial(white);
        whiteLines.move(0f, 0f, 1f);
        rootNode.attachChild(whiteLines);
    }

    @Test
    public void testPhantomTriangles() {
        JmeSystem.setSystemDelegate(new MockJmeSystemDelegate());
        assetManager = new DesktopAssetManager();
        assetManager.registerLocator(null, ClasspathLocator.class);
        assetManager.registerLoader(J3MLoader.class, "j3m", "j3md");
        rootNode = new Node();

        createRedSquare();
        createWhiteLines();
        rootNode.updateLogicalState(0.01f);
        rootNode.updateGeometricState();
        castRay();
    }
}
