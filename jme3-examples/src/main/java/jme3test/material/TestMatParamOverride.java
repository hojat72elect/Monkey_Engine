
package jme3test.material;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.MatParamOverride;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;

/**
 * Test if {@link MatParamOverride}s are working correctly.
 *
 * @author Kirill Vainer
 */
public class TestMatParamOverride extends SimpleApplication {

    final private Box box = new Box(1, 1, 1);
    final MatParamOverride overrideYellow
            = new MatParamOverride(VarType.Vector4, "Color",
                    ColorRGBA.Yellow);
    final MatParamOverride overrideWhite
            = new MatParamOverride(VarType.Vector4, "Color",
                    Vector4f.UNIT_XYZW);
    final MatParamOverride overrideGray
            = new MatParamOverride(VarType.Vector4, "Color",
                    new Quaternion(0.5f, 0.5f, 0.5f, 1f));

    public static void main(String[] args) {
        TestMatParamOverride app = new TestMatParamOverride();
        app.start();
    }

    private void createBox(float location, ColorRGBA color) {
        Geometry geom = new Geometry("Box", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.move(location, 0, 0);
        rootNode.attachChild(geom);
    }

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);

        createBox(-3, ColorRGBA.Red);
        createBox(0, ColorRGBA.Green);
        createBox(3, ColorRGBA.Blue);

        System.out.println("Press G, W, Y, or space bar ...");
        inputManager.addMapping("overrideClear", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("overrideGray", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("overrideWhite", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("overrideYellow", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    if (name.equals("overrideClear")) {
                        rootNode.clearMatParamOverrides();
                    } else if (name.equals("overrideGray")) {
                        rootNode.clearMatParamOverrides();
                        rootNode.addMatParamOverride(overrideGray);
                    } else if (name.equals("overrideWhite")) {
                        rootNode.clearMatParamOverrides();
                        rootNode.addMatParamOverride(overrideWhite);
                    } else if (name.equals("overrideYellow")) {
                        rootNode.clearMatParamOverrides();
                        rootNode.addMatParamOverride(overrideYellow);
                    }
                    System.out.println(rootNode.getLocalMatParamOverrides());
                }
            }
        }, "overrideClear", "overrideGray", "overrideWhite", "overrideYellow");
    }
}
