
package jme3test.light.pbr;

import com.jme3.app.SimpleApplication;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.CenterQuad;
import com.jme3.system.AppSettings;

/**
 * Reproduces an issue where PBR materials render much darker with the Core 3.2
 * profile than with the Compatibility profile.
 * 
 * <p>This test relies on AppSettings set in main(), so it shouldn't be run
 * from the jme3-examples TestChooser!
 *
 * <p>Compare the window rendered by this test with that rendered by
 * TestIssue1903Core. If they differ, you have reproduced the issue.
 * If they are identical, then you haven't reproduced it.
 */
public class TestIssue1903Compat extends SimpleApplication {
    /**
     * Main entry point for the TestIssue1903Compat application.
     *
     * @param unused array of command-line arguments
     */
    public static void main(String[] unused) {
        boolean loadDefaults = true;
        AppSettings appSettings = new AppSettings(loadDefaults);
        appSettings.setGammaCorrection(true);
        appSettings.setRenderer(AppSettings.LWJGL_OPENGL2); // Compatibility profile
        appSettings.setTitle("Compatibility");

        TestIssue1903Compat application = new TestIssue1903Compat();
        application.setSettings(appSettings);
        application.setShowSettings(false); // to speed up testing
        application.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);

        // Attach a 9x9 quad at the origin.
        Mesh mesh = new CenterQuad(9f, 9f);
        Geometry quad = new Geometry("quad", mesh);
        rootNode.attachChild(quad);

        // Apply a PBR material to the quad.
        String materialAssetPath = "TestIssue1903.j3m";
        Material material = assetManager.loadMaterial(materialAssetPath);
        quad.setMaterial(material);

        // Add a LightProbe.
        String lightProbePath = "Scenes/LightProbes/quarry_Probe.j3o";
        LightProbe probe = (LightProbe) assetManager.loadAsset(lightProbePath);
        rootNode.addLight(probe);
    }
}
