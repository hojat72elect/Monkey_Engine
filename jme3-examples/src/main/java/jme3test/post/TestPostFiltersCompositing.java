
package jme3test.post;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.post.filters.ComposeFilter;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.FrameBuffer.FrameBufferTarget;
import com.jme3.texture.Image.Format;
import com.jme3.util.SkyFactory;

/**
 * This test showcases the possibility to compose the post filtered outputs of several viewports.
 * The usual use case is when you want to apply some post process to the main viewport and then other post process to the gui viewport
 * @author Nehon
 */
public class TestPostFiltersCompositing extends SimpleApplication {

    public static void main(String[] args) {
        TestPostFiltersCompositing app = new TestPostFiltersCompositing();
//        AppSettings settings = new AppSettings(true);
//        settings.putBoolean("GraphicsDebug", false);
//        app.setSettings(settings);
        app.start();        
        
    }

    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0.028406568f, 2.015769f, 7.386517f));
        cam.setRotation(new Quaternion(-1.0729783E-5f, 0.9999721f, -0.0073241726f, -0.0014647911f));


        makeScene();

        //Creating the main view port post processor
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(new ColorOverlayFilter(ColorRGBA.Blue));
        viewPort.addProcessor(fpp);

        //creating a frame buffer for the main viewport
        FrameBuffer mainVPFrameBuffer = new FrameBuffer(cam.getWidth(), cam.getHeight(), 1);
        Texture2D mainVPTexture = new Texture2D(cam.getWidth(), cam.getHeight(), Image.Format.RGBA8);
        mainVPFrameBuffer.setDepthTarget(FrameBufferTarget.newTarget(Format.Depth));
        mainVPFrameBuffer.addColorTarget(FrameBufferTarget.newTarget(mainVPTexture));

        viewPort.setOutputFrameBuffer(mainVPFrameBuffer);

        // Create the post processor for the GUI viewport.
        final FilterPostProcessor guiFpp = new FilterPostProcessor(assetManager);
        guiFpp.setFrameBufferFormat(Image.Format.RGBA8);
        guiFpp.addFilter(new ColorOverlayFilter(ColorRGBA.Red));
        // This will compose the main viewport texture with the GUI-viewport back buffer.
        // Note that you can switch the order of the filters so that GUI-viewport filters are applied or not to the main viewport texture
        guiFpp.addFilter(new ComposeFilter(mainVPTexture));

        guiViewPort.addProcessor(guiFpp);
        
        // Compositing is done by mixing texture depending on the alpha channel, so
        // it's important that the GUI-viewport clear-color alpha value is set to 0.
        guiViewPort.setBackgroundColor(ColorRGBA.BlackNoAlpha);
        guiViewPort.setClearColor(true);


    }

    private void makeScene() {
        // load sky
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
        //assetManager.registerLocator("http://jmonkeyengine.googlecode.com/files/wildhouse.zip", HttpZipLocator.class);
        Spatial scene = assetManager.loadModel("Models/Test/CornellBox.j3o");
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.4790551f, -0.39247334f, -0.7851566f));
        scene.addLight(sun);
        rootNode.attachChild(scene);

    }
}
