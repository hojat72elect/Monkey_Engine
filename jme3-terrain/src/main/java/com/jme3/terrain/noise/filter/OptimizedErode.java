
package com.jme3.terrain.noise.filter;

import java.nio.FloatBuffer;

public class OptimizedErode extends AbstractFilter {

    private float talus;
    private int radius;

    public OptimizedErode setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getRadius() {
        return this.radius;
    }

    public OptimizedErode setTalus(float talus) {
        this.talus = talus;
        return this;
    }

    public float getTalus() {
        return this.talus;
    }

    @Override
    public int getMargin(int size, int margin) {
        return super.getMargin(size, margin) + this.radius;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer buffer, int size) {
        float[] tmp = buffer.array();
        float[] retval = new float[tmp.length];

        for (int y = this.radius + 1; y < size - this.radius; y++) {
            for (int x = this.radius + 1; x < size - this.radius; x++) {
                int idx = y * size + x;
                float h = tmp[idx];

                float horizAvg = 0;
                int horizCount = 0;
                float vertAvg = 0;
                int vertCount = 0;

                boolean horizT = false;
                boolean vertT = false;

                for (int i = 0; i >= -this.radius; i--) {
                    int idxV = (y + i) * size + x;
                    int idxVL = (y + i - 1) * size + x;
                    int idxH = y * size + x + i;
                    int idxHL = y * size + x + i - 1;
                    float hV = tmp[idxV];
                    float hH = tmp[idxH];

                    if (Math.abs(h - hV) > this.talus && Math.abs(h - tmp[idxVL]) > this.talus || vertT) {
                        vertT = true;
                    } else {
                        if (Math.abs(h - hV) <= this.talus) {
                            vertAvg += hV;
                            vertCount++;
                        }
                    }

                    if (Math.abs(h - hH) > this.talus && Math.abs(h - tmp[idxHL]) > this.talus || horizT) {
                        horizT = true;
                    } else {
                        if (Math.abs(h - hH) <= this.talus) {
                            horizAvg += hH;
                            horizCount++;
                        }
                    }
                }

                retval[idx] = 0.5f * (vertAvg / (vertCount > 0 ? vertCount : 1) + horizAvg / (horizCount > 0 ? horizCount : 1));
            }
        }
        return FloatBuffer.wrap(retval);
    }

}
