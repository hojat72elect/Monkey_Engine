
package com.jme3.audio;

/**
 *  Holds render thread specific audio context information.
 *
 *  @author Paul Speed
 */
public class AudioContext {

    final private static ThreadLocal<AudioRenderer> audioRenderer = new ThreadLocal<AudioRenderer>();

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private AudioContext() {
    }

    public static void setAudioRenderer(AudioRenderer ar) {
        audioRenderer.set(ar);
    }

    public static AudioRenderer getAudioRenderer() {
        return audioRenderer.get();
    }
}
