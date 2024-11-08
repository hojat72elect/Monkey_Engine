
package jme3test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.RectangleMesh;
import com.jme3.system.AppSettings;

/**
 * A 3rd-person camera node follows a target (teapot).  Follow the teapot with
 * WASD keys, rotate by dragging the mouse.
 */
public class TestCameraNode extends SimpleApplication implements AnalogListener, ActionListener {

  private Node teaNode;
  private boolean rotate = false;
  final private Vector3f direction = new Vector3f();

  public static void main(String[] args) {
    TestCameraNode app = new TestCameraNode();
    AppSettings s = new AppSettings(true);
    s.setFrameRate(100);
    app.setSettings(s);
    app.start();
  }

  @Override
  public void simpleInitApp() {
    // load a teapot model 
    Geometry teaGeom 
            = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
    teaGeom.setMaterial(mat);
    //create a node to attach the geometry and the camera node
    teaNode = new Node("teaNode");
    teaNode.attachChild(teaGeom);
    rootNode.attachChild(teaNode);
    // create a floor
    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
    Geometry ground = new Geometry("ground", new RectangleMesh(
            new Vector3f(-25, -1, 25),
            new Vector3f(25, -1, 25),
            new Vector3f(-25, -1, -25)));
    ground.setMaterial(mat);
    rootNode.attachChild(ground);

    //creating the camera Node
    CameraNode camNode = new CameraNode("CamNode", cam);
    // Set the direction to SpatialToCamera, which means the camera will copy the movements of the Node.
    camNode.setControlDir(ControlDirection.SpatialToCamera);
    //attaching the camNode to the teaNode
    teaNode.attachChild(camNode);
    //setting the local translation of the cam node to move it away from the tea Node a bit
    camNode.setLocalTranslation(new Vector3f(-10, 0, 0));
    //setting the camNode to look at the teaNode
    camNode.lookAt(teaNode.getLocalTranslation(), Vector3f.UNIT_Y);

    //disable the default 1st-person flyCam (don't forget this!!)
    flyCam.setEnabled(false);

    registerInput();
  }

  public void registerInput() {
    inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("toggleRotate", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft");
    inputManager.addListener(this, "rotateRight", "rotateLeft", "toggleRotate");
  }

  @Override
  public void onAnalog(String name, float value, float tpf) {
    //computing the normalized direction of the cam to move the teaNode
    direction.set(cam.getDirection()).normalizeLocal();
    if (name.equals("moveForward")) {
      direction.multLocal(5 * tpf);
      teaNode.move(direction);
    }
    if (name.equals("moveBackward")) {
      direction.multLocal(-5 * tpf);
      teaNode.move(direction);
    }
    if (name.equals("moveRight")) {
      direction.crossLocal(Vector3f.UNIT_Y).multLocal(5 * tpf);
      teaNode.move(direction);
    }
    if (name.equals("moveLeft")) {
      direction.crossLocal(Vector3f.UNIT_Y).multLocal(-5 * tpf);
      teaNode.move(direction);
    }
    if (name.equals("rotateRight") && rotate) {
      teaNode.rotate(0, 5 * tpf, 0);
    }
    if (name.equals("rotateLeft") && rotate) {
      teaNode.rotate(0, -5 * tpf, 0);
    }

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {
    //toggling rotation on or off
    if (name.equals("toggleRotate") && keyPressed) {
      rotate = true;
      inputManager.setCursorVisible(false);
    }
    if (name.equals("toggleRotate") && !keyPressed) {
      rotate = false;
      inputManager.setCursorVisible(true);
    }

  }
}
