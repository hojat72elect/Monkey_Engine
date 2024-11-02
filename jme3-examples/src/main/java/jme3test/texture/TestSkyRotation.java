
package jme3test.texture;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;

/**
 * Simple application to test sky rotation with a cube-mapped sky.
 *
 * Press "T" to rotate the sky and floor to the camera's left. Press "Y" to
 * rotate the sky and floor to the camera's right. Both should appear to move by
 * the same amount in the same direction.
 *
 * See issue #651 for further information.
 *
 * @author Stephen Gold
 */
public class TestSkyRotation extends SimpleApplication implements ActionListener {

    /**
     * objects visible in the scene
     */
    private Spatial floor, sky;
    /**
     * Y-axis rotation angle in radians
     */
    private float angle = 0f;

    public static void main(String[] arguments) {
        TestSkyRotation application = new TestSkyRotation();
        application.start();
    }

    @Override
    public void simpleInitApp() {
        /*
         * Configure the camera.
         */
        flyCam.setEnabled(false);
        Vector3f location = new Vector3f(-7f, 4f, 8f);
        cam.setLocation(location);
        Quaternion orientation;
        orientation = new Quaternion(0.0037f, 0.944684f, -0.01067f, 0.327789f);
        assert FastMath.approximateEquals(orientation.norm(), 1f);
        cam.setRotation(orientation);
        /*
         * Attach a cube-mapped sky to the scene graph.
         */
        sky = SkyFactory.createSky(assetManager,
                "Scenes/Beach/FullskiesSunset0068.dds",
                SkyFactory.EnvMapType.CubeMap);
        rootNode.attachChild(sky);
        /*
         * Attach a "floor" geometry to the scene graph.
         */
        Mesh floorMesh = new Box(10f, 0.1f, 10f);
        floor = new Geometry("floor", floorMesh);
        Material floorMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        floorMaterial.setTexture("ColorMap",
                assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        floor.setMaterial(floorMaterial);
        rootNode.attachChild(floor);
        /*
         * Configure mappings and listeners for keyboard input.
         */
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addListener(this, "left");
        inputManager.addListener(this, "right");
    }

    /**
     * Handle an input action from the user.
     *
     * @param name the name of the action
     * @param ongoing true&rarr;depress key, false&rarr;release key
     * @param ignored ignored
     */
    @Override
    public void onAction(String name, boolean ongoing, float ignored) {
        if (!ongoing) {
            return;
        }
        /*
         * Update the Y-axis rotation angle based on which key was pressed.
         */
        if (name.equals("left")) {
            angle += 0.1f; // radians
            System.out.print("rotate floor and sky leftward ...");
        } else if (name.equals("right")) {
            angle -= 0.1f; // radians
            System.out.printf("rotate floor and sky spatials rightward ...");
        } else {
            return;
        }
        /*
         * Update the local rotations of both objects based on the angle.
         */
        System.out.printf(" to %.1f radians left of start%n", angle);
        Quaternion rotation = new Quaternion();
        rotation.fromAngleNormalAxis(angle, Vector3f.UNIT_Y);
        floor.setLocalRotation(rotation);
        sky.setLocalRotation(rotation);
    }
}
