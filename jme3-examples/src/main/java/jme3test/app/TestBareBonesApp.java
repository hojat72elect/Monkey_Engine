

package jme3test.app;

import com.jme3.app.LegacyApplication;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Test a bare-bones application, without SimpleApplication.
 */
public class TestBareBonesApp extends LegacyApplication {

    private Geometry boxGeom;

    public static void main(String[] args){
        TestBareBonesApp app = new TestBareBonesApp();
        app.start();
    }

    @Override
    public void initialize(){
        super.initialize();

        System.out.println("Initialize");

        // create a box
        boxGeom = new Geometry("Box", new Box(2, 2, 2));

        // load some default material
        boxGeom.setMaterial(assetManager.loadMaterial("Interface/Logo/Logo.j3m"));

        // attach box to display in primary viewport
        viewPort.attachScene(boxGeom);
    }

    @Override
    public void update(){
        super.update();

        // do some animation
        float tpf = timer.getTimePerFrame();
        boxGeom.rotate(tpf * 2, tpf * 4, tpf * 3);

        // don't forget to update the scenes
        boxGeom.updateLogicalState(tpf);
        boxGeom.updateGeometricState();

        // render the viewports
        renderManager.render(tpf, context.isRenderable());
    }

    @Override
    public void destroy(){
        super.destroy();

        System.out.println("Destroy");
    }
}
