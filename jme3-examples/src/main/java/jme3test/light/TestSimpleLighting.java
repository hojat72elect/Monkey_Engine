

package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.MaterialDebugAppState;
import com.jme3.util.TangentBinormalGenerator;

public class TestSimpleLighting extends SimpleApplication {

    private float angle;
    private PointLight pl;
    private Geometry lightMdl;

    public static void main(String[] args){
        TestSimpleLighting app = new TestSimpleLighting();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Geometry teapot = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
        TangentBinormalGenerator.generate(teapot.getMesh(), true);

        teapot.setLocalScale(2f);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        mat.selectTechnique("GBuf");
        mat.setFloat("Shininess", 25);
        mat.setBoolean("UseMaterialColors", true);
        cam.setLocation(new Vector3f(0.015041917f, 0.4572918f, 5.2874837f));
        cam.setRotation(new Quaternion(-1.8875003E-4f, 0.99882424f, 0.04832061f, 0.0039016632f));

//        mat.setTexture("ColorRamp", assetManager.loadTexture("Textures/ColorRamp/cloudy.png"));
//
//        mat.setBoolean("VTangent", true);
//        mat.setBoolean("Minnaert", true);
//        mat.setBoolean("WardIso", true);
//        mat.setBoolean("VertexLighting", true);
//        mat.setBoolean("LowQuality", true);
//        mat.setBoolean("HighQuality", true);

        mat.setColor("Ambient",  ColorRGBA.Black);
        mat.setColor("Diffuse",  ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.Gray);
        
        teapot.setMaterial(mat);
        rootNode.attachChild(teapot);

        lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.getMesh().setStatic();
        rootNode.attachChild(lightMdl);

        pl = new PointLight();
        pl.setColor(ColorRGBA.White);
        pl.setRadius(4f);
        rootNode.addLight(pl);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        dl.setColor(ColorRGBA.Green);
        rootNode.addLight(dl);
        
        
        MaterialDebugAppState debug = new MaterialDebugAppState();
        debug.registerBinding("Common/ShaderLib/BlinnPhongLighting.glsllib", teapot);
        stateManager.attach(debug);
        setPauseOnLostFocus(false);
        flyCam.setDragToRotate(true);
        
    }

    @Override
    public void simpleUpdate(float tpf){
//        cam.setLocation(new Vector3f(2.0632997f, 1.9493936f, 2.6885238f));
//        cam.setRotation(new Quaternion(-0.053555284f, 0.9407851f, -0.17754152f, -0.28378546f));

        angle += tpf;
        angle %= FastMath.TWO_PI;
        
        pl.setPosition(new Vector3f(FastMath.cos(angle) * 2f, 0.5f, FastMath.sin(angle) * 2f));
        lightMdl.setLocalTranslation(pl.getPosition());
    }

}
