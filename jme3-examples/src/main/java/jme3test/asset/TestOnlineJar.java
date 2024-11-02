

package jme3test.asset;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;

/**
 * This tests loading a file from a jar stored online.
 * @author Kirill Vainer
 */
public class TestOnlineJar extends SimpleApplication {

    public static void main(String[] args){
        TestOnlineJar app = new TestOnlineJar();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // create a simple plane/quad
        Quad quadMesh = new Quad(1, 1);
        quadMesh.updateGeometry(1, 1, true);

        Geometry quad = new Geometry("Textured Quad", quadMesh);
        
        assetManager.registerLocator("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/town.zip", 
                                     HttpZipLocator.class);
        assetManager.registerLocator("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/wildhouse.zip", 
                                     HttpZipLocator.class);

        Picture pic1 = new Picture("Picture1");
        pic1.move(0, 0, -1); 
        pic1.setPosition(0, 0);
        pic1.setWidth(128);
        pic1.setHeight(128);
        pic1.setImage(assetManager, "grass.jpg", false);
        guiNode.attachChild(pic1);
        
        Picture pic2 = new Picture("Picture1");
        pic2.move(0, 0, -1); 
        pic2.setPosition(128, 0);
        pic2.setWidth(128);
        pic2.setHeight(128);
        pic2.setImage(assetManager, "glasstile2.png", false);
        guiNode.attachChild(pic2);
    }

}
