
package com.jme3.audio.lwjgl;

import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * The LWJGL implementation of {@link com.jme3.audio.openal.ALC}.
 */
public class LwjglALC implements com.jme3.audio.openal.ALC {

    /**
     * The device id.
     */
    private long device;

    /**
     * The context id.
     */
    private long context;

    @Override
    public void createALC() {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    @Override
    public void destroyALC() {
        if (context != 0) {
            ALC10.alcDestroyContext(context);
            context = 0;
        }

        if (device != 0) {
            ALC10.alcCloseDevice(device);
            device = 0;
        }
    }

    @Override
    public boolean isCreated() {
        return context != 0;
    }

    @Override
    public String alcGetString(final int parameter) {
        return ALC10.alcGetString(device, parameter);
    }

    @Override
    public boolean alcIsExtensionPresent(final String extension) {
        return ALC10.alcIsExtensionPresent(device, extension);
    }

    @Override
    public void alcGetInteger(final int param, final IntBuffer buffer, final int size) {
        if (buffer.position() != 0) {
            throw new AssertionError();
        }
        if (buffer.limit() != size) {
            throw new AssertionError();
        }
        ALC10.alcGetIntegerv(device, param, buffer);
    }

    @Override
    public void alcDevicePauseSOFT() {
        SOFTPauseDevice.alcDevicePauseSOFT(device);
    }

    @Override
    public void alcDeviceResumeSOFT() {
        SOFTPauseDevice.alcDeviceResumeSOFT(device);
    }
}
