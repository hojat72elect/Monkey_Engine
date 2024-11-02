
package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class TestAmbient extends SimpleApplication {

  public static void main(String[] args) {
    TestAmbient test = new TestAmbient();
    test.start();
  }

  @Override
  public void simpleInitApp() {
    float[] eax = new float[]{15, 38.0f, 0.300f, -1000, -3300, 0,
      1.49f, 0.54f, 1.00f, -2560, 0.162f, 0.00f, 0.00f,
      0.00f, -229, 0.088f, 0.00f, 0.00f, 0.00f, 0.125f, 1.000f,
      0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f};
    Environment env = new Environment(eax);
    audioRenderer.setEnvironment(env);

    AudioNode waves = new AudioNode(assetManager,
            "Sound/Environment/Ocean Waves.ogg", DataType.Buffer);
    waves.setPositional(true);
    waves.setLocalTranslation(new Vector3f(0, 0,0));
    waves.setMaxDistance(100);
    waves.setRefDistance(5);

    AudioNode nature = new AudioNode(assetManager,
            "Sound/Environment/Nature.ogg", DataType.Stream);
    nature.setPositional(false);
    nature.setVolume(3);
    
    waves.playInstance();
    nature.play();
    
    // just a blue box to mark the spot
    Box box1 = new Box(.5f, .5f, .5f);
    Geometry player = new Geometry("Player", box1);
    Material mat1 = new Material(assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat1);
    rootNode.attachChild(player);
  }

  @Override
  public void simpleUpdate(float tpf) {
  }
}
