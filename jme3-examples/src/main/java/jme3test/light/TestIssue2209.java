
package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.logging.Logger;
import jme3test.bullet.TestIssue1125;

/**
 * Test case for JME issue #2209: AssertionError caused by shadow renderer.
 *
 * <p>For a valid test, assertions must be enabled.
 *
 * <p>If successful, the Oto model will appear. If unsuccessful, the application
 * with crash with an {@code AssertionError} in {@code GLRenderer}.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestIssue2209 extends SimpleApplication {
    /**
     * message logger for debugging this class
     */
    final public static Logger logger
            = Logger.getLogger(TestIssue1125.class.getName());

    /**
     * Main entry point for the TestIssue2209 application.
     */
    public static void main(String[] args) {
        new TestIssue2209().start();
    }

    /**
     * Initializes this application, adding Oto, a light, and a shadow renderer.
     */
    @Override
    public void simpleInitApp() {
        if (!areAssertionsEnabled()) {
            throw new IllegalStateException(
                    "For a valid test, assertions must be enabled.");
        }

        DirectionalLight dl = new DirectionalLight();
        rootNode.addLight(dl);

        DirectionalLightShadowRenderer dlsr
                = new DirectionalLightShadowRenderer(assetManager, 4_096, 3);
        dlsr.setLight(dl);
        viewPort.addProcessor(dlsr);

        Node player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        player.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(player);
    }

    /**
     * Tests whether assertions are enabled.
     *
     * @return true if enabled, otherwise false
     */
    private static boolean areAssertionsEnabled() {
        boolean enabled = false;
        assert enabled = true; // Note: intentional side effect.

        return enabled;
    }
}
