
package jme3test.bullet;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Test case for JME issue #877: multiple hinges. Based on code submitted by
 * Daniel Martensson.
 *
 * If successful, all pendulums will swing at the same frequency, and all the
 * free-falling objects will fall straight down.
 */
public class TestIssue877 extends SimpleApplication {

    final private BulletAppState bulletAppState = new BulletAppState();
    final private int numPendulums = 6;
    final private int numFalling = 6;
    final private Node pivots[] = new Node[numPendulums];
    final private Node bobs[] = new Node[numPendulums];
    final private Node falling[] = new Node[numFalling];
    private float timeToNextPrint = 1f; // in seconds

    public static void main(String[] args) {
        TestIssue877 app = new TestIssue877();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(-4.77f, -7.55f, 16.52f));
        cam.setRotation(new Quaternion(-0.103433f, 0.889420f, 0.368792f, 0.249449f));

        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);

        float pivotY = 14.6214f;
        float bobStartY = 3f;
        float length = pivotY - bobStartY;
        for (int i = 0; i < numPendulums; i++) {
            float x = 6f - 2.5f * i;
            Vector3f pivotLocation = new Vector3f(x, pivotY, 0f);
            pivots[i] = createTestNode(0f, pivotLocation);

            Vector3f bobLocation = new Vector3f(x, bobStartY, 0f);
            bobs[i] = createTestNode(1f, bobLocation);
        }

        for (int i = 0; i < numFalling; i++) {
            float x = -6f - 2.5f * (i + numPendulums);
            Vector3f createLocation = new Vector3f(x, bobStartY, 0f);
            falling[i] = createTestNode(1f, createLocation);
        }

        for (int i = 0; i < numPendulums; i++) {
            HingeJoint joint = new HingeJoint(
                    pivots[i].getControl(RigidBodyControl.class),
                    bobs[i].getControl(RigidBodyControl.class),
                    new Vector3f(0f, 0f, 0f),
                    new Vector3f(length, 0f, 0f),
                    Vector3f.UNIT_Z.clone(),
                    Vector3f.UNIT_Z.clone());
            bulletAppState.getPhysicsSpace().add(joint);
        }
    }

    Node createTestNode(float mass, Vector3f location) {
        float size = 0.1f;
        Vector3f halfExtents = new Vector3f(size, size, size);
        CollisionShape shape = new BoxCollisionShape(halfExtents);
        RigidBodyControl control = new RigidBodyControl(shape, mass);
        Node node = new Node();
        node.addControl(control);
        rootNode.attachChild(node);
        bulletAppState.getPhysicsSpace().add(node);
        control.setPhysicsLocation(location);

        return node;
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (timeToNextPrint > 0f) {
            timeToNextPrint -= tpf;
            return;
        }

        if (numFalling > 0) {
            Vector3f fallingLocation = falling[0].getWorldTranslation();
            System.out.printf("  falling[0] location(x=%f, z=%f)",
                    fallingLocation.x, fallingLocation.z);
            /*
             * If an object is falling vertically, its X- and Z-coordinates 
             * should not change.
             */
        }
        if (numPendulums > 0) {
            Vector3f bobLocation = bobs[0].getWorldTranslation();
            Vector3f pivotLocation = pivots[0].getWorldTranslation();
            float distance = bobLocation.distance(pivotLocation);
            System.out.printf("  bob[0] distance=%f", distance);
            /*
             * If the hinge is working properly, the distance from the
             * pivot to the bob should remain roughly constant.
             */
        }
        System.out.println();
        timeToNextPrint = 1f;
    }
}
