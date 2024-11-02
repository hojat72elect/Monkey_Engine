
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Simple application to test split-screen rendering. Clicking with LMB toggles
 * between a single camera/viewport (cam) and split screen (leftCam plus
 * rightCam). See issue #357.
 */
public class TestSplitScreen extends SimpleApplication implements ActionListener {

    private boolean splitScreen = false;
    final private Box mesh = new Box(1f, 1f, 1f);
    final private Node leftScene = new Node("left scene");
    private ViewPort leftView, rightView;

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);

        Geometry blueBox = new Geometry("blue box", mesh);
        Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blueMat.setColor("Color", ColorRGBA.Blue);
        blueBox.setMaterial(blueMat);
        rootNode.attachChild(blueBox);

        Camera rightCam = cam.clone();
        rightCam.setViewPort(0.5f, 1f, 0f, 1f);

        rightView = renderManager.createMainView("right", rightCam);
        rightView.setClearFlags(true, true, true);
        rightView.setEnabled(false);
        rightView.attachScene(rootNode);

        Geometry redBox = new Geometry("red box", mesh);
        Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.setColor("Color", ColorRGBA.Red);
        redBox.setMaterial(redMat);
        leftScene.attachChild(redBox);

        Camera leftCam = cam.clone();
        leftCam.setViewPort(0f, 0.5f, 0f, 1f);

        leftView = renderManager.createMainView("left", leftCam);
        leftView.setClearFlags(true, true, true);
        leftView.setEnabled(false);
        leftView.attachScene(leftScene);

        inputManager.addMapping("lmb", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "lmb");
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("lmb") && !keyPressed) {
            splitScreen = !splitScreen;
            viewPort.setEnabled(!splitScreen);
            leftView.setEnabled(splitScreen);
            rightView.setEnabled(splitScreen);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        leftScene.updateLogicalState(tpf);
        leftScene.updateGeometricState();
    }

    public static void main(String[] args) {
        TestSplitScreen app = new TestSplitScreen();
        app.start();
    }
}
