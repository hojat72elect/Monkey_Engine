
package com.jme3.system;

import com.jme3.asset.AssetConfig;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestUtil {
    
    static {
        JmeSystem.setSystemDelegate(new MockJmeSystemDelegate());
    }
    
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private TestUtil() {
    }

    public static AssetManager createAssetManager() {
        Logger.getLogger(AssetConfig.class.getName()).setLevel(Level.OFF);
        return new DesktopAssetManager(true);
    }
    
    public static RenderManager createRenderManager() {        
        return createRenderManager(new NullRenderer());
    }

    public static RenderManager createRenderManager(Renderer renderer) {
        RenderManager rm = new RenderManager(renderer);
        rm.setPassDrawBufferTargetIdToShaders(false);
        return rm;
    }
}
