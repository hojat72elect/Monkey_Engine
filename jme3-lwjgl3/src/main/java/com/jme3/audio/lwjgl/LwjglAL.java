package com.jme3.audio.lwjgl;

import com.jme3.audio.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * The LWJGL implementation of {@link AL}.
 */
public final class LwjglAL implements AL {

    public LwjglAL() {
    }

    @Override
    public String alGetString(final int parameter) {
        return AL10.alGetString(parameter);
    }

    @Override
    public int alGenSources() {
        return AL10.alGenSources();
    }

    @Override
    public int alGetError() {
        return AL10.alGetError();
    }

    @Override
    public void alDeleteSources(final int numSources, final IntBuffer sources) {
        if (sources.position() != 0) throw new AssertionError();
        if (sources.limit() != numSources) throw new AssertionError();
        AL10.alDeleteSources(sources);
    }

    @Override
    public void alGenBuffers(final int numBuffers, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numBuffers) throw new AssertionError();
        AL10.alGenBuffers(buffers);
    }

    @Override
    public void alDeleteBuffers(final int numBuffers, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numBuffers) throw new AssertionError();
        AL10.alDeleteBuffers(buffers);
    }

    @Override
    public void alSourceStop(final int source) {
        AL10.alSourceStop(source);
    }

    @Override
    public void alSourcei(final int source, final int param, final int value) {
        AL10.alSourcei(source, param, value);
    }

    @Override
    public void alBufferData(final int buffer, final int format, final ByteBuffer data, final int size, final int frequency) {
        if (data.position() != 0) throw new AssertionError();
        if (data.limit() != size) throw new AssertionError();
        AL10.alBufferData(buffer, format, data, frequency);
    }

    @Override
    public void alSourcePlay(final int source) {
        AL10.alSourcePlay(source);
    }

    @Override
    public void alSourcePause(final int source) {
        AL10.alSourcePause(source);
    }

    @Override
    public void alSourcef(final int source, final int param, final float value) {
        AL10.alSourcef(source, param, value);
    }

    @Override
    public void alSource3f(final int source, final int param, final float value1, final float value2, final float value3) {
        AL10.alSource3f(source, param, value1, value2, value3);
    }

    @Override
    public int alGetSourcei(final int source, final int param) {
        return AL10.alGetSourcei(source, param);
    }

    @Override
    public void alSourceUnqueueBuffers(final int source, final int numBuffers, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numBuffers) throw new AssertionError();
        AL10.alSourceUnqueueBuffers(source, buffers);
    }

    @Override
    public void alSourceQueueBuffers(final int source, final int numBuffers, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numBuffers) throw new AssertionError();
        AL10.alSourceQueueBuffers(source, buffers);
    }

    @Override
    public void alListener(final int param, final FloatBuffer data) {
        AL10.alListenerfv(param, data);
    }

    @Override
    public void alListenerf(final int param, final float value) {
        AL10.alListenerf(param, value);
    }

    @Override
    public void alListener3f(final int param, final float value1, final float value2, final float value3) {
        AL10.alListener3f(param, value1, value2, value3);
    }

    @Override
    public void alSource3i(final int source, final int param, final int value1, final int value2, final int value3) {
        AL11.alSource3i(source, param, value1, value2, value3);
    }
}
