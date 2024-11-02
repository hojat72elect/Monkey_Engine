
package com.jme3.system;

import com.jme3.audio.AudioRenderer;
import com.jme3.util.res.Resources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class MockJmeSystemDelegate extends JmeSystemDelegate {

    @Override
    public void writeImageFile(OutputStream outStream, String format, ByteBuffer imageData, int width, int height) throws IOException {
    }

    @Override
    public URL getPlatformAssetConfigURL() {
        return Resources.getResource("com/jme3/asset/General.cfg");
    }

    @Override
    public JmeContext newContext(AppSettings settings, JmeContext.Type contextType) {
        return null;
    }

    @Override
    public AudioRenderer newAudioRenderer(AppSettings settings) {
        return null;
    }

    @Override
    public void initialize(AppSettings settings) {
    }

    @Override
    public void showSoftKeyboard(boolean show) {
    }
    
}
