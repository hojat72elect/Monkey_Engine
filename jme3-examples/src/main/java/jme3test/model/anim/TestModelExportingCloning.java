
package jme3test.model.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class TestModelExportingCloning extends SimpleApplication {
    
    public static void main(String[] args) {
        TestModelExportingCloning app = new TestModelExportingCloning();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(10f, 3f, 40f));
        cam.lookAtDirection(Vector3f.UNIT_Z.negate(), Vector3f.UNIT_Y);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

        AnimComposer composer;

        Spatial originalModel = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        composer = originalModel.getControl(AnimComposer.class);
        composer.setCurrentAction("Walk");
        composer.setGlobalSpeed(1.5f);
        rootNode.attachChild(originalModel);
        
        Spatial clonedModel = originalModel.clone();
        clonedModel.move(10, 0, 0);
        composer = clonedModel.getControl(AnimComposer.class);
        composer.setCurrentAction("push");
        System.out.println("clonedModel: globalSpeed=" + composer.getGlobalSpeed());
        rootNode.attachChild(clonedModel);
        
        Spatial exportedModel = BinaryExporter.saveAndLoad(assetManager, originalModel);
        exportedModel.move(20, 0, 0);
        composer = exportedModel.getControl(AnimComposer.class);
        composer.setCurrentAction("pull");
        System.out.println("exportedModel: globalSpeed=" + composer.getGlobalSpeed());
        rootNode.attachChild(exportedModel);
    }
}
