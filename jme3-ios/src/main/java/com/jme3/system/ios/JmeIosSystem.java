
package com.jme3.system.ios;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystemDelegate;
import com.jme3.system.NullContext;
import com.jme3.util.res.Resources;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.ios.IosAL;
import com.jme3.audio.ios.IosALC;
//import com.jme3.audio.ios.IosEFX;
import com.jme3.audio.openal.AL;
import com.jme3.audio.openal.ALAudioRenderer;
import com.jme3.audio.openal.ALC;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 *
 * @author normenhansen
 */
public class JmeIosSystem extends JmeSystemDelegate {

    public JmeIosSystem() {
        setErrorMessageHandler((message) -> {
            showDialog(message);
            System.err.println("JME APPLICATION ERROR:" + message);
        });       
    }

    @Override
    public URL getPlatformAssetConfigURL() {
        return Resources.getResource("com/jme3/asset/IOS.cfg");
    }
    
    @Override
    public void writeImageFile(OutputStream outStream, String format, ByteBuffer imageData, int width, int height) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   

    private native void showDialog(String message);

    

    @Override
    public JmeContext newContext(AppSettings settings, JmeContext.Type contextType) {
        initialize(settings);
        JmeContext ctx = null;
        if (settings.getRenderer() == null
                || settings.getRenderer().equals("NULL")
                || contextType == JmeContext.Type.Headless) {
            ctx = new NullContext();
            ctx.setSettings(settings);
        } else {
            ctx = new IGLESContext();
            ctx.setSettings(settings);
        }
        return ctx;
    }

    @Override
    public AudioRenderer newAudioRenderer(AppSettings settings) {
        ALC alc = new IosALC();
        AL al = new IosAL();
        //EFX efx = new IosEFX();
        return new ALAudioRenderer(al, alc, null);
     }

    @Override
    public void initialize(AppSettings settings) {
        Logger.getLogger("").addHandler(new IosLogHandler());
//                throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showSoftKeyboard(boolean show) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}