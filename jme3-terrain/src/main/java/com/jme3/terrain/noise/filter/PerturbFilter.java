
package com.jme3.terrain.noise.filter;

import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.fractal.FractalSum;
import java.nio.FloatBuffer;
import java.util.logging.Logger;

public class PerturbFilter extends AbstractFilter {

    private float magnitude;

    @Override
    public int getMargin(int size, int margin) {
        margin = super.getMargin(size, margin);
        return (int) Math.floor(this.magnitude * (margin + size) + margin);
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getMagnitude() {
        return this.magnitude;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer data, int workSize) {
        float[] arr = data.array();
        int origSize = (int) Math.ceil(workSize / (2 * this.magnitude + 1));
        int offset = (workSize - origSize) / 2;
        Logger.getLogger(PerturbFilter.class.getCanonicalName()).info(
                "Found origSize : " + origSize + " and offset: " + offset + " for workSize : " + workSize + " and magnitude : "
                        + this.magnitude);
        float[] retval = new float[workSize * workSize];
        float[] perturbX = new FractalSum().setOctaves(8).setScale(5f).getBuffer(sx, sy, base, workSize).array();
        float[] perturbY = new FractalSum().setOctaves(8).setScale(5f).getBuffer(sx, sy, base + 1, workSize).array();
        for (int y = 0; y < workSize; y++) {
            for (int x = 0; x < workSize; x++) {
                // Perturb our coordinates
                float noiseX = perturbX[y * workSize + x];
                float noiseY = perturbY[y * workSize + x];

                int px = (int) (origSize * noiseX * this.magnitude);
                int py = (int) (origSize * noiseY * this.magnitude);

                float c00 = arr[this.wrap(y - py, workSize) * workSize + this.wrap(x - px, workSize)];
                float c01 = arr[this.wrap(y - py, workSize) * workSize + this.wrap(x + px, workSize)];
                float c10 = arr[this.wrap(y + py, workSize) * workSize + this.wrap(x - px, workSize)];
                float c11 = arr[this.wrap(y + py, workSize) * workSize + this.wrap(x + px, workSize)];

                float c0 = ShaderUtils.mix(c00, c01, noiseX);
                float c1 = ShaderUtils.mix(c10, c11, noiseX);
                retval[y * workSize + x] = ShaderUtils.mix(c0, c1, noiseY);
            }
        }
        return FloatBuffer.wrap(retval);
    }

    private int wrap(int v, int size) {
        if (v < 0) {
            return v + size - 1;
        } else if (v >= size) {
            return v - size;
        } else {
            return v;
        }
    }
}
