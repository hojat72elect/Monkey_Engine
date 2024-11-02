
package com.jme3.input.dummy;

import com.jme3.input.Input;
import com.jme3.input.RawInputListener;

/**
 * DummyInput as an implementation of <code>Input</code> that raises no
 * input events.
 * 
 * @author Kirill Vainer.
 */
public class DummyInput implements Input {

    protected boolean inited = false;

    @Override
    public void initialize() {
        if (inited)
            throw new IllegalStateException("Input already initialized.");

        inited = true;
    }

    @Override
    public void update() {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");
    }

    @Override
    public void destroy() {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");

        inited = false;
    }

    @Override
    public boolean isInitialized() {
        return inited;
    }

    @Override
    public void setInputListener(RawInputListener listener) {
    }

    @Override
    public long getInputTimeNanos() {
        return System.currentTimeMillis() * 1000000;
    }

}
