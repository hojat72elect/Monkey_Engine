
package com.jme3.audio;

import com.jme3.util.BufferUtils;
import com.jme3.util.NativeObject;

import java.nio.ByteBuffer;

/**
 * An <code>AudioBuffer</code> is an implementation of AudioData
 * where the audio is buffered (stored in memory). All parts of it
 * are accessible at any time. <br>
 * AudioBuffers are useful for short sounds, like effects, etc.
 *
 * @author Kirill Vainer
 */
public class AudioBuffer extends AudioData {

    /**
     * The audio data buffer. Should be direct and native ordered.
     */
    protected ByteBuffer audioData;

    public AudioBuffer() {
        super();
    }

    protected AudioBuffer(int id) {
        super(id);
    }

    @Override
    public DataType getDataType() {
        return DataType.Buffer;
    }

    /**
     * @return The duration of the audio in seconds. It is expected
     * that audio is uncompressed.
     */
    @Override
    public float getDuration() {
        int bytesPerSec = (bitsPerSample / 8) * channels * sampleRate;
        if (audioData != null)
            return (float) audioData.limit() / bytesPerSec;
        else
            return Float.NaN; // unknown
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[id=" + id + ", ch=" + channels + ", bits=" + bitsPerSample
                + ", rate=" + sampleRate + ", duration=" + getDuration() + "]";
    }

    /**
     * Update the data in the buffer with new data.
     *
     * @param data the audio data provided (not null, direct, alias created)
     * @throws IllegalArgumentException if the provided buffer is not a direct buffer
     */
    public void updateData(ByteBuffer data) {
        if (!data.isDirect()) {
            throw new IllegalArgumentException(
                    "Currently only direct buffers are allowed");
        }

        this.audioData = data;
        updateNeeded = true;
    }

    /**
     * @return The buffered audio data.
     */
    public ByteBuffer getData() {
        return audioData;
    }

    @Override
    public void resetObject() {
        id = -1;
        setUpdateNeeded();
    }

    @Override
    protected void deleteNativeBuffers() {
        if (audioData != null) {
            BufferUtils.destroyDirectBuffer(audioData);
        }
    }

    @Override
    public void deleteObject(Object rendererObject) {
        ((AudioRenderer) rendererObject).deleteAudioData(this);
    }

    @Override
    public NativeObject createDestructableClone() {
        return new AudioBuffer(id);
    }

    @Override
    public long getUniqueId() {
        return ((long) OBJTYPE_AUDIOBUFFER << 32) | (0xffffffffL & (long) id);
    }
}
