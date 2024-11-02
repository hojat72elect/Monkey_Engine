
package com.jme3.audio;

/**
 * Interface to be implemented by audio renderers.
 *
 * @author Kirill Vainer
 */
public interface AudioRenderer {

    /**
     * @param listener The listener camera, all 3D sounds will be
     * oriented around the listener.
     */
    public void setListener(Listener listener);

    /**
     * Sets the environment, used for reverb effects.
     *
     * @see AudioNode#setReverbEnabled(boolean)
     * @param env The environment to set.
     */
    public void setEnvironment(Environment env);

    public void playSourceInstance(AudioSource src);
    public void playSource(AudioSource src);
    public void pauseSource(AudioSource src);
    public void stopSource(AudioSource src);

    public void updateSourceParam(AudioSource src, AudioParam param);
    public void updateListenerParam(Listener listener, ListenerParam param);
    public float getSourcePlaybackTime(AudioSource src);

    public void deleteFilter(Filter filter);
    public void deleteAudioData(AudioData ad);

    /**
     * Initializes the renderer. Should be the first method called
     * before using the system.
     */
    public void initialize();

    /**
     * Update the audio system. Must be called periodically.
     * @param tpf Time per frame.
     */
    public void update(float tpf);
    
    /**
     * Pauses all Playing audio. 
     * To be used when the app is placed in the background.
     */
    public void pauseAll();

    /**
     * Resumes all audio paused by {@link #pauseAll()}. 
     * To be used when the app is brought back to the foreground.
     */
    public void resumeAll();

    /**
     * Cleanup/destroy the audio system. Call this when app closes.
     */
    public void cleanup();
}
