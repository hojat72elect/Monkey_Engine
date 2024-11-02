

package jme3test.export;

import com.jme3.anim.AnimComposer;
import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.*;

public class TestOgreConvert extends SimpleApplication {

    public static void main(String[] args){
        TestOgreConvert app = new TestOgreConvert();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Spatial ogreModel = assetManager.loadModel("Models/Oto/Oto.mesh.xml");

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(0,-1,-1).normalizeLocal());
        rootNode.addLight(dl);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BinaryExporter exp = new BinaryExporter();
            exp.save(ogreModel, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            BinaryImporter imp = new BinaryImporter();
            imp.setAssetManager(assetManager);
            Node ogreModelReloaded = (Node) imp.load(bais, null, null);

            AnimComposer composer = ogreModelReloaded.getControl(AnimComposer.class);
            composer.setCurrentAction("Walk");

            rootNode.attachChild(ogreModelReloaded);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
