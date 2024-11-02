
package com.jme3.input.dummy;

import com.jme3.input.KeyInput;

/**
 * DummyKeyInput as an implementation of <code>KeyInput</code> that raises no
 * input events.
 * 
 * @author Kirill Vainer.
 */
public class DummyKeyInput extends DummyInput implements KeyInput {

    public int getKeyCount() {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");

        return 0;
    }

    
    @Override
    public String getKeyName(int key){
        return "Unknown";
    }

}
