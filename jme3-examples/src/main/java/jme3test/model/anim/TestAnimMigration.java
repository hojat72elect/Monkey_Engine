
package jme3test.model.anim;

import com.jme3.anim.*;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.*;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.ChaseCameraAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.custom.ArmatureDebugAppState;

import java.util.LinkedList;

/**
 * Created by Nehon on 18/12/2017.
 */
public class TestAnimMigration extends SimpleApplication {

    private ArmatureDebugAppState debugAppState;
    private AnimComposer composer;
    final private LinkedList<String> anims = new LinkedList<>();
    private boolean playAnim = false;
    private BlendAction action;
    private float blendValue = 1f;

    public static void main(String... argv) {
        TestAnimMigration app = new TestAnimMigration();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setTimer(new EraseTimer());
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.1f, 100f);
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        rootNode.addLight(new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal()));
        rootNode.addLight(new AmbientLight(ColorRGBA.DarkGray));

        Spatial model = assetManager.loadModel("Models/Jaime/Jaime.j3o");

        AnimMigrationUtils.migrate(model);

        rootNode.attachChild(model);


        debugAppState = new ArmatureDebugAppState();
        stateManager.attach(debugAppState);

        setupModel(model);

        flyCam.setEnabled(false);

        Node target = new Node("CamTarget");

        target.move(0, 1, 0);
        ChaseCameraAppState chaseCam = new ChaseCameraAppState();
        chaseCam.setTarget(target);
        getStateManager().attach(chaseCam);
        chaseCam.setInvertHorizontalAxis(true);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setZoomSpeed(0.5f);
        chaseCam.setMinVerticalRotation(-FastMath.HALF_PI);
        chaseCam.setRotationSpeed(3);
        chaseCam.setDefaultDistance(3);
        chaseCam.setMinDistance(0.01f);
        chaseCam.setZoomSpeed(0.01f);
        chaseCam.setDefaultVerticalRotation(0.3f);

        initInputs();
    }

    public void initInputs() {
        inputManager.addMapping("toggleAnim", new KeyTrigger(KeyInput.KEY_RETURN));

        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    playAnim = !playAnim;
                    if (playAnim) {
                        String anim = anims.poll();
                        anims.add(anim);
                        composer.setCurrentAction(anim);
                        System.err.println(anim);
                    } else {
                        composer.reset();
                    }
                }
            }
        }, "toggleAnim");
        inputManager.addMapping("nextAnim", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed && composer != null) {
                    String anim = anims.poll();
                    anims.add(anim);
                    composer.setCurrentAction(anim);
                    System.err.println(anim);
                }
            }
        }, "nextAnim");
        inputManager.addMapping("toggleArmature", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    debugAppState.setEnabled(!debugAppState.isEnabled());
                }
            }
        }, "toggleArmature");

        inputManager.addMapping("mask", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    ((BlendableAction)composer.setCurrentAction("Wave", "LeftArm", false)).setMaxTransitionWeight(0.9);
                }
            }

        }, "mask");

        inputManager.addMapping("blendUp", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("blendDown", new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                if (name.equals("blendUp")) {
                    blendValue += value;
                    blendValue = FastMath.clamp(blendValue, 1, 4);
                    action.getBlendSpace().setValue(blendValue);
                    //action.setSpeed(blendValue);
                }
                if (name.equals("blendDown")) {
                    blendValue -= value;
                    blendValue = FastMath.clamp(blendValue, 1, 4);
                    action.getBlendSpace().setValue(blendValue);
                    //action.setSpeed(blendValue);
                }
                //System.err.println(blendValue);
            }
        }, "blendUp", "blendDown");

        inputManager.addMapping("maxTransitionWeightInc", new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("maxTransitionWeightDec", new KeyTrigger(KeyInput.KEY_SUBTRACT));

        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                if (name.equals("maxTransitionWeightInc")) {
                    Action action = composer.getCurrentAction();
                    if (action instanceof BlendableAction) {
                        BlendableAction ba = (BlendableAction) action;
                        ba.setMaxTransitionWeight(Math.min(ba.getMaxTransitionWeight() + 0.01, 1.0));
                        System.out.println("MaxTransitionWeight=" + ba.getMaxTransitionWeight());
                    }
                }
                if (name.equals("maxTransitionWeightDec")) {
                    Action action = composer.getCurrentAction();
                    if (action instanceof BlendableAction) {
                        BlendableAction ba = (BlendableAction) action;
                        ba.setMaxTransitionWeight(Math.max(ba.getMaxTransitionWeight() - 0.01, 0.0));
                        System.out.println("MaxTransitionWeight=" + ba.getMaxTransitionWeight());
                    }
                }
                //System.err.println(blendValue);
            }
        }, "maxTransitionWeightInc", "maxTransitionWeightDec");
    }

    private void setupModel(Spatial model) {
        if (composer != null) {
            return;
        }
        composer = model.getControl(AnimComposer.class);
        if (composer != null) {

            SkinningControl sc = model.getControl(SkinningControl.class);
            debugAppState.addArmatureFrom(sc);

            anims.clear();
            for (String name : composer.getAnimClipsNames()) {
                anims.add(name);
            }
            composer.actionSequence("Sequence1",
                    composer.makeAction("Walk"),
                    composer.makeAction("Run"),
                    composer.makeAction("Jumping")).setSpeed(1);

            composer.actionSequence("Sequence2",
                    composer.makeAction("Walk"),
                    composer.makeAction("Run"),
                    composer.makeAction("Jumping")).setSpeed(-1);

            action = composer.actionBlended("Blend", new LinearBlendSpace(1, 4),
                    "Walk", "Run");

            action.getBlendSpace().setValue(1);

            composer.action("Walk").setSpeed(-1);

            composer.addAction("WalkCycle", new BaseAction(Tweens.cycle(composer.makeAction("Walk"))));

            composer.makeLayer("LeftArm", ArmatureMask.createMask(sc.getArmature(), "shoulder.L"));

            anims.addFirst("WalkCycle");
            anims.addFirst("Blend");
            anims.addFirst("Sequence2");
            anims.addFirst("Sequence1");

            if (anims.isEmpty()) {
                return;
            }
            if (playAnim) {
                String anim = anims.poll();
                anims.add(anim);
                composer.setCurrentAction(anim);
                System.err.println(anim);
            }

        } else {
            if (model instanceof Node) {
                Node n = (Node) model;
                for (Spatial child : n.getChildren()) {
                    setupModel(child);
                }
            }
        }

    }
}
