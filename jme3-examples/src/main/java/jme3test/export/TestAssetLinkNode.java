

package jme3test.export;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.MaterialKey;
import com.jme3.asset.ModelKey;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestAssetLinkNode extends SimpleApplication {

    private float angle;
    private PointLight pl;
    private Spatial lightMdl;

    public static void main(String[] args){
        TestAssetLinkNode app = new TestAssetLinkNode();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        AssetLinkNode loaderNode=new AssetLinkNode();
        loaderNode.addLinkedChild(new ModelKey("Models/MonkeyHead/MonkeyHead.mesh.xml"));
        //load/attach the children (happens automatically on load)
//        loaderNode.attachLinkedChildren(assetManager);
//        rootNode.attachChild(loaderNode);

        //save and load the loaderNode
        try {
            //export to byte array
            ByteArrayOutputStream bout=new ByteArrayOutputStream();
            BinaryExporter.getInstance().save(loaderNode, bout);
            //import from byte array, automatically loads the monkey head from file
            ByteArrayInputStream bin=new ByteArrayInputStream(bout.toByteArray());
            BinaryImporter imp=BinaryImporter.getInstance();
            imp.setAssetManager(assetManager);
            Node newLoaderNode=(Node)imp.load(bin);
            //attach to rootNode
            rootNode.attachChild(newLoaderNode);
        } catch (IOException ex) {
            Logger.getLogger(TestAssetLinkNode.class.getName()).log(Level.SEVERE, null, ex);
        }


        rootNode.attachChild(loaderNode);

        lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(assetManager.loadAsset(new MaterialKey("Common/Materials/RedColor.j3m")));
        rootNode.attachChild(lightMdl);

        // fluorescent main light
        pl = new PointLight();
        pl.setColor(new ColorRGBA(0.88f, 0.92f, 0.95f, 1.0f));
        rootNode.addLight(pl);

        // sunset light
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f,-0.7f,1).normalizeLocal());
        dl.setColor(new ColorRGBA(0.44f, 0.30f, 0.20f, 1.0f));
        rootNode.addLight(dl);

        // skylight
        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.6f,-1,-0.6f).normalizeLocal());
        dl.setColor(new ColorRGBA(0.10f, 0.22f, 0.44f, 1.0f));
        rootNode.addLight(dl);

        // white ambient light
        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(1, -0.5f,-0.1f).normalizeLocal());
        dl.setColor(new ColorRGBA(0.50f, 0.40f, 0.50f, 1.0f));
        rootNode.addLight(dl);
    }

    @Override
    public void simpleUpdate(float tpf){
        angle += tpf * 0.25f;
        angle %= FastMath.TWO_PI;

        pl.setPosition(new Vector3f(FastMath.cos(angle) * 6f, 3f, FastMath.sin(angle) * 6f));
        lightMdl.setLocalTranslation(pl.getPosition());
    }

}
