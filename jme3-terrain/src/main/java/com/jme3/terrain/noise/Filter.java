
package com.jme3.terrain.noise;

import java.nio.FloatBuffer;

public interface Filter {
    public Filter addPreFilter(Filter filter);

    public Filter addPostFilter(Filter filter);

    public FloatBuffer doFilter(float sx, float sy, float base, FloatBuffer data, int size);

    public int getMargin(int size, int margin);

    public boolean isEnabled();
}
