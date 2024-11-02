
package com.jme3.terrain.noise.basis;

import com.jme3.terrain.noise.Basis;

/**
 * A simple aggregator basis. Takes two basis functions and a rate and return
 * some mixed values
 * 
 * 
 * 
 */
public class NoiseAggregator extends Noise {

    private final float rate;
    private final Basis a;
    private final Basis b;

    public NoiseAggregator(final Basis a, final Basis b, final float rate) {
        this.a = a;
        this.b = b;
        this.rate = rate;
    }

    @Override
    public void init() {
        this.a.init();
        this.b.init();
    }

    @Override
    public float value(final float x, final float y, final float z) {
        return this.a.value(x, y, z) * (1 - this.rate) + this.rate * this.b.value(x, y, z);
    }

}
