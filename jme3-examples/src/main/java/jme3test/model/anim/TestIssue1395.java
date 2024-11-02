
package jme3test.model.anim;

import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Test case for Issue 1395 (OGRE Importer should call saveInitialPose, to be
 * consistent with GLTF)
 * <p>
 * If the test succeeds, the humanoid Oto model will appear in a standing
 * position. If the test fails, the model will appear rolled up into a tight
 * bundle.
 */
public class TestIssue1395 extends SimpleApplication {

    public static void main(String[] args) {
        TestIssue1395 app = new TestIssue1395();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(1f, 1f, 1f, 1f));

        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(6.4013605f, 7.488437f, 12.843031f));
        cam.setRotation(new Quaternion(-0.06f, 0.939258f, -0.2399f, -0.2379f));

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
        rootNode.addLight(dl);

        Spatial model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        rootNode.attachChild(model);
        model.center();

        SkinningControl skinningControl
                = model.getControl(SkinningControl.class);
        skinningControl.getArmature().applyInitialPose();
    }
}
