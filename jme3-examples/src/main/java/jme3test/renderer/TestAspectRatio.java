
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Simple application to test a viewport/camera with a different aspect ratio
 * than the display -- issue #357. The cube should render as a blue square, not
 * a rectangle.
 *
 * Based closely on the test case submitted by slyh on September 28, 2015.
 */
public class TestAspectRatio extends SimpleApplication {

    public static void main(String[] args) {
        new TestAspectRatio().start();
    }

    @Override
    public void simpleInitApp() {
        Geometry cube = new Geometry("blue cube", new Box(1, 1, 1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        cube.setMaterial(mat);

        rootNode.attachChild(cube);

        // Trying to update the viewport:
        cam.setViewPortBottom(0.5f);
        cam.resize(640, 480 / 2, false);
        cam.setFrustumPerspective(40, 640f / (480 / 2), 0.05f, 500f);
        cam.update();
    }
}
