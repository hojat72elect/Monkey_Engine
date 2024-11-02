
package com.jme3.terrain.noise.filter;

import com.jme3.terrain.noise.Filter;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class IterativeFilter extends AbstractFilter {

    private int iterations;

    private List<Filter> preIterateFilters = new ArrayList<>();
    private List<Filter> postIterateFilters = new ArrayList<>();
    private Filter filter;

    @Override
    public int getMargin(int size, int margin) {
        if (!this.isEnabled()) {
            return margin;
        }
        for (Filter f : this.preIterateFilters) {
            margin = f.getMargin(size, margin);
        }
        margin = this.filter.getMargin(size, margin);
        for (Filter f : this.postIterateFilters) {
            margin = f.getMargin(size, margin);
        }
        return this.iterations * margin + super.getMargin(size, margin);
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public IterativeFilter addPostIterateFilter(Filter filter) {
        this.postIterateFilters.add(filter);
        return this;
    }

    public IterativeFilter addPreIterateFilter(Filter filter) {
        this.preIterateFilters.add(filter);
        return this;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public FloatBuffer filter(float sx, float sy, float base, FloatBuffer data, int size) {
        if (!this.isEnabled()) {
            return data;
        }
        FloatBuffer retval = data;

        for (int i = 0; i < this.iterations; i++) {
            for (Filter f : this.preIterateFilters) {
                retval = f.doFilter(sx, sy, base, retval, size);
            }
            retval = this.filter.doFilter(sx, sy, base, retval, size);
            for (Filter f : this.postIterateFilters) {
                retval = f.doFilter(sx, sy, base, retval, size);
            }
        }

        return retval;
    }
}
