
package jme3test.post;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

/**
 * Test case for JME issue #1798: filtered scenes are squeezed by resizable
 * windows.
 * <p>
 * If successful, a cartoon monkey head will be shown, and resizing the window
 * (using the system's window manager) will not change its shape.
 * <p>
 * If unsuccessful, then making the window taller will make the head taller, and
 * making the window wider will make the head wider.
 * <p>
 * Based on the TestCartoonEdge application.
 */
public class TestIssue1798 extends SimpleApplication {
    // *************************************************************************
    // fields

    private FilterPostProcessor fpp;
    // *************************************************************************
    // new methods exposed

    public static void main(String[] args) {
        AppSettings s = new AppSettings(true);
        s.setResizable(true);
        TestIssue1798 app = new TestIssue1798();
        app.setSettings(s);
        app.start();
    }
    // *************************************************************************
    // SimpleApplication methods

    /**
     * Initialize this application.
     */
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        flyCam.setDragToRotate(true);
        setupLighting();
        setupModel();
        setupFilters();
    }
    // *************************************************************************
    // private methods

    private void makeToonish(Spatial spatial) {
        if (spatial instanceof Node) {
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren()) {
                makeToonish(child);
            }
        } else if (spatial instanceof Geometry) {
            Geometry g = (Geometry) spatial;
            Material m = g.getMaterial();
            if (m.getMaterialDef().getMaterialParam("UseMaterialColors") != null) {
                Texture t = assetManager.loadTexture("Textures/ColorRamp/toon.png");
                m.setTexture("ColorRamp", t);
                m.setBoolean("UseMaterialColors", true);
                m.setColor("Specular", ColorRGBA.Black);
                m.setColor("Diffuse", ColorRGBA.White);
                m.setBoolean("VertexLighting", true);
            }
        }
    }

    private void setupFilters() {
        fpp = new FilterPostProcessor(assetManager);
        int numSamples = getContext().getSettings().getSamples();
        if (numSamples > 0) {
            fpp.setNumSamples(numSamples);
        }
        CartoonEdgeFilter toon = new CartoonEdgeFilter();
        toon.setEdgeColor(ColorRGBA.Yellow);
        fpp.addFilter(toon);
        viewPort.addProcessor(fpp);
    }

    private void setupLighting() {
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(2, 2, 2, 1));
        rootNode.addLight(dl);
    }

    private void setupModel() {
        Spatial model = assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        makeToonish(model);
        rootNode.attachChild(model);
    }
}
