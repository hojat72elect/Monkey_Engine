
package com.jme3.terrain.noise.basis;

import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.filter.AbstractFilter;
import com.jme3.terrain.noise.modulator.Modulator;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class FilteredBasis extends AbstractFilter implements Basis {

    private Basis basis;
    private List<Modulator> modulators = new ArrayList<>();
    private float scale;

    public FilteredBasis() {}

    public FilteredBasis(Basis basis) {
        this.basis = basis;
    }

    public Basis getBasis() {
        return this.basis;
    }

    public void setBasis(Basis basis) {
        this.basis = basis;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer data, int size) {
        return data;
    }

    @Override
    public void init() {
        this.basis.init();
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

    @Override
    public Basis addModulator(Modulator modulator) {
        this.modulators.add(modulator);
        return this;
    }

    @Override
    public float value(float x, float y, float z) {
        throw new UnsupportedOperationException(
                "Method value cannot be called on FilteredBasis and its descendants. Use getBuffer instead!");
    }

    @Override
    public FloatBuffer getBuffer(float sx, float sy, float base, int size) {
        int margin = this.getMargin(size, 0);
        int workSize = size + 2 * margin;
        FloatBuffer retval = this.basis.getBuffer(sx - margin, sy - margin, base, workSize);
        return this.clip(this.doFilter(sx, sy, base, retval, workSize), workSize, size, margin);
    }

    public FloatBuffer clip(FloatBuffer buf, int origSize, int newSize, int offset) {
        FloatBuffer result = FloatBuffer.allocate(newSize * newSize);

        float[] orig = buf.array();
        for (int i = offset; i < offset + newSize; i++) {
            result.put(orig, i * origSize + offset, newSize);
        }

        return result;
    }
}
