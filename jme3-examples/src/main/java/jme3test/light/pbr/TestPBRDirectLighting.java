
package jme3test.light.pbr;

import com.jme3.app.ChaseCameraAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.scene.shape.Sphere;

/**
 * A test case for PBR lighting.
 * Still experimental.
 *
 * @author nehon
 */
public class TestPBRDirectLighting extends SimpleApplication {

    public static void main(String[] args) {
        TestPBRDirectLighting app = new TestPBRDirectLighting();
        app.start();
    }

    private DirectionalLight dl;

    private float roughness = 0.0f;

    @Override
    public void simpleInitApp() {


        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        rootNode.addLight(dl);
        dl.setColor(ColorRGBA.White);

        ChaseCameraAppState chaser = new ChaseCameraAppState();
        chaser.setDragToRotate(true);
        chaser.setMinVerticalRotation(-FastMath.HALF_PI);
        chaser.setMaxDistance(1000);
        chaser.setInvertVerticalAxis(true);
        getStateManager().attach(chaser);
        chaser.setTarget(rootNode);
        flyCam.setEnabled(false);

        Geometry sphere = new Geometry("sphere", new Sphere(32, 32, 1));
        final Material m = new Material(assetManager, "Common/MatDefs/Light/PBRLighting.j3md");
        m.setColor("BaseColor", ColorRGBA.Black);
        m.setFloat("Metallic", 0f);
        m.setFloat("Roughness", roughness);
        sphere.setMaterial(m);
        rootNode.attachChild(sphere);

        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (name.equals("rup") && isPressed) {
                    roughness = FastMath.clamp(roughness + 0.1f, 0.0f, 1.0f);
                    m.setFloat("Roughness", roughness);
                }
                if (name.equals("rdown") && isPressed) {
                    roughness = FastMath.clamp(roughness - 0.1f, 0.0f, 1.0f);
                    m.setFloat("Roughness", roughness);
                }

                if (name.equals("light") && isPressed) {
                    dl.setDirection(cam.getDirection().normalize());
                }
            }
        }, "light", "rup", "rdown");


        inputManager.addMapping("light", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("rup", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("rdown", new KeyTrigger(KeyInput.KEY_DOWN));


    }

    @Override
    public void simpleUpdate(float tpf) {
    }

}

