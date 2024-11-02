
package com.jme3.asset;

import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.shader.plugins.GLSLLoader;
import com.jme3.system.JmeSystem;
import com.jme3.system.MockJmeSystemDelegate;
import org.junit.Test;

public class LoadShaderSourceTest {

    @Test
    public void testLoadShaderSource() {
        JmeSystem.setSystemDelegate(new MockJmeSystemDelegate());
        AssetManager assetManager = new DesktopAssetManager();
        assetManager.registerLocator(null, ClasspathLocator.class);
        assetManager.registerLoader(GLSLLoader.class, "frag");
        assetManager.registerLoader(GLSLLoader.class, "glsllib");
        assetManager.registerLoader(GLSLLoader.class, "glsl");
        String showNormals = (String) assetManager.loadAsset("Common/MatDefs/Misc/ShowNormals.frag");
        System.out.println(showNormals);
    }
    
}
