
package com.jme3.niftygui;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.AudioSource.Status;
import de.lessvoid.nifty.spi.sound.SoundHandle;

public class SoundHandleJme implements SoundHandle {

    private AudioNode node;
    private AssetManager am;
    private String fileName;
    private float volume = 1;

    public SoundHandleJme(AudioRenderer ar, AudioNode node) {
        if (ar == null) {
            throw new IllegalArgumentException("AudioRenderer cannot be null");
        }
        if (node == null) {
            throw new IllegalArgumentException("AudioNode cannot be null");
        }

        this.node = node;
    }

    /**
     * For streaming music only. (May need to loop.)
     *
     * @param ar for rendering audio (not null)
     * @param am the AssetManager for loading assets (not null)
     * @param fileName the path to the audio asset (not null)
     */
    public SoundHandleJme(AudioRenderer ar, AssetManager am, String fileName) {
        if (ar == null) {
            throw new IllegalArgumentException("AudioRenderer cannot be null");
        }
        if (am == null) {
            throw new IllegalArgumentException("AssetManager cannot be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName cannot be null");
        }

        this.am = am;
        this.fileName = fileName;
    }

    @Override
    public void play() {
        if (fileName != null) {
            if (node != null) {
                node.stop();
            }

            node = new AudioNode(am, fileName, AudioData.DataType.Stream);
            node.setPositional(false);
            node.setVolume(volume);
            node.play();
        } else {
            node.playInstance();
        }
    }

    @Override
    public void stop() {
        if (node != null) {
            node.stop();
            // Do not nullify the node for non-streaming nodes!
            if (fileName != null) {
                // Causes play() to reload the stream on the next playback
                node = null;
            }
        }
    }

    @Override
    public void setVolume(float f) {
        if (node != null) {
            node.setVolume(f);
        }
        volume = f;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public boolean isPlaying() {
        return node != null && node.getStatus() == Status.Playing;
    }

    @Override
    public void dispose() {
    }
}
