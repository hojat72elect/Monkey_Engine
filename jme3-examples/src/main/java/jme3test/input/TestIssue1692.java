
package jme3test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * A test for issue https://github.com/jMonkeyEngine/jmonkeyengine/pull/1692.
 * We are testing to see if disabling then re-enabling the chase camera keeps the correct flags
 * set so that we can still rotate without dragging
 */
public class TestIssue1692 extends SimpleApplication implements ActionListener {

    private ChaseCamera chaseCam;
    private BitmapText cameraStatus;

    public static void main(String[] args) {
        TestIssue1692 app = new TestIssue1692();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Load a teapot model, we will chase this with the camera
        Geometry teaGeom = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
        Material teapotMaterial = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teaGeom.setMaterial(teapotMaterial);
        rootNode.attachChild(teaGeom);

        // Load a floor model
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMaterial.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Geometry ground = new Geometry("ground", new Quad(50, 50));
        ground.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        ground.setLocalTranslation(-25, -1, 25);
        ground.setMaterial(floorMaterial);
        rootNode.attachChild(ground);

        // Disable the default first-person cam!
        flyCam.setEnabled(false);

        // Enable a chase cam
        chaseCam = new ChaseCamera(cam, teaGeom, inputManager);
        /*
         * Explicitly set drag to rotate to false.
         * We are testing to see if disabling then re-enabling the camera keeps the correct flags
         * set so that we can still rotate without dragging.
         */
        chaseCam.setDragToRotate(false);

        // Show instructions
        int yTop = settings.getHeight();
        int size = guiFont.getCharSet().getRenderedSize();
        BitmapText hudText = new BitmapText(guiFont);
        hudText.setSize(size);
        hudText.setColor(ColorRGBA.Blue);
        hudText.setText("This test is for issue 1692.\n"
                + "We are testing to see if drag to rotate stays disabled"
                + "after disabling and re-enabling the chase camera.\n"
                + "For this test, use the SPACE key to disable and re-enable the camera.");
        hudText.setLocalTranslation(0, yTop - (hudText.getLineHeight() * 3), 0);
        guiNode.attachChild(hudText);

        // Show camera status
        cameraStatus = new BitmapText(guiFont);
        cameraStatus.setSize(size);
        cameraStatus.setColor(ColorRGBA.Blue);
        cameraStatus.setLocalTranslation(0, yTop - cameraStatus.getLineHeight(), 0); // position
        guiNode.attachChild(cameraStatus);

        // Register inputs
        registerInput();
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Update chaseCam status
        cameraStatus.setText("chaseCam " + (chaseCam.isEnabled() ? "enabled" : "disabled"));
    }

    private void registerInput() {
        inputManager.addMapping("toggleCamera", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "toggleCamera");
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("toggleCamera") && keyPressed) {
            // Toggle chase camera
            chaseCam.setEnabled(!chaseCam.isEnabled());
        }
    }
}
