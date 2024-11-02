
package jme3test.app;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 * Tests the capability to resize the application window.
 * 
 * @author Kirill Vainer
 */
public class TestResizableApp extends SimpleApplication {
    
    private BitmapText txt;
    
    public static void main(String[] args){
        TestResizableApp app = new TestResizableApp();
        AppSettings settings = new AppSettings(true);
        settings.setResizable(true);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void reshape(int width, int height) {
        super.reshape(width, height);

        // Need to move text relative to app height
        txt.setLocalTranslation(0, settings.getHeight(), 0);
        txt.setText("Drag the corners of the application to resize it.\n" +
                    "Current Size: " + settings.getWidth() + "x" + settings.getHeight());
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setDragToRotate(true);
        
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        
        txt = new BitmapText(loadGuiFont());
        txt.setText("Drag the corners of the application to resize it.\n" +
                    "Current Size: " + settings.getWidth() + "x" + settings.getHeight());
        txt.setLocalTranslation(0, settings.getHeight(), 0);
        guiNode.attachChild(txt);
    }

}
