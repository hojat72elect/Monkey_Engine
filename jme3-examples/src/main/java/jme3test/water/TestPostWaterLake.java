
package jme3test.water;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import java.io.File;

public class TestPostWaterLake extends SimpleApplication {

    private static boolean useHttp = false;

    public static void main(String[] args) {         
        TestPostWaterLake app = new TestPostWaterLake();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        File file = new File("wildhouse.zip");
        if (!file.exists()) {
            useHttp = true;
        }
        
        this.flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(-27.0f, 1.0f, 75.0f));
      //  cam.setRotation(new Quaternion(0.03f, 0.9f, 0f, 0.4f));

        // load sky
        rootNode.attachChild(SkyFactory.createSky(assetManager, 
                "Textures/Sky/Bright/BrightSky.dds", 
                SkyFactory.EnvMapType.CubeMap));
        
        // create the geometry and attach it
        // load the level from zip or http zip
        if (useHttp) {
            assetManager.registerLocator(
                    "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/wildhouse.zip", 
                    HttpZipLocator.class);
        } else {
            assetManager.registerLocator("wildhouse.zip", ZipLocator.class);
        }
        Spatial scene = assetManager.loadModel("main.scene");
        rootNode.attachChild(scene);

        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        scene.addLight(sun);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);        
        final WaterFilter water = new WaterFilter(rootNode, lightDir);
        water.setWaterHeight(-20);
        water.setUseFoam(false);
        water.setUseRipples(false);
        water.setDeepWaterColor(ColorRGBA.Brown);
        water.setWaterColor(ColorRGBA.Brown.mult(2.0f));
        water.setWaterTransparency(0.2f);
        water.setMaxAmplitude(0.3f);
        water.setWaveScale(0.008f);
        water.setSpeed(0.7f);
        water.setShoreHardness(1.0f);
        water.setRefractionConstant(0.2f);
        water.setShininess(0.3f);
        water.setSunScale(1.0f);
        water.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);

        inputManager.addListener(new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
              if(isPressed){
                  if(water.isUseHQShoreline()){
                      water.setUseHQShoreline(false);
                  }else{
                      water.setUseHQShoreline(true);
                  }
              }
            }
        }, "HQ");

        inputManager.addMapping("HQ", new KeyTrigger(KeyInput.KEY_SPACE));
    }
}
