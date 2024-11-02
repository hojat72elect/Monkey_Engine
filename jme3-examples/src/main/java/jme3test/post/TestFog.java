

package jme3test.post;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.ArrayList;
import java.util.List;

public class TestFog extends SimpleApplication {

    private FilterPostProcessor fpp;
    private boolean enabled=true;
    private FogFilter fog;

    public static void main(String[] args) {
        TestFog app = new TestFog();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(50);
        Node mainScene=new Node();
        cam.setLocation(new Vector3f(-34.74095f, 95.21318f, -287.4945f));
        cam.setRotation(new Quaternion(0.023536969f, 0.9361278f, -0.016098259f, -0.35050195f));

        // load sky
        mainScene.attachChild(SkyFactory.createSky(assetManager, 
                "Textures/Sky/Bright/BrightSky.dds", 
                SkyFactory.EnvMapType.CubeMap));
        createTerrain(mainScene);



        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir=new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        mainScene.addLight(sun);

        rootNode.attachChild(mainScene);

        fpp=new FilterPostProcessor(assetManager);
        //fpp.setNumSamples(4);
        int numSamples = getContext().getSettings().getSamples();
        if( numSamples > 0 ) {
            fpp.setNumSamples(numSamples); 
        }
        fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        fog.setFogDistance(155);
        fog.setFogDensity(1.0f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
        initInputs();
    }

     private void initInputs() {
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("DistanceUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("DistanceDown", new KeyTrigger(KeyInput.KEY_J));


        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("toggle") && keyPressed) {
                    if(enabled){
                        enabled=false;
                        viewPort.removeProcessor(fpp);
                    }else{
                        enabled=true;
                        viewPort.addProcessor(fpp);
                    }
                }

            }
        };

        AnalogListener anl=new AnalogListener() {

            @Override
            public void onAnalog(String name, float isPressed, float tpf) {
                if(name.equals("DensityUp")){
                    fog.setFogDensity(fog.getFogDensity()+0.001f);
                    System.out.println("Fog density : "+fog.getFogDensity());
                }
                if(name.equals("DensityDown")){
                    fog.setFogDensity(fog.getFogDensity()-0.010f);
                    System.out.println("Fog density : "+fog.getFogDensity());
                }
                if(name.equals("DistanceUp")){
                    fog.setFogDistance(fog.getFogDistance()+0.5f);
                    System.out.println("Fog Distance : "+fog.getFogDistance());
                }
                if(name.equals("DistanceDown")){
                    fog.setFogDistance(fog.getFogDistance()-0.5f);
                    System.out.println("Fog Distance : "+fog.getFogDistance());
                }

            }
        };

        inputManager.addListener(acl, "toggle");
        inputManager.addListener(anl, "DensityUp","DensityDown","DistanceUp","DistanceDown");

     }

    private void createTerrain(Node rootNode) {
        Material matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);
        matRock.setBoolean("WardIso", true);
        matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap", grass);
        matRock.setFloat("DiffuseMap_0_scale", 64);
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_1", dirt);
        matRock.setFloat("DiffuseMap_1_scale", 16);
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_2", rock);
        matRock.setFloat("DiffuseMap_2_scale", 128);
        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMap0.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMap1.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMap2.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("NormalMap", normalMap0);
        matRock.setTexture("NormalMap_1", normalMap1);
        matRock.setTexture("NormalMap_2", normalMap2);

        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
            heightmap.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        List<Camera> cameras = new ArrayList<>();
        cameras.add(getCamera());
        terrain.setMaterial(matRock);
        terrain.setLocalScale(new Vector3f(5, 5, 5));
        terrain.setLocalTranslation(new Vector3f(0, -30, 0));
        terrain.setLocked(false); // unlock it so we can edit the height

        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(terrain);

    }
}

