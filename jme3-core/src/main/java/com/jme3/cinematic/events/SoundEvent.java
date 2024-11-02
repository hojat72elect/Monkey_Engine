
package com.jme3.cinematic.events;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.cinematic.Cinematic;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 * A sound track to be played in a cinematic.
 * @author Nehon
 */
public class SoundEvent extends AbstractCinematicEvent {

    protected String path;
    protected AudioNode audioNode;
    protected boolean stream = false;

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     */
    public SoundEvent(String path) {
        this.path = path;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param stream true to make the audio data streamed
     */
    public SoundEvent(String path, boolean stream) {
        this(path);
        this.stream = stream;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param stream true to make the audio data streamed
     * @param initialDuration the initial duration of the event
     */
    public SoundEvent(String path, boolean stream, float initialDuration) {
        super(initialDuration);
        this.path = path;
        this.stream = stream;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param stream true to make the audio data streamed
     * @param loopMode the loopMode
     * @see LoopMode
     */
    public SoundEvent(String path, boolean stream, LoopMode loopMode) {
        super(loopMode);
        this.path = path;
        this.stream = stream;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param stream true to make the audio data streamed
     * @param initialDuration the initial duration of the event
     * @param loopMode the loopMode
     * @see LoopMode
     */
    public SoundEvent(String path, boolean stream, float initialDuration, LoopMode loopMode) {
        super(initialDuration, loopMode);
        this.path = path;
        this.stream = stream;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param initialDuration the initial duration of the event
     */
    public SoundEvent(String path, float initialDuration) {
        super(initialDuration);
        this.path = path;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param loopMode the loopMode
     * @see LoopMode
     */
    public SoundEvent(String path, LoopMode loopMode) {
        super(loopMode);
        this.path = path;
    }

    /**
     * creates a sound track from the given resource path
     * @param path the path to an audio file (ie : "Sounds/mySound.wav")
     * @param initialDuration the initial duration of the event
     * @param loopMode the loopMode
     * @see LoopMode
     */
    public SoundEvent(String path, float initialDuration, LoopMode loopMode) {
        super(initialDuration, loopMode);
        this.path = path;
    }

    /**
     * creates a sound event
     * used for serialization
     */
    public SoundEvent() {
        super();
    }

    @Override
    public void initEvent(Application app, Cinematic cinematic) {
        super.initEvent(app, cinematic);
        audioNode = new AudioNode(app.getAssetManager(), path, stream ? AudioData.DataType.Stream : AudioData.DataType.Buffer);
        audioNode.setPositional(false);
        setLoopMode(loopMode);
    }

    @Override
    public void setTime(float time) {
        super.setTime(time);
        //can occur on rewind
        if (time < 0f) {
            stop();
        } else {
            audioNode.setTimeOffset(time);
        }
    }

    @Override
    public void onPlay() {
        audioNode.play();
    }

    @Override
    public void onStop() {
        audioNode.stop();
    }

    @Override
    public void onPause() {
        audioNode.pause();
    }

    @Override
    public void onUpdate(float tpf) {
        if (audioNode.getStatus() == AudioSource.Status.Stopped) {
            stop();
        }
    }

    /**
     * Returns the underlying audio node of this sound track
     * @return the pre-existing instance
     */
    public AudioNode getAudioNode() {
        return audioNode;
    }

    @Override
    public void setLoopMode(LoopMode loopMode) {
        super.setLoopMode(loopMode);

        if (loopMode != LoopMode.DontLoop) {
            audioNode.setLooping(true);
        } else {
            audioNode.setLooping(false);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(path, "path", "");
        oc.write(stream, "stream", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        path = ic.readString("path", "");
        stream = ic.readBoolean("stream", false);
    }
}
