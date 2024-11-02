
package jme3test.niftygui;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class TestIssue1013 extends SimpleApplication implements ScreenController {

    public static void main(String[] args) {
        new TestIssue1013().start();
    }

    private NiftyJmeDisplay niftyDisplay;

    @Override
    public void simpleInitApp() {

        // this box here always renders
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("/com/jme3/app/Monkey.png"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);

        Nifty nifty = niftyDisplay.getNifty();
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        ScreenController ctrl = this;

        new ScreenBuilder("start") {
            {
                controller(ctrl);
                layer(new LayerBuilder() {
                    {
                        childLayoutVertical();
                        panel(new PanelBuilder() {
                            {
                                childLayoutCenter();
                                width("100%");
                                height("50%");
                                backgroundColor("#ff0000");
                            }
                        });
                        control(new ButtonBuilder("RestartButton", "Restart Context") {
                            {
                                alignCenter();
                                valignCenter();
                                height("32px");
                                width("480px");
                                interactOnClick("restartContext()");
                            }
                        });
                    }
                });
            }
        }.build(nifty);

        guiViewPort.addProcessor(niftyDisplay);
        nifty.gotoScreen("start");

        flyCam.setDragToRotate(true);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void restartContext() {
        // even without changing settings, stuff breaks!
        restart();
        // ...and re-adding the processor doesn't help at all
        guiViewPort.addProcessor(niftyDisplay);
    }

}
