
package com.jme3.test;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.app.LegacyApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;
import com.jme3.system.NullRenderer;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The Test Suite to prevent regressions from previously fixed Core issues
 * @author Stephen Gold &lt;sgold@sonic.net&gt;
 */
public class PreventCoreIssueRegressions {

    /**
     * Test case for JME issue #1421: ScreenshotAppState never cleans up.
     */
    @Test
    public void testIssue1421() throws NoSuchFieldException, IllegalAccessException {
        ScreenshotAppState screenshotAppState = new ScreenshotAppState("./", "screen_shot");

        SimpleApplication app = new SimpleApplication(new AppState[]{}) {
            // Dummy application because SimpleApp is abstract
            @Override
            public void simpleInitApp() { }
        };

        app.setAssetManager(new DesktopAssetManager(true));
        Field f1 = LegacyApplication.class.getDeclaredField("inputManager");
        f1.setAccessible(true);
        f1.set(app, new InputManager(new DummyMouseInput(), new DummyKeyInput(), null, null));

        Field f2 = LegacyApplication.class.getDeclaredField("renderManager");
        f2.setAccessible(true);
        f2.set(app, new RenderManager(new NullRenderer()));

        app.getRenderManager().createPostView("null", new Camera(1, 1));

        Assert.assertTrue(app.getStateManager().attach(screenshotAppState));
        app.getStateManager().update(0f); // Causes SAS#initialize to be called

        // Confirm that the SceneProcessor is attached.
        List<ViewPort> vps = app.getRenderManager().getPostViews();
        Assert.assertEquals(1, vps.size());
        Assert.assertEquals(1, vps.get(0).getProcessors().size());
        Assert.assertTrue(app.getInputManager().hasMapping("ScreenShot")); // Confirm that KEY_SYSRQ is mapped.
        Assert.assertTrue(app.getStateManager().detach(screenshotAppState));

        app.getStateManager().update(0f); // Causes SAS#cleanup to be called

        // Check whether the SceneProcessor is still attached.
        Assert.assertEquals(0, vps.get(0).getProcessors().size());
        Assert.assertFalse(app.getInputManager().hasMapping("ScreenShot")); // Confirm that KEY_SYSRQ is unmapped.
    }

    /**
     * Test case for JME issue #1138: Elephant's legUp animation sets Joint translation to NaN.
     */
    @Test
    public void testIssue1138() {
        AssetManager am = JmeSystem.newAssetManager(PreventCoreIssueRegressions.class.getResource("/com/jme3/asset/Desktop.cfg"));
        Node cgModel = (Node)am.loadModel("Models/Elephant/Elephant.mesh.xml");
        cgModel.rotate(0f, -1f, 0f);
        cgModel.scale(0.04f);

        AnimComposer composer = cgModel.getControl(AnimComposer.class);
        composer.setCurrentAction("legUp");
        SkinningControl sControl = cgModel.getControl(SkinningControl.class);

        for (Joint joint : sControl.getArmature().getJointList()) {
            Assert.assertTrue("Invalid translation for joint " + joint.getName(),
                    Vector3f.isValidVector(joint.getLocalTranslation()));
        }

        cgModel.updateLogicalState(1.0f);
        cgModel.updateGeometricState();

        for (Joint joint : sControl.getArmature().getJointList()) {
            Assert.assertTrue("Invalid translation for joint " + joint.getName(),
                    Vector3f.isValidVector(joint.getLocalTranslation()));
        }
    }
}
