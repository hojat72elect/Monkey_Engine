
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * Tests the setting of the FOV and aspect ratios.
 *
 * @author Markil 3
 */
public class TestAspectFov extends SimpleApplication implements AnalogListener {
    private static final String FOV_IN = "fovIn";
    private static final String FOV_OUT = "fovOut";
    private BitmapText header, fov;

    public static void main(String[] args) {
        new TestAspectFov().start();
    }

    @Override
    public void simpleInitApp() {
        header = new BitmapText(this.guiFont);
        header.setText("Adjust FOV with R/F or with mouse scroll");
        guiNode.attachChild(header);
        fov = new BitmapText(this.guiFont);
        guiNode.attachChild(fov);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);

        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);

        assetManager.registerLocator("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/town.zip",
                HttpZipLocator.class);
        Spatial sceneModel = assetManager.loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        rootNode.attachChild(sceneModel);

        inputManager.addMapping(FOV_IN, new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping(FOV_OUT, new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, FOV_IN, FOV_OUT);
    }

    @Override
    public void update() {
        /*
         * Updates the labels
         */
        TempVars vars = TempVars.get();
        super.update();
        header.setLocalTranslation(0, cam.getHeight(), 0);
        vars.vect1.set(header.getLocalTranslation());
        vars.vect1.subtractLocal(0, header.getLineHeight(), 0);
        fov.setLocalTranslation(vars.vect1);
        fov.setText("FOV: " + cam.getFov());
        vars.vect1.subtractLocal(0, fov.getLineHeight(), 0);
        vars.release();
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        final float CHANGE_VALUE = tpf * 10;
        float newFov = cam.getFov();
        switch (name) {
            case FOV_IN:
                newFov -= CHANGE_VALUE;
                break;
            case FOV_OUT:
                newFov += CHANGE_VALUE;
                break;
        }
        if (newFov > 0 && newFov != cam.getFov()) {
            cam.setFov(newFov);
        }
    }
}
