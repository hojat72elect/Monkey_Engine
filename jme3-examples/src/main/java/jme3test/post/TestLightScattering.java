

package jme3test.post;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

public class TestLightScattering extends SimpleApplication {

    public static void main(String[] args) {
        TestLightScattering app = new TestLightScattering();
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // put the camera in a bad position
        cam.setLocation(new Vector3f(55.35316f, -0.27061665f, 27.092093f));
        cam.setRotation(new Quaternion(0.010414706f, 0.9874893f, 0.13880467f, -0.07409228f));
//        cam.setDirection(new Vector3f(0,-0.5f,1.0f));
//        cam.setLocation(new Vector3f(0, 300, -500));
        //cam.setFrustumFar(1000);
        flyCam.setMoveSpeed(10);
        Material mat = assetManager.loadMaterial("Textures/Terrain/Rocky/Rocky.j3m");
        Spatial scene = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
        TangentBinormalGenerator.generate(((Geometry)((Node)scene).getChild(0)).getMesh());
        scene.setMaterial(mat);
        scene.setShadowMode(ShadowMode.CastAndReceive);
        scene.setLocalScale(400);
        scene.setLocalTranslation(0, -10, -120);

        rootNode.attachChild(scene);

        // load sky
        rootNode.attachChild(SkyFactory.createSky(assetManager,
                "Textures/Sky/Bright/FullskiesBlueClear03.dds", 
                SkyFactory.EnvMapType.CubeMap));

        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-0.12f, -0.3729129f, 0.74847335f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        scene.addLight(sun);


        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        int numSamples = getContext().getSettings().getSamples();
        if (numSamples > 0) {
            fpp.setNumSamples(numSamples);
        }
        Vector3f lightPos = lightDir.multLocal(-3000);
        LightScatteringFilter filter = new LightScatteringFilter(lightPos);
        LightScatteringUI ui = new LightScatteringUI(inputManager, filter);
        fpp.addFilter(filter);
        viewPort.addProcessor(fpp);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
}
