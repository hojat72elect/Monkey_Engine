
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 * Tests whether gamma correction works after a context restart. This test
 * generates a series of boxes, each one with a slightly different shade from
 * the other. If the boxes look the same before and after the restart, that
 * means that gamma correction is working properly.
 * <p>
 * Note that for testing, it may be helpful to bypass the test chooser and run
 * this class directly, since it can be easier to define your own settings
 * beforehand. Of course, it should still work if all you need to test is the
 * gamma correction, as long as you enable it in the settings dialog.
 * </p>
 *
 * @author Markil 3
 */
public class TestContextRestart extends SimpleApplication
{
    public static final String INPUT_RESTART_CONTEXT = "SIMPLEAPP_Restart";

    public static void main(String[] args)
    {
        TestContextRestart app = new TestContextRestart();
        AppSettings settings = new AppSettings(true);
        settings.setGammaCorrection(true);
//        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        for (int i = 0, l = 256; i < l; i += 8)
        {
            Geometry box = new Geometry("Box" + i, new Box(10, 200, 10));
            Material mat = new Material(this.assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", new ColorRGBA((float) i / 255F, 0, 0, 1));
            box.setMaterial(mat);
            box.setLocalTranslation(-2.5F * (l / 2 - i), 0, -700);
            box.addControl(new AbstractControl()
            {
                @Override
                protected void controlUpdate(float tpf)
                {
                    float[] angles = this.getSpatial()
                            .getLocalRotation()
                            .toAngles(new float[3]);
                    angles[0] = angles[0] + (FastMath.PI / 500F);
                    this.getSpatial()
                            .setLocalRotation(new Quaternion().fromAngles(angles));
                }

                @Override
                protected void controlRender(RenderManager rm, ViewPort vp)
                {

                }
            });
            this.rootNode.attachChild(box);
        }

        this.viewPort.setBackgroundColor(ColorRGBA.Yellow);

        this.flyCam.setEnabled(false);
        this.inputManager.setCursorVisible(true);

        inputManager.addMapping(INPUT_RESTART_CONTEXT, new KeyTrigger(
                KeyInput.KEY_TAB));
        this.inputManager.addListener(new ActionListener()
        {
            @Override
            public void onAction(String name, boolean isPressed, float tpf)
            {
                if (name.equals(INPUT_RESTART_CONTEXT))
                {
                    restart();
                }
            }
        }, INPUT_RESTART_CONTEXT);
    }
}
