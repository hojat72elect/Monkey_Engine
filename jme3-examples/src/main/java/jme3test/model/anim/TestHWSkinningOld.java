
package jme3test.model.anim;

import com.jme3.animation.*;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class TestHWSkinningOld extends SimpleApplication implements ActionListener {

    final private String[] animNames = {"Dodge", "Walk", "pull", "push"};
    private final static int SIZE = 50;
    private boolean hwSkinningEnable = true;
    final private List<SkeletonControl> skControls = new ArrayList<>();
    private BitmapText hwsText;

    public static void main(String[] args) {
        TestHWSkinningOld app = new TestHWSkinningOld();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
        setPauseOnLostFocus(false);
        cam.setLocation(new Vector3f(24.746134f, 13.081396f, 32.72753f));
        cam.setRotation(new Quaternion(-0.06867662f, 0.92435044f, -0.19981281f, -0.31770203f));
        makeHudText();

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Spatial model = assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
                model.setLocalScale(0.1f);
                model.setLocalTranslation(i - SIZE / 2, 0, j - SIZE / 2);
                AnimControl control = model.getControl(AnimControl.class);
                AnimChannel channel = control.createChannel();
                channel.setAnim(animNames[(i + j) % 4]);
                SkeletonControl skeletonControl = model.getControl(SkeletonControl.class);
                skeletonControl.setHardwareSkinningPreferred(hwSkinningEnable);
                skControls.add(skeletonControl);
                rootNode.attachChild(model);
            }
        }

        inputManager.addListener(this, "toggleHWS");
        inputManager.addMapping("toggleHWS", new KeyTrigger(KeyInput.KEY_SPACE));
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed && name.equals("toggleHWS")) {
            hwSkinningEnable = !hwSkinningEnable;
            for (SkeletonControl control : skControls) {
                control.setHardwareSkinningPreferred(hwSkinningEnable);
                hwsText.setText("HWS : " + hwSkinningEnable);
            }
        }
    }

    private void makeHudText() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        hwsText = new BitmapText(guiFont);
        hwsText.setSize(guiFont.getCharSet().getRenderedSize());
        hwsText.setText("HWS : " + hwSkinningEnable);
        hwsText.setLocalTranslation(0, cam.getHeight(), 0);
        guiNode.attachChild(hwsText);
    }
}