
package jme3test.model.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Simple application to test an attachments node on the Jaime model.
 *
 * Derived from {@link jme3test.model.anim.TestOgreAnim}.
 */
public class TestAttachmentsNode extends SimpleApplication
        implements ActionListener {

    private Action punchesOnce;
    private AnimComposer control;

    public static void main(String[] args) {
        TestAttachmentsNode app = new TestAttachmentsNode();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(6.4f, 7.5f, 12.8f));
        cam.setRotation(new Quaternion(-0.060740203f, 0.93925786f, -0.2398315f, -0.2378785f));

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(ColorRGBA.White);
        rootNode.addLight(dl);
        /*
         * Load the Jaime model and convert it
         * from the old animation system to the new one.
         */
        Spatial model = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        AnimMigrationUtils.migrate(model);
        /*
         * Play the "Idle" animation at half speed.
         */
        control = model.getControl(AnimComposer.class);
        control.setCurrentAction("Idle");
        control.setGlobalSpeed(0.5f);
        /*
         * Define a "PunchesOnce" action sequence to play the "Punches"
         * animation for one cycle before returning to idle.
         */
        Action punches = control.action("Punches");
        Tween doneTween
                = Tweens.callMethod(control, "setCurrentAction", "Idle");
        punchesOnce = control.actionSequence("PunchesOnce", punches, doneTween);

        model.center();
        model.setLocalScale(5f);

        Box box = new Box(0.3f, 0.02f, 0.02f);
        Geometry saber = new Geometry("saber", box);
        saber.move(0.4f, 0.05f, 0.01f);
        Material red = assetManager.loadMaterial("Common/Materials/RedColor.j3m");
        saber.setMaterial(red);
        /*
         * Create an attachments node for Jaime's right hand,
         * and attach the saber to that Node.
         */
        SkinningControl skeletonControl = model.getControl(SkinningControl.class);
        Node n = skeletonControl.getAttachmentsNode("hand.R");
        n.attachChild(saber);
        rootNode.attachChild(model);

        inputManager.addListener(this, "Attack");
        inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_SPACE));
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (value && binding.equals("Attack")
                && control.getCurrentAction() != punchesOnce) {
            control.setCurrentAction("PunchesOnce");
        }
    }
}
