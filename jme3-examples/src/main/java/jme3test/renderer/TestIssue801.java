
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class TestIssue801 extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings initialSettings = new AppSettings(true);
        initialSettings.setBitsPerPixel(24);

        TestIssue801 app = new TestIssue801();
        app.setSettings(initialSettings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1f));

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        inputManager.addMapping("changeBpp", new KeyTrigger(KeyInput.KEY_P));
        ActionListener listener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("changeBpp") && keyPressed) {
                    goWindowed();
                }
            }
        };
        inputManager.addListener(listener, "changeBpp");
    }

    void goWindowed() {
        AppSettings newSettings = new AppSettings(false);
        newSettings.copyFrom(settings);
        newSettings.setBitsPerPixel(16);

        setSettings(newSettings);
        restart();
    }
}
