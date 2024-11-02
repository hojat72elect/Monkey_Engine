

package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;

/**
 * Test Doppler Effect
 */
public class TestDoppler extends SimpleApplication {

    private float pos = -5;
    private float vel = 5;
    private AudioNode ufoNode;

    public static void main(String[] args){
        TestDoppler test = new TestDoppler();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);

        Torus torus = new Torus(10, 6, 1, 3);
        Geometry g = new Geometry("Torus Geom", torus);
        g.rotate(-FastMath.HALF_PI, 0, 0);
        g.center();

        g.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
//        rootNode.attachChild(g);

        ufoNode = new AudioNode(assetManager, "Sound/Effects/Beep.ogg", AudioData.DataType.Buffer);
        ufoNode.setLooping(true);
        ufoNode.setPitch(0.5f);
        ufoNode.setRefDistance(1);
        ufoNode.setMaxDistance(100000000);
        ufoNode.setVelocityFromTranslation(true);
        ufoNode.play();

        Geometry ball = new Geometry("Beeper", new Sphere(10, 10, 0.1f));
        ball.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        ufoNode.attachChild(ball);

        rootNode.attachChild(ufoNode);
    }


    @Override
    public void simpleUpdate(float tpf) {
        pos += tpf * vel;
        if (pos < -10 || pos > 10) {
            vel *= -1;
        }
        ufoNode.setLocalTranslation(new Vector3f(pos, 0, 0));
    }
}
