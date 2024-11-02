

package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

public class TestMusicStreaming extends SimpleApplication {

    public static void main(String[] args){
        TestMusicStreaming test = new TestMusicStreaming();
        test.start();
    }

    @Override
    public void simpleInitApp(){
        assetManager.registerLocator("https://web.archive.org/web/20170625151521if_/http://www.vorbis.com/music/", UrlLocator.class);
        AudioNode audioSource = new AudioNode(assetManager, "Lumme-Badloop.ogg",
                AudioData.DataType.Stream);
        audioSource.setPositional(false);
        audioSource.setReverbEnabled(false);
        audioSource.play();
    }

    @Override
    public void simpleUpdate(float tpf){}

}