
package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TempVars;

/**
 * Creates a directional light controlled by rotating a node. The light will shine on a surrounding sphere.
 * The light will rotate on each axis, then on a random axis, then return to the X
 * axis.
 *
 * @author Markil 3
 */
public class TestLightControlDirectional extends SimpleApplication {
    private final Vector3f rotAxis = new Vector3f(Vector3f.UNIT_X);
    private final float[] angles = new float[3];

    private Node lightNode;
    private DirectionalLight direction;

    public static void main(String[] args) {
        TestLightControlDirectional app = new TestLightControlDirectional();
        app.start();
    }

    public void setupLighting() {
        Geometry lightMdl;
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(2f));
        rootNode.addLight(al);

        direction = new DirectionalLight();
        direction.setColor(ColorRGBA.White.mult(10));
        rootNode.addLight(direction);

        lightMdl = new Geometry("Light", new Dome(Vector3f.ZERO, 2, 32, 5, false));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.setLocalTranslation(new Vector3f(0, 0, 0));
        lightMdl.setLocalRotation(new Quaternion().fromAngles(FastMath.PI / 2F, 0, 0));
        rootNode.attachChild(lightMdl);

        /*
         * We need this Dome doesn't have a "floor."
         */
        Geometry lightFloor = new Geometry("LightFloor", new Cylinder(2, 32, 5, .1F, true));
        lightFloor.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightFloor.getMaterial().setColor("Color", ColorRGBA.White);

        lightNode = new Node();
        lightNode.addControl(new LightControl(direction));
        lightNode.attachChild(lightMdl);
        lightNode.attachChild(lightFloor);
        rootNode.attachChild(lightNode);
    }

    public void setupDome() {
        Geometry dome = new Geometry("Dome", new Sphere(16, 32, 30, false, true));
        dome.setMaterial(new Material(this.assetManager, "Common/MatDefs/Light/PBRLighting.j3md"));
        dome.setLocalTranslation(new Vector3f(0, 0, 0));
        rootNode.attachChild(dome);
    }

    @Override
    public void simpleInitApp() {
        this.cam.setLocation(new Vector3f(-50, 20, 50));
        this.cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(30);

        setupLighting();
        setupDome();
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*
         * In Radians per second
         */
        final float ROT_SPEED = FastMath.PI / 2;
        /*
         * 360 degree rotation
         */
        final float FULL_ROT = FastMath.PI * 2;

        TempVars vars = TempVars.get();
        Vector3f lightDirection = vars.vect2, nodeDirection = vars.vect3;
        float length;

        angles[0] += rotAxis.x * ROT_SPEED * tpf;
        angles[1] += rotAxis.y * ROT_SPEED * tpf;
        angles[2] += rotAxis.z * ROT_SPEED * tpf;
        lightNode.setLocalRotation(new Quaternion().fromAngles(angles));
        super.simpleUpdate(tpf);

        /*
         * Make sure they are equal.
         */
        lightDirection.set(direction.getDirection());
        lightDirection.normalize();
        lightNode.getWorldRotation().mult(Vector3f.UNIT_Z, nodeDirection);
        nodeDirection.negateLocal().normalizeLocal();
        length = lightDirection.subtract(nodeDirection, vars.vect4).lengthSquared();
        length = FastMath.abs(length);
        if (length > .1F) {
            System.err.printf("Rotation not equal: is %s, needs to be %s (%f)\n", nodeDirection, lightDirection, length);
        }

        if (angles[0] >= FULL_ROT || angles[1] >= FULL_ROT || angles[2] >= FULL_ROT) {
            lightNode.setLocalRotation(Quaternion.DIRECTION_Z);
            angles[0] = 0;
            angles[1] = 0;
            angles[2] = 0;
            if (rotAxis.x > 0 && rotAxis.y == 0 && rotAxis.z == 0) {
                rotAxis.set(0, 1, 0);
            } else if (rotAxis.y > 0 && rotAxis.x == 0 && rotAxis.z == 0) {
                rotAxis.set(0, 0, 1);
            } else if (rotAxis.z > 0 && rotAxis.x == 0 && rotAxis.y == 0) {
                rotAxis.set(FastMath.nextRandomFloat() % 1, FastMath.nextRandomFloat() % 1, FastMath.nextRandomFloat() % 1);
            } else {
                rotAxis.set(1, 0, 0);
            }
        }

        vars.release();
    }
}
