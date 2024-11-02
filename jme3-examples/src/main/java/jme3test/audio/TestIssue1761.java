
package jme3test.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import java.util.Random;

/**
 * Stress test to reproduce JME issue #1761 (AssertionError in ALAudioRenderer).
 *
 * <p>After some network delay, a song will play,
 * albeit slowly and in a broken fashion.
 * If the issue is solved, the song will play all the way through.
 * If the issue is present, an AssertionError will be thrown, usually within a
 * second of the song starting.
 */
public class TestIssue1761 extends SimpleApplication {

    private AudioNode audioNode;
    final private Random random = new Random();

    /**
     * Main entry point for the TestIssue1761 application.
     *
     * @param unused array of command-line arguments
     */
    public static void main(String[] unused) {
        TestIssue1761 test = new TestIssue1761();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(
                "https://web.archive.org/web/20170625151521if_/http://www.vorbis.com/music/",
                UrlLocator.class);
        audioNode = new AudioNode(assetManager, "Lumme-Badloop.ogg",
                AudioData.DataType.Stream);
        audioNode.setPositional(false);
        audioNode.play();
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*
         * Randomly pause and restart the audio.
         */
        if (random.nextInt(2) == 0) {
            audioNode.pause();
        } else {
            audioNode.play();
        }
    }
}
