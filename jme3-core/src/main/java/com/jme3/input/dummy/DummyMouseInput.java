
package com.jme3.input.dummy;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.MouseInput;

/**
 * DummyMouseInput as an implementation of <code>MouseInput</code> that raises no
 * input events.
 *
 * @author Kirill Vainer.
 */
public class DummyMouseInput extends DummyInput implements MouseInput {

    @Override
    public void setCursorVisible(boolean visible) {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");
    }

    @Override
    public int getButtonCount() {
        return 0;
    }

    @Override
    public void setNativeCursor(JmeCursor cursor) {
    }

}
