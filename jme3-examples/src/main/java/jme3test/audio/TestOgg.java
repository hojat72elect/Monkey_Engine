

package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.audio.LowPassFilter;

public class TestOgg extends SimpleApplication {

    private AudioNode audioSource;

    public static void main(String[] args){
        TestOgg test = new TestOgg();
        test.start();
    }

    @Override
    public void simpleInitApp(){
        System.out.println("Playing without filter");
        audioSource = new AudioNode(assetManager, "Sound/Effects/Foot steps.ogg", DataType.Buffer);
        audioSource.play();
    }

    @Override
    public void simpleUpdate(float tpf){
        if (audioSource.getStatus() != AudioSource.Status.Playing){
            audioRenderer.deleteAudioData(audioSource.getAudioData());

            System.out.println("Playing with low pass filter");
            audioSource = new AudioNode(assetManager, "Sound/Effects/Foot steps.ogg", DataType.Buffer);
            audioSource.setDryFilter(new LowPassFilter(1f, .1f));
            audioSource.setVolume(3);
            audioSource.play();
        }
    }

}
