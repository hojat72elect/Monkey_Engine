
package jme3test.animation;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Spline.SplineType;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;

public class TestCameraMotionPath extends SimpleApplication {

    private Spatial teapot;
    private boolean active = true;
    private boolean playing = false;
    private MotionPath path;
    private MotionEvent cameraMotionControl;
    private ChaseCamera chaser;
    private CameraNode camNode;

    public static void main(String[] args) {
        TestCameraMotionPath app = new TestCameraMotionPath();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        createScene();
        cam.setLocation(new Vector3f(8.4399185f, 11.189463f, 14.267577f));
        camNode = new CameraNode("Motion cam", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        camNode.setEnabled(false);
        path = new MotionPath();
        path.setCycle(true);
        path.addWayPoint(new Vector3f(20, 3, 0));
        path.addWayPoint(new Vector3f(0, 3, 20));
        path.addWayPoint(new Vector3f(-20, 3, 0));
        path.addWayPoint(new Vector3f(0, 3, -20));
        path.setCurveTension(0.83f);
        path.enableDebugShape(assetManager, rootNode);

        cameraMotionControl = new MotionEvent(camNode, path);
        cameraMotionControl.setLoopMode(LoopMode.Loop);
        //cameraMotionControl.setDuration(15f);
        cameraMotionControl.setLookAt(teapot.getWorldTranslation(), Vector3f.UNIT_Y);
        cameraMotionControl.setDirectionType(MotionEvent.Direction.LookAt);

        rootNode.attachChild(camNode);

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        final BitmapText wayPointsText = new BitmapText(guiFont);
        wayPointsText.setSize(guiFont.getCharSet().getRenderedSize());

        guiNode.attachChild(wayPointsText);

        path.addListener(new MotionPathListener() {

            @Override
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (path.getNbWayPoints() == wayPointIndex + 1) {
                    wayPointsText.setText(control.getSpatial().getName() + " Finish!!! ");
                } else {
                    wayPointsText.setText(control.getSpatial().getName() + " Reached way point " + wayPointIndex);
                }
                wayPointsText.setLocalTranslation((cam.getWidth() - wayPointsText.getLineWidth()) / 2, cam.getHeight(), 0);
            }
        });

        flyCam.setEnabled(false);
        chaser = new ChaseCamera(cam, teapot);
        chaser.registerWithInput(inputManager);
        chaser.setSmoothMotion(true);
        chaser.setMaxDistance(50);
        chaser.setDefaultDistance(50);
        initInputs();

    }

    private void createScene() {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 1f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));
        Material matSoil = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matSoil.setBoolean("UseMaterialColors", true);
        matSoil.setColor("Ambient", ColorRGBA.Gray);
        matSoil.setColor("Diffuse", ColorRGBA.Gray);
        matSoil.setColor("Specular", ColorRGBA.Black);
        teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot.setLocalScale(3);
        teapot.setMaterial(mat);



        rootNode.attachChild(teapot);
        Geometry soil = new Geometry("soil", new Box(50, 1, 50));
        soil.setLocalTranslation(0, -1, 0);
        soil.setMaterial(matSoil);
        rootNode.attachChild(soil);
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(0, -1, 0).normalizeLocal());
        light.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(light);
    }

    private void initInputs() {
        inputManager.addMapping("display_hidePath", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("SwitchPathInterpolation", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("tensionUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("tensionDown", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("display_hidePath") && keyPressed) {
                    if (active) {
                        active = false;
                        path.disableDebugShape();
                    } else {
                        active = true;
                        path.enableDebugShape(assetManager, rootNode);
                    }
                }
                if (name.equals("play_stop") && keyPressed) {
                    if (playing) {
                        playing = false;
                        cameraMotionControl.stop();
                        chaser.setEnabled(true);
                        camNode.setEnabled(false);
                    } else {
                        playing = true;
                        chaser.setEnabled(false);
                        camNode.setEnabled(true);
                        cameraMotionControl.play();
                    }
                }

                if (name.equals("SwitchPathInterpolation") && keyPressed) {
                    if (path.getPathSplineType() == SplineType.CatmullRom) {
                        path.setPathSplineType(SplineType.Linear);
                    } else {
                        path.setPathSplineType(SplineType.CatmullRom);
                    }
                }

                if (name.equals("tensionUp") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() + 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }
                if (name.equals("tensionDown") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() - 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }


            }
        };

        inputManager.addListener(acl, "display_hidePath", "play_stop", "SwitchPathInterpolation", "tensionUp", "tensionDown");

    }
}
