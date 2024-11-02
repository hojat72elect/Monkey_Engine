
package com.jme3.terrain.noise;

import com.jme3.terrain.noise.basis.ImprovedNoise;
import com.jme3.terrain.noise.modulator.Modulator;
import java.nio.FloatBuffer;

/**
 * Interface for - basically 3D - noise generation algorithms, based on the
 * book: Texturing &amp; Modeling - A Procedural Approach
 * <p>
 * The main concept is to look at noise as a basis for generating fractals.
 * Basis can be anything, like a simple:
 * <p>
 * <code>
 * float value(float x, float y, float z) {
 *     return 0; // a flat noise with 0 value everywhere
 * }
 * </code>
 * <p>
 * or a more complex perlin noise ({@link ImprovedNoise}
 * 
 * Fractals use these functions to generate a more complex result based on some
 * frequency, roughness, etcetera values.
 * <p>
 * Fractals themselves are implementing the Basis interface as well, opening
 * an infinite range of results.
 */
public interface Basis {

    public void init();

    public Basis setScale(float scale);

    public float getScale();

    public Basis addModulator(Modulator modulator);

    public float value(float x, float y, float z);

    public FloatBuffer getBuffer(float sx, float sy, float base, int size);

}
