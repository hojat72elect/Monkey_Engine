
package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class TestReverb extends SimpleApplication {

  private AudioNode audioSource;
  private float time = 0;
  private float nextTime = 1;

  public static void main(String[] args) {
    TestReverb test = new TestReverb();
    test.start();
  }

  @Override
  public void simpleInitApp() {
    audioSource = new AudioNode(assetManager, "Sound/Effects/Bang.wav",
            AudioData.DataType.Buffer);

    float[] eax = new float[]{15, 38.0f, 0.300f, -1000, -3300, 0,
      1.49f, 0.54f, 1.00f, -2560, 0.162f, 0.00f, 0.00f, 0.00f,
      -229, 0.088f, 0.00f, 0.00f, 0.00f, 0.125f, 1.000f, 0.250f,
      0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f};
    audioRenderer.setEnvironment(new Environment(eax));
    Environment env = Environment.Cavern;
    audioRenderer.setEnvironment(env);
  }

  @Override
  public void simpleUpdate(float tpf) {
    time += tpf;

    if (time > nextTime) {
      Vector3f v = new Vector3f();
      v.setX(FastMath.nextRandomFloat());
      v.setY(FastMath.nextRandomFloat());
      v.setZ(FastMath.nextRandomFloat());
      v.multLocal(40, 2, 40);
      v.subtractLocal(20, 1, 20);

      audioSource.setLocalTranslation(v);
      audioSource.playInstance();
      time = 0;
      nextTime = FastMath.nextRandomFloat() * 2 + 0.5f;
    }
  }
}
