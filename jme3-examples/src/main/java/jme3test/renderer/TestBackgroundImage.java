
package jme3test.renderer;

import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.util.List;

/**
 * Demonstrates how to render a non-moving background image using a pre
 * ViewPort.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestBackgroundImage extends SimpleApplication {

    private static ViewPort backgroundViewport;

    public static void main(String[] args) {
        new TestBackgroundImage().start();
    }

    @Override
    public void simpleInitApp() {
        /*
         * SimpleApplication creates 2 viewports:
         * 1. the default viewport (rendered first, after clearing all buffers)
         * 2. the GUI viewport (rendered last, without clearing any buffers)
         *
         * Create a 3rd ViewPort, named "background viewport",
         * to be rendered BEFORE the default viewport.
         */
        Camera backgroundCamera = guiViewPort.getCamera().clone();
        backgroundViewport = renderManager.createPreView(
                "background viewport", backgroundCamera);
        /*
         * Don't clear the color buffer before drawing the main viewport.
         * Clearing the color buffer would hide the background.
         */
        boolean clearColorBuffer = false;
        viewPort.setClearFlags(clearColorBuffer, true, true);
        /*
         * Create a quad to display the JMonkeyEngine logo,
         * assign it to the Gui bucket,
         * and attach it to the background viewport.
         */
        Texture quadTexture
                = assetManager.loadTexture("Interface/Logo/Monkey.png");
        Material quadMaterial = new Material(assetManager, Materials.UNSHADED);
        quadMaterial.setTexture("ColorMap", quadTexture);

        float quadHeight = backgroundCamera.getHeight();
        float quadWidth = backgroundCamera.getWidth();
        Mesh quadMesh = new Quad(quadWidth, quadHeight);

        Spatial quadGeometry = new Geometry("quad geometry", quadMesh);
        quadGeometry.setMaterial(quadMaterial);
        quadGeometry.setQueueBucket(RenderQueue.Bucket.Gui);
        backgroundViewport.attachScene(quadGeometry);
        /*
         * Add Jaime model and lighting to the default scene.
         */
        loadModel();
        setupLights();
        /*
         * Speed up camera motion for convenience.
         */
        flyCam.setMoveSpeed(8f);
    }

    @Override
    public void simpleUpdate(float timePerFrame) {
        /*
         * Since SimpleApplication is unaware of the background viewport,
         * the application must explicitly update its scenes.
         */
        List<Spatial> scenes = backgroundViewport.getScenes();
        for (Spatial scene : scenes) {
            scene.updateLogicalState(timePerFrame);
            scene.updateGeometricState();
        }
    }

    private void loadModel() {
        Node jaime = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        AnimMigrationUtils.migrate(jaime);
        jaime.scale(3f);
        rootNode.attachChild(jaime);
    }

    private void setupLights() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f));
        rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0f, -1f, -1f).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(sun);
    }
}
