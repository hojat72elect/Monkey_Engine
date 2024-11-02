

package jme3test.niftygui;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;

public class TestNiftyGui extends SimpleApplication {

    private Nifty nifty;

    public static void main(String[] args){
        TestNiftyGui app = new TestNiftyGui();
        app.setPauseOnLostFocus(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        StartScreenController startScreen = new StartScreenController(this);
        nifty.fromXml("Interface/Nifty/HelloJme.xml", "start", startScreen);

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
//        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
    }
}
