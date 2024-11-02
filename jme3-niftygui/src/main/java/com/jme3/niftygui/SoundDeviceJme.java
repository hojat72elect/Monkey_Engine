
package com.jme3.niftygui;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class SoundDeviceJme implements SoundDevice {

    protected AssetManager assetManager;
    protected AudioRenderer ar;

    public SoundDeviceJme(AssetManager assetManager, AudioRenderer ar) {
        this.assetManager = assetManager;
        this.ar = ar;
    }

    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
    }

    @Override
    public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
        AudioNode an = new AudioNode(assetManager, filename, AudioData.DataType.Buffer);
        an.setPositional(false);
        return new SoundHandleJme(ar, an);
    }

    @Override
    public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
        return new SoundHandleJme(ar, assetManager, filename);
    }

    @Override
    public void update(int delta) {
    }
}
