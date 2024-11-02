
package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.shadow.SpotLightShadowFilter;
import com.jme3.shadow.SpotLightShadowRenderer;

public class TestPointDirectionalAndSpotLightShadows extends SimpleApplication {
    public static final int SHADOWMAP_SIZE = 512;

    public static void main(String[] args) {
        TestPointDirectionalAndSpotLightShadows app = new TestPointDirectionalAndSpotLightShadows();
        app.start();
    }
    private Node lightNode;
    private SpotLight spotLight;
    
    final private boolean useFilter = false;
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0.040581334f, 1.7745866f, 6.155161f));
        cam.setRotation(new Quaternion(4.3868728E-5f, 0.9999293f, -0.011230096f, 0.0039059948f));


        Node scene = (Node) assetManager.loadModel("Models/Test/CornellBox.j3o");
        scene.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);
        rootNode.getChild("Cube").setShadowMode(RenderQueue.ShadowMode.Receive);
        lightNode = (Node) rootNode.getChild("Lamp");
        Geometry lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        //Geometry  lightMdl = new Geometry("Light", new Box(.1f,.1f,.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.setShadowMode(RenderQueue.ShadowMode.Off);
        lightNode.attachChild(lightMdl);
        //lightMdl.setLocalTranslation(lightNode.getLocalTranslation());


        Geometry box = new Geometry("box", new Box(0.2f, 0.2f, 0.2f));
        //Geometry  lightMdl = new Geometry("Light", new Box(.1f,.1f,.1f));
        box.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        box.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(box);
        box.setLocalTranslation(-1f, 0.5f, -2);

        scene.getLocalLightList().get(0).setColor(ColorRGBA.Red);

        PointLightShadowRenderer plsr
                = new PointLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        plsr.setLight((PointLight) scene.getLocalLightList().get(0));
        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);

        PointLightShadowFilter plsf
                = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        plsf.setLight((PointLight) scene.getLocalLightList().get(0));     
        plsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        plsf.setEnabled(useFilter);

        //DIRECTIONAL LIGHT
        DirectionalLight directionalLight = new DirectionalLight();
        rootNode.addLight(directionalLight);
        directionalLight.setColor(ColorRGBA.Blue);
        directionalLight.setDirection(new Vector3f(-1f, -.2f, 0f));

        DirectionalLightShadowRenderer dlsr
                = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE*2, 4);
        dlsr.setLight(directionalLight);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);

        DirectionalLightShadowFilter dlsf
                = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE*2, 4);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        dlsf.setLight(directionalLight);        
        dlsf.setEnabled(useFilter);
        
        //SPOT LIGHT
        spotLight = new SpotLight();
        spotLight.setDirection(new Vector3f(1f,-1f,0f));
        spotLight.setPosition(new Vector3f(-1f,3f,0f));
        spotLight.setSpotOuterAngle(0.5f);
        spotLight.setColor(ColorRGBA.Green);
        Sphere sphere = new Sphere(8, 8, .1f);
        Geometry sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setLocalTranslation(-1f, 3f, 0f);
        sphereGeometry.setMaterial(assetManager.loadMaterial("Common/Materials/WhiteColor.j3m"));
        rootNode.attachChild(sphereGeometry);
        rootNode.addLight(spotLight);

        SpotLightShadowRenderer slsr
                = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        slsr.setLight(spotLight);
        slsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);

        SpotLightShadowFilter slsf
                = new SpotLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        slsf.setLight(spotLight);
        slsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        slsf.setEnabled(useFilter);
        
        
        
        if (!useFilter)viewPort.addProcessor(slsr);
        if (!useFilter)viewPort.addProcessor(plsr);
        if (!useFilter)viewPort.addProcessor(dlsr);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(plsf);
        fpp.addFilter(dlsf);
        fpp.addFilter(slsf);
        viewPort.addProcessor(fpp);
              
        ShadowTestUIManager uiMan = new ShadowTestUIManager(assetManager, plsr, plsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManPls = new ShadowTestUIManager(assetManager, plsr, plsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManDls = new ShadowTestUIManager(assetManager, dlsr, dlsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManSls = new ShadowTestUIManager(assetManager, slsr, slsf, guiNode, inputManager, viewPort);
  
    }

    private float timeElapsed = 0.0f;
    @Override
    public void simpleUpdate(float tpf) {
        timeElapsed += tpf;
       lightNode.setLocalTranslation(FastMath.cos(timeElapsed), lightNode.getLocalTranslation().y, FastMath.sin(timeElapsed));
       spotLight.setDirection(new Vector3f(FastMath.cos(-timeElapsed*.7f), -1.0f, FastMath.sin(-timeElapsed*.7f)));
    }
}