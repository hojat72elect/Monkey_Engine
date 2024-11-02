
package jme3test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.RectangleMesh;

/** A 3rd-person chase camera orbits a target (teapot). 
 * Follow the teapot with WASD keys, rotate by dragging the mouse. */
public class TestChaseCamera extends SimpleApplication implements AnalogListener, ActionListener {

  private Geometry teaGeom;

  public static void main(String[] args) {
    TestChaseCamera app = new TestChaseCamera();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    // Load a teapot model
    teaGeom = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
    Material mat_tea = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
    teaGeom.setMaterial(mat_tea);
    rootNode.attachChild(teaGeom);

    // Load a floor model
    Material mat_ground = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat_ground.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
    Geometry ground = new Geometry("ground", new RectangleMesh(
            new Vector3f(-25, -1, 25),
            new Vector3f(25, -1, 25),
            new Vector3f(-25, -1, -25)));
    ground.setMaterial(mat_ground);
    rootNode.attachChild(ground);
    
    // Disable the default first-person cam!
    flyCam.setEnabled(false);

    // Enable a chase cam
    ChaseCamera chaseCam = new ChaseCamera(cam, teaGeom, inputManager);

    //Uncomment this to invert the camera's vertical rotation Axis 
    //chaseCam.setInvertVerticalAxis(true);

    //Uncomment this to invert the camera's horizontal rotation Axis
    //chaseCam.setInvertHorizontalAxis(true);

    //Comment this to disable smooth camera motion
    chaseCam.setSmoothMotion(true);

    //Uncomment this to disable trailing of the camera 
    //WARNING, trailing only works with smooth motion enabled. It is true by default.
    //chaseCam.setTrailingEnabled(false);

    //Uncomment this to look 3 world units above the target
    //chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(3));

    //Uncomment this to enable rotation when the middle mouse button is pressed (like Blender)
    // WARNING: setting this trigger disables the rotation on right and left mouse button click
    //chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

    //Uncomment this to set multiple triggers to enable rotation of the cam
    // Here spacebar and middle mouse button
    //chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE),new KeyTrigger(KeyInput.KEY_SPACE));

    //registering inputs for target's movement
    registerInput();

  }

  public void registerInput() {
    inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("displayPosition", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft");
    inputManager.addListener(this, "displayPosition");
  }

  @Override
  public void onAnalog(String name, float value, float tpf) {
    if (name.equals("moveForward")) {
      teaGeom.move(0, 0, -5 * tpf);
    }
    if (name.equals("moveBackward")) {
      teaGeom.move(0, 0, 5 * tpf);
    }
    if (name.equals("moveRight")) {
      teaGeom.move(5 * tpf, 0, 0);
    }
    if (name.equals("moveLeft")) {
      teaGeom.move(-5 * tpf, 0, 0);

    }

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {
    if (name.equals("displayPosition") && keyPressed) {
      teaGeom.move(10, 10, 10);

    }
  }

  @Override
  public void simpleUpdate(float tpf) {
    super.simpleUpdate(tpf);

    //  teaGeom.move(new Vector3f(0.001f, 0, 0));
    // pivot.rotate(0, 0.00001f, 0);
    //   rootNode.updateGeometricState();
  }
//    public void update() {
//        super.update();
//// render the viewports
//        float tpf = timer.getTimePerFrame();
//        state.getRootNode().rotate(0, 0.000001f, 0);
//        stateManager.update(tpf);
//        stateManager.render(renderManager);
//        renderManager.render(tpf);
//    }
}
