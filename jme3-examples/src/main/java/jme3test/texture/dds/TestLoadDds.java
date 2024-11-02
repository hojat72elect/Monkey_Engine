
package jme3test.texture.dds;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.DDSLoader;
import com.jme3.ui.Picture;

/**
 * Test various supported BC* textures in DDS file format
 * 
 * @author Toni Helenius
 */
public class TestLoadDds extends SimpleApplication {

    public static void main(String[] args) {
        TestLoadDds app = new TestLoadDds();
        //app.setShowSettings(false);
        app.start();
    }
   
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        assetManager.registerLoader(DDSLoader.class, "dds");
           
        loadTexture(0, "Textures/dds/Monkey_PNG_BC7_1.DDS", "BC7");
        loadTexture(1, "Textures/dds/Monkey_PNG_BC6H_3.DDS", "BC6");
        loadTexture(2, "Textures/dds/Monkey_PNG_BC6H_SF_2.DDS", "BC6_SF");
        loadTexture(3, "Textures/dds/Monkey_PNG_BC5_S_6.DDS", "BC5_S");
        loadTexture(4, "Textures/dds/Monkey_PNG_BC5_7.DDS", "BC5");
        loadTexture(5, "Textures/dds/Monkey_PNG_BC4_S_8.DDS", "BC4_S");
        loadTexture(6, "Textures/dds/Monkey_PNG_BC4_9.DDS", "BC4");
        loadTexture(7, "Textures/dds/Monkey_PNG_BC3_10.DDS", "BC3");
        loadTexture(8, "Textures/dds/Monkey_PNG_BC2_11.DDS", "BC2");
        loadTexture(9, "Textures/dds/Monkey_PNG_BC1_12.DDS", "BC1");
        
        flyCam.setDragToRotate(true);
               
    }

    private void loadTexture(int index, String texture, String description) {
        Texture2D t = (Texture2D)assetManager.loadTexture(new TextureKey(texture, false));
        Picture p = new Picture(description, true);
        p.setTexture(assetManager, t, false);
        p.setLocalTranslation((index % 4) * 200, Math.floorDiv(index, 4) * 200, 0);
        p.setWidth(200);
        p.setHeight(200);
        guiNode.attachChild(p);
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
