
package com.jme3.audio.lwjgl;

import com.jme3.audio.openal.EFX;
import org.lwjgl.openal.EXTEfx;

import java.nio.IntBuffer;

/**
 * The LWJGL implementation of {@link EFX}.
 */
public class LwjglEFX implements EFX {

    @Override
    public void alGenAuxiliaryEffectSlots(final int numSlots, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numSlots) throw new AssertionError();
        EXTEfx.alGenAuxiliaryEffectSlots(buffers);
    }

    @Override
    public void alGenEffects(final int numEffects, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numEffects) throw new AssertionError();
        EXTEfx.alGenEffects(buffers);
    }

    @Override
    public void alEffecti(final int effect, final int param, final int value) {
        EXTEfx.alEffecti(effect, param, value);
    }

    @Override
    public void alAuxiliaryEffectSloti(final int effectSlot, final int param, final int value) {
        EXTEfx.alAuxiliaryEffectSloti(effectSlot, param, value);
    }

    @Override
    public void alDeleteEffects(final int numEffects, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numEffects) throw new AssertionError();
        EXTEfx.alDeleteEffects(buffers);
    }

    @Override
    public void alDeleteAuxiliaryEffectSlots(final int numEffectSlots, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numEffectSlots) throw new AssertionError();
        EXTEfx.alDeleteAuxiliaryEffectSlots(buffers);
    }

    @Override
    public void alGenFilters(final int numFilters, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numFilters) throw new AssertionError();
        EXTEfx.alGenFilters(buffers);
    }

    @Override
    public void alFilteri(final int filter, final int param, final int value) {
        EXTEfx.alFilteri(filter, param, value);
    }

    @Override
    public void alFilterf(final int filter, final int param, final float value) {
        EXTEfx.alFilterf(filter, param, value);
    }

    @Override
    public void alDeleteFilters(final int numFilters, final IntBuffer buffers) {
        if (buffers.position() != 0) throw new AssertionError();
        if (buffers.limit() != numFilters) throw new AssertionError();
        EXTEfx.alDeleteFilters(buffers);
    }

    @Override
    public void alEffectf(final int effect, final int param, final float value) {
        EXTEfx.alEffectf(effect, param, value);
    }
}
