
package jme3test.light.pbr;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 * Reproduces an issue where PBR materials render much darker with the Core 3.2
 * profile than with the Compatibility profile.
 * 
 * <p>This test relies on AppSettings set in main(), so it shouldn't be run
 * from the jme3-examples TestChooser!
 *
 * <p>Compare the window rendered by this test with that rendered by
 * TestIssue1903Compat. If they differ, you have reproduced the issue.
 * If they are identical, then you haven't reproduced it.
 */
public class TestIssue1903Core extends SimpleApplication {
    /**
     * Main entry point for the TestIssue1903Core application.
     *
     * @param unused array of command-line arguments
     */
    public static void main(String[] unused) {
        boolean loadDefaults = true;
        AppSettings appSettings = new AppSettings(loadDefaults);
        appSettings.setGammaCorrection(true);
        appSettings.setRenderer(AppSettings.LWJGL_OPENGL32); // Core 3.2 profile
        appSettings.setTitle("Core 3.2");

        TestIssue1903Compat application = new TestIssue1903Compat();
        application.setSettings(appSettings);
        application.setShowSettings(false); // to speed up testing
        application.start();
    }

    @Override
    public void simpleInitApp() {
        throw new AssertionError(); // never reached
    }
}
