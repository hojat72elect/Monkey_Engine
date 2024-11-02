
package com.jme3.terrain.noise.filter;

import java.nio.FloatBuffer;

public class ThermalErodeFilter extends AbstractFilter {

    private float talus;
    private float c;

    public ThermalErodeFilter setC(float c) {
        this.c = c;
        return this;
    }

    public ThermalErodeFilter setTalus(float talus) {
        this.talus = talus;
        return this;
    }

    @Override
    public int getMargin(int size, int margin) {
        return super.getMargin(size, margin) + 1;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer buffer, int workSize) {
        float[] ga = buffer.array();
        float[] sa = new float[workSize * workSize];

        int[] idxrel = { -workSize - 1, -workSize + 1, workSize - 1, workSize + 1 };

        for (int y = 0; y < workSize; y++) {
            for (int x = 0; x < workSize; x++) {
                int idx = y * workSize + x;
                ga[idx] += sa[idx];
                sa[idx] = 0;

                float[] deltas = new float[idxrel.length];
                float deltaMax = this.talus;
                float deltaTotal = 0;

                for (int j = 0; j < idxrel.length; j++) {
                    if (idx + idxrel[j] > 0 && idx + idxrel[j] < ga.length) {
                        float dj = ga[idx] - ga[idx + idxrel[j]];
                        if (dj > this.talus) {
                            deltas[j] = dj;
                            deltaTotal += dj;
                            if (dj > deltaMax) {
                                deltaMax = dj;
                            }
                        }
                    }
                }

                for (int j = 0; j < idxrel.length; j++) {
                    if (deltas[j] != 0) {
                        float d = this.c * (deltaMax - this.talus) * deltas[j] / deltaTotal;
                        if (d > ga[idx] + sa[idx]) {
                            d = ga[idx] + sa[idx];
                        }
                        sa[idx] -= d;
                        sa[idx + idxrel[j]] += d;
                    }
                    deltas[j] = 0;
                }
            }
        }

        return buffer;
    }

}
