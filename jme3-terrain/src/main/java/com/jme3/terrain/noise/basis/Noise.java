
package com.jme3.terrain.noise.basis;

import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.modulator.Modulator;
import com.jme3.terrain.noise.modulator.NoiseModulator;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility base class for Noise implementations
 * 
 * 
 * 
 */
public abstract class Noise implements Basis {

    protected List<Modulator> modulators = new ArrayList<>();

    protected float scale = 1.0f;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public FloatBuffer getBuffer(float sx, float sy, float base, int size) {
        FloatBuffer retval = FloatBuffer.allocate(size * size);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                retval.put(this.modulate((sx + x) / size, (sy + y) / size, base));
            }
        }
        return retval;
    }

    public float modulate(float x, float y, float z) {
        float retval = this.value(x, y, z);
        for (Modulator m : this.modulators) {
            if (m instanceof NoiseModulator) {
                retval = m.value(retval);
            }
        }
        return retval;
    }

    @Override
    public Basis addModulator(Modulator modulator) {
        this.modulators.add(modulator);
        return this;
    }

    @Override
    public Basis setScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public float getScale() {
        return this.scale;
    }
}
