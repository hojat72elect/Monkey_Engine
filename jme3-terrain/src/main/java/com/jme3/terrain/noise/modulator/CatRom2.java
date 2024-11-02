
package com.jme3.terrain.noise.modulator;

import com.jme3.terrain.noise.ShaderUtils;
import java.util.HashMap;
import java.util.Map;

public class CatRom2 implements Modulator {

    private int sampleRate = 100;

    private final float[] table;

    private static Map<Integer, CatRom2> instances = new HashMap<Integer, CatRom2>();

    public CatRom2(final int sampleRate) {
        this.sampleRate = sampleRate;
        this.table = new float[4 * sampleRate + 1];
        for (int i = 0; i < 4 * sampleRate + 1; i++) {
            float x = i / (float) sampleRate;
            x = (float) Math.sqrt(x);
            if (x < 1) {
                this.table[i] = 0.5f * (2 + x * x * (-5 + x * 3));
            } else {
                this.table[i] = 0.5f * (4 + x * (-8 + x * (5 - x)));
            }
        }
    }

    public static CatRom2 getInstance(final int sampleRate) {
        if (!CatRom2.instances.containsKey(sampleRate)) {
            CatRom2.instances.put(sampleRate, new CatRom2(sampleRate));
        }
        return CatRom2.instances.get(sampleRate);
    }

    @Override
    public float value(final float... in) {
        if (in[0] >= 4) {
            return 0;
        }
        in[0] = in[0] * this.sampleRate + 0.5f;
        int i = ShaderUtils.floor(in[0]);
        if (i >= 4 * this.sampleRate + 1) {
            return 0;
        }
        return this.table[i];
    }
}
