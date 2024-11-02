
package jme3test.model;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.ModelKey;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class TestObjGroupsLoading extends SimpleApplication {
    
    public static void main(String[] args) {
        TestObjGroupsLoading app = new TestObjGroupsLoading();
        app.start();
    }
    
    private BitmapText pointerDisplay;
    
    @Override
    public void simpleInitApp() {
    
        // load scene with following structure:
        // Chair 1 (just mesh without name) and named groups: Chair 2, Pillow 2, Podium
        Spatial scene = assetManager.loadModel(new ModelKey("OBJLoaderTest/TwoChairs.obj"));
        // add light to make it visible
        scene.addLight(new AmbientLight(ColorRGBA.White));
        // attach scene to the root
        rootNode.attachChild(scene);
        
        // configure camera for best scene viewing
        cam.setLocation(new Vector3f(-3, 4, 3));
        cam.lookAtDirection(new Vector3f(0, -0.5f, -1), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(10);
        
        // create display to indicate pointed geometry name
        pointerDisplay = new BitmapText(guiFont);
        pointerDisplay.setBox(new Rectangle(0, settings.getHeight(), settings.getWidth(), settings.getHeight()/2));
        pointerDisplay.setAlignment(BitmapFont.Align.Center);
        pointerDisplay.setVerticalAlignment(BitmapFont.VAlign.Center);
        guiNode.attachChild(pointerDisplay);
        
        initCrossHairs();
    }
    
    @Override
    public void simpleUpdate(final float tpf) {
        
        // ray to the center of the screen from the camera
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        
        // find object at the center of the screen
    
        final CollisionResults results = new CollisionResults();
        rootNode.collideWith(ray, results);
        
        CollisionResult result = results.getClosestCollision();
        if (result == null) {
            pointerDisplay.setText("");
        } else {
            // display pointed geometry and it's parents names
            StringBuilder sb = new StringBuilder();
            for (Spatial node = result.getGeometry(); node != null; node = node.getParent()) {
                if (sb.length() > 0) {
                    sb.append(" < ");
                }
                sb.append(node.getName());
            }
            pointerDisplay.setText(sb);
        }
    }
    
    private void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
            settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
            settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
}
