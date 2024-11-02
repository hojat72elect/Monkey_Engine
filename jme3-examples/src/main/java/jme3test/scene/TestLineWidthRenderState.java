

package jme3test.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class TestLineWidthRenderState extends SimpleApplication {

    private Material mat;

    public static void main(String[] args){
        TestLineWidthRenderState app = new TestLineWidthRenderState();
        app.start();
    }



    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        cam.setLocation(new Vector3f(5.5826545f, 3.6192513f, 8.016988f));
        cam.setRotation(new Quaternion(-0.04787097f, 0.9463123f, -0.16569641f, -0.27339742f));

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(2);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if(name.equals("up") && isPressed){
                    mat.getAdditionalRenderState().setLineWidth(mat.getAdditionalRenderState().getLineWidth() + 1);
                }
                if(name.equals("down") && isPressed){
                    mat.getAdditionalRenderState().setLineWidth(Math.max(mat.getAdditionalRenderState().getLineWidth() - 1, 1));
                }
            }
        }, "up", "down");
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_J));
    }
}