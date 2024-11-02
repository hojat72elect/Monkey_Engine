
package com.jme3.terrain.noise.filter;

import java.nio.FloatBuffer;

public class SmoothFilter extends AbstractFilter {

    private int radius;
    private float effect;

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setEffect(float effect) {
        this.effect = effect;
    }

    public float getEffect() {
        return this.effect;
    }

    @Override
    public int getMargin(int size, int margin) {
        return super.getMargin(size, margin) + this.radius;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer buffer, int size) {
        float[] data = buffer.array();
        float[] retval = new float[data.length];

        for (int y = this.radius; y < size - this.radius; y++) {
            for (int x = this.radius; x < size - this.radius; x++) {
                int idx = y * size + x;
                float n = 0;
                for (int i = -this.radius; i < this.radius + 1; i++) {
                    for (int j = -this.radius; j < this.radius + 1; j++) {
                        n += data[(y + i) * size + x + j];
                    }
                }
                retval[idx] = this.effect * n / (4 * this.radius * (this.radius + 1) + 1) + (1 - this.effect) * data[idx];
            }
        }

        return FloatBuffer.wrap(retval);
    }
}
