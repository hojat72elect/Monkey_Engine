
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.system.AppSettings;

/**
 * Test for JMonkeyEngine issue #2011: context profiles are not defined for
 * OpenGL v3.0/v3.1
 * <p>
 * If the issue is resolved, then pressing the "0" or "1" key shouldn't crash
 * the app; it should close the app display and create a new display, mostly
 * black with statistics displayed in the lower left.
 * <p>
 * If the issue is not resolved, then pressing the "0" or "1" key should crash
 * the app with multiple exceptions.
 * <p>
 * Since the issue was specific to LWJGL v3, this test should be built with the
 * jme3-lwjgl3 library, not jme3-lwjgl.
 *
 * @author Stephen Gold
 */
public class TestIssue2011 extends SimpleApplication {
    /**
     * Main entry point for the TestIssue2011 application.
     *
     * @param args array of command-line arguments (not null)
     */
    public static void main(String[] args) {
        TestIssue2011 app = new TestIssue2011();
        app.start();
    }

    /**
     * Initialize this application.
     */
    @Override
    public void simpleInitApp() {
        inputManager.addMapping("3.0", new KeyTrigger(KeyInput.KEY_0),
                new KeyTrigger(KeyInput.KEY_NUMPAD0));
        inputManager.addMapping("3.1", new KeyTrigger(KeyInput.KEY_1),
                new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("3.2", new KeyTrigger(KeyInput.KEY_2),
                new KeyTrigger(KeyInput.KEY_NUMPAD2));

        ActionListener listener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("3.0") && keyPressed) {
                    setApi(AppSettings.LWJGL_OPENGL30);
                } else if (name.equals("3.1") && keyPressed) {
                    setApi(AppSettings.LWJGL_OPENGL31);
                } else if (name.equals("3.2") && keyPressed) {
                    setApi(AppSettings.LWJGL_OPENGL32);
                }
            }
        };

        inputManager.addListener(listener, "3.0", "3.1", "3.2");
    }

    /**
     * Restart the app, specifying which OpenGL version to use.
     *
     * @param desiredApi the string to be passed to setRenderer()
     */
    private void setApi(String desiredApi) {
        System.out.println("desiredApi = " + desiredApi);
        settings.setRenderer(desiredApi);
        setSettings(settings);

        restart();
    }
}
