

package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class TestParallelProjection  extends SimpleApplication implements AnalogListener {

    private float frustumSize = 1;

    public static void main(String[] args){
        TestParallelProjection app = new TestParallelProjection();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Geometry teaGeom = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(Vector3f.UNIT_XYZ.negate());

        rootNode.addLight(dl);
        rootNode.attachChild(teaGeom);

        // Setup first view
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);

        inputManager.addListener(this, "Size+", "Size-");
        inputManager.addMapping("Size+", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Size-", new KeyTrigger(KeyInput.KEY_S));
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        // Instead of moving closer/farther to object, we zoom in/out.
        if (name.equals("Size-"))
            frustumSize += 0.3f * tpf;
        else
            frustumSize -= 0.3f * tpf;

        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
    }
}
