

package jme3test.model.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.action.Action;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.debug.custom.ArmatureDebugger;

public class TestOgreComplexAnim extends SimpleApplication {

    private SkinningControl skinningControl;

    private float angle = 0;
    private float rate = 1;

    public static void main(String[] args) {
        TestOgreComplexAnim app = new TestOgreComplexAnim();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(6.4013605f, 7.488437f, 12.843031f));
        cam.setRotation(new Quaternion(-0.060740203f, 0.93925786f, -0.2398315f, -0.2378785f));

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

        Node model = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");

        skinningControl = model.getControl(SkinningControl.class);
        AnimComposer ac = model.getControl(AnimComposer.class);

        ArmatureMask feet = ArmatureMask.createMask(skinningControl.getArmature(), "hip.right", "hip.left");
        Action dodgeAction = ac.action("Dodge");
        dodgeAction.setMask(feet);
        dodgeAction.setSpeed(2f);
        Action walkAction = ac.action("Walk");
        walkAction.setMask(feet);
        walkAction.setSpeed(0.25f);

        ArmatureMask rightHand = ArmatureMask.createMask(skinningControl.getArmature(), "uparm.right");
        Action pullAction = ac.action("pull");
        pullAction.setMask(rightHand);
        pullAction.setSpeed(0.5f);
        Action standAction = ac.action("stand");
        standAction.setMask(rightHand);
        standAction.setSpeed(0.5f);

        ac.actionSequence("complexAction",
                ac.actionSequence("feetAction", dodgeAction, walkAction),
                ac.actionSequence("rightHandAction", pullAction, standAction));

        ac.setCurrentAction("complexAction");

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Green);
        mat.setFloat("PointSize", 7f); // Bug ? do not change size of debug points ?
        mat.getAdditionalRenderState().setDepthTest(false);

        ArmatureDebugger armatureDebug = new ArmatureDebugger("armature", skinningControl.getArmature(),
                skinningControl.getArmature().getJointList());
        armatureDebug.setMaterial(mat);
        model.attachChild(armatureDebug);

        rootNode.attachChild(model);
    }

    @Override
    public void simpleUpdate(float tpf) {
        Joint j = skinningControl.getArmature().getJoint("spinehigh");
        Joint j2 = skinningControl.getArmature().getJoint("uparm.left");

        angle += tpf * rate;
        if (angle > FastMath.HALF_PI / 2f) {
            angle = FastMath.HALF_PI / 2f;
            rate = -1;
        } else if (angle < -FastMath.HALF_PI / 2f) {
            angle = -FastMath.HALF_PI / 2f;
            rate = 1;
        }

        Quaternion q = new Quaternion();
        q.fromAngles(0, angle, 0);

        j.setLocalRotation(j.getInitialTransform().getRotation().mult(q));
        j2.setLocalScale(j.getInitialTransform().getScale().mult(new Vector3f(1 + angle, 1 + angle, 1 + angle)));
    }

}
