
package jme3test.texture.ktx;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.ktx.KTXLoader;
import com.jme3.ui.Picture;

/**
 * test
 * @author nehon
 */
public class TestLoadKtx extends SimpleApplication {

    public static void main(String[] args) {
        TestLoadKtx app = new TestLoadKtx();
        //app.setShowSettings(false);
        app.start();
    }
   
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        assetManager.registerLoader(KTXLoader.class, "ktx");
             
        
        Texture2D t = (Texture2D)assetManager.loadTexture("Textures/ktx/down-reference.ktx");
        Picture p = new Picture("bla", false);
        p.setTexture(assetManager, t, false);
        p.setLocalTranslation(200, 200, 0);
        p.setWidth(t.getImage().getWidth());
        p.setHeight(t.getImage().getHeight());
        guiNode.attachChild(p);
        
        
        Texture2D t2 = (Texture2D)assetManager.loadTexture("Textures/ktx/up-reference.ktx");
        Picture p2 = new Picture("bla", false);
        p2.setTexture(assetManager, t2, false);
        p2.setLocalTranslation(400, 200, 0);
        p2.setWidth(t2.getImage().getWidth());
        p2.setHeight(t2.getImage().getHeight());
        guiNode.attachChild(p2);
        
        Texture2D t3 = (Texture2D)assetManager.loadTexture("Textures/ktx/rgba-reference.ktx");
        Picture p3 = new Picture("bla", false);
        p3.setTexture(assetManager, t3, false);
        p3.setLocalTranslation(200, 400, 0);
        p3.setWidth(t3.getImage().getWidth());
        p3.setHeight(t3.getImage().getHeight());
        guiNode.attachChild(p3);

        
        Texture2D t4 = (Texture2D)assetManager.loadTexture("Textures/ktx/rgb-amg-reference.ktx");
        Picture p4 = new Picture("bla", false);
        p4.setTexture(assetManager, t4, false);
        p4.setLocalTranslation(400, 400, 0);
        p4.setWidth(t4.getImage().getWidth());
        p4.setHeight(t4.getImage().getHeight());
        guiNode.attachChild(p4);
        
        
        flyCam.setDragToRotate(true);
               
    }
    
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
