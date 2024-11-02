
package com.jme3.system.lwjgl;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

/**
 * @author Daniel Johansson
 */
public class LwjglOffscreenBuffer extends LwjglWindow {

    private KeyInput keyInput;
    private MouseInput mouseInput;

    public LwjglOffscreenBuffer() {
        super(JmeContext.Type.OffscreenSurface);
    }

    @Override
    protected void showWindow() {
    }

    @Override
    protected void setWindowIcon(final AppSettings settings) {
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public MouseInput getMouseInput() {
        if (mouseInput == null) {
            mouseInput = new DummyMouseInput();
        }

        return mouseInput;
    }

    @Override
    public KeyInput getKeyInput() {
        if (keyInput == null) {
            keyInput = new DummyKeyInput();
        }

        return keyInput;
    }

    @Override
    public JoyInput getJoyInput() {
        return null;
    }

    @Override
    public TouchInput getTouchInput() {
        return null;
    }
}
