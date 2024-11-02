
package com.jme3.terrain.noise.fractal;

import com.jme3.terrain.noise.Basis;

/**
 * Interface for a general fractal basis.
 * 
 * Takes any number of basis functions to work with and a few common parameters
 * for noise fractals
 * 
 * 
 * 
 */
public interface Fractal extends Basis {

    public Fractal setOctaves(final float octaves);

    public Fractal setFrequency(final float frequency);

    public Fractal setRoughness(final float roughness);

    public Fractal setAmplitude(final float amplitude);

    public Fractal setLacunarity(final float lacunarity);

    public Fractal addBasis(Basis basis);

}
