
package com.jme3.terrain.noise.fractal;

import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.basis.ImprovedNoise;
import com.jme3.terrain.noise.basis.Noise;

/**
 * FractalSum is the simplest form of fractal functions summing up a few octaves
 * of the noise value with an ever decreasing roughness (0 to 1) amplitude
 * 
 * lacunarity = 2.0f is the classical octave distance
 * 
 * Note: though noise basis functions are generally designed to return value
 * between -1..1, there sum can easily be made to extend out of this range. To
 * handle this is up to the user.
 * 
 * 
 * 
 */
public class FractalSum extends Noise implements Fractal {

    private Basis basis;
    private float lacunarity;
    private float amplitude;
    private float roughness;
    private float frequency;
    private float octaves;
    private int maxFreq;

    public FractalSum() {
        this.basis = new ImprovedNoise();
        this.lacunarity = 2.124367f;
        this.amplitude = 1.0f;
        this.roughness = 0.6f;
        this.frequency = 1f;
        this.setOctaves(1);
    }

    @Override
    public float value(final float x, final float y, final float z) {
        float total = 0;

        for (float f = this.frequency, a = this.amplitude; f < this.maxFreq; f *= this.lacunarity, a *= this.roughness) {
            total += this.basis.value(this.scale * x * f, this.scale * y * f, this.scale * z * f) * a;
        }

        return ShaderUtils.clamp(total, -1, 1);
    }

    @Override
    public Fractal addBasis(final Basis basis) {
        this.basis = basis;
        return this;
    }

    public float getOctaves() {
        return this.octaves;
    }

    @Override
    public Fractal setOctaves(final float octaves) {
        this.octaves = octaves;
        this.maxFreq = 1 << (int) octaves;
        return this;
    }

    public float getFrequency() {
        return this.frequency;
    }

    @Override
    public Fractal setFrequency(final float frequency) {
        this.frequency = frequency;
        return this;
    }

    public float getRoughness() {
        return this.roughness;
    }

    @Override
    public Fractal setRoughness(final float roughness) {
        this.roughness = roughness;
        return this;
    }

    public float getAmplitude() {
        return this.amplitude;
    }

    @Override
    public Fractal setAmplitude(final float amplitude) {
        this.amplitude = amplitude;
        return this;
    }

    public float getLacunarity() {
        return this.lacunarity;
    }

    @Override
    public Fractal setLacunarity(final float lacunarity) {
        this.lacunarity = lacunarity;
        return this;
    }

    @Override
    public void init() {

    }

}
