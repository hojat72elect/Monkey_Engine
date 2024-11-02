
package jme3test.model.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class TestHWSkinning extends SimpleApplication implements ActionListener{


    // private AnimComposer composer;
    final private String[] animNames = {"Dodge", "Walk", "pull", "push"};
    private final static int SIZE = 40;
    private boolean hwSkinningEnable = true;
    final private List<SkinningControl> skControls = new ArrayList<>();
    private BitmapText hwsText;

    public static void main(String[] args) {
        TestHWSkinning app = new TestHWSkinning();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
        setPauseOnLostFocus(false);
        cam.setLocation(new Vector3f(38.76639f, 14.744472f, 45.097454f));
        cam.setRotation(new Quaternion(-0.06086266f, 0.92303723f, -0.1639443f, -0.34266636f));

        makeHudText();
 
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

        Spatial models[] = new Spatial[4];
        for (int i = 0; i < 4; i++) {
            models[i] =loadModel(i);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Node model = (Node)models[(i + j) % 4];
                Spatial s = model.getChild(0).clone();
                model.attachChild(s);
                float x = (i - SIZE / 2) / 0.1f;
                float z = (j - SIZE / 2) / 0.1f;
                s.setLocalTranslation(x, 0, z);
            }
        }

        inputManager.addListener(this, "toggleHWS");
        inputManager.addMapping("toggleHWS", new KeyTrigger(KeyInput.KEY_SPACE));

    }

    private Spatial loadModel(int i) {
        Spatial model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        model.setLocalScale(0.1f);
        AnimComposer composer = model.getControl(AnimComposer.class);

        composer.setCurrentAction(animNames[i]);
        SkinningControl skinningControl = model.getControl(SkinningControl.class);
        skinningControl.setHardwareSkinningPreferred(hwSkinningEnable);
        skControls.add(skinningControl);
        rootNode.attachChild(model);
        return model;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(isPressed && name.equals("toggleHWS")){
            hwSkinningEnable = !hwSkinningEnable;
            for (SkinningControl control : skControls) {
                control.setHardwareSkinningPreferred(hwSkinningEnable);
                hwsText.setText("HWS : "+ hwSkinningEnable);
            }
        }
    }

    private void makeHudText() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        hwsText = new BitmapText(guiFont);
        hwsText.setSize(guiFont.getCharSet().getRenderedSize());
        hwsText.setText("HWS : "+ hwSkinningEnable);
        hwsText.setLocalTranslation(0, cam.getHeight(), 0);
        guiNode.attachChild(hwsText);
    }
}
