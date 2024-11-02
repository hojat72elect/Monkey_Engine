
package jme3test.model;

import com.jme3.app.*;
import com.jme3.scene.*;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.plugins.gltf.GltfModelKey;

public class TestGltfNaming extends SimpleApplication {
    private final static String indentString = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    public static void main(String[] args) {
        TestGltfNaming app = new TestGltfNaming();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Node r1 = new Node("test1");
        Node r2 = new Node("test2");
        Node r3 = new Node("test3");

        r1.attachChild(loadModel("jme3test/gltfnaming/single.gltf"));
        r2.attachChild(loadModel("jme3test/gltfnaming/multi.gltf"));
        r3.attachChild(loadModel("jme3test/gltfnaming/parent.gltf"));

        System.out.println("");
        System.out.println("");

        System.out.println("Test 1: ");
        dumpScene(r1, 0);

        System.out.println("");
        System.out.println("");

        System.out.println("Test 2: ");
        dumpScene(r2, 0);

        System.out.println("");
        System.out.println("");

        System.out.println("Test 3: ");
        dumpScene(r3, 0);
    }

    private Spatial loadModel(String path) {
        GltfModelKey k = new GltfModelKey(path);
        Spatial s = assetManager.loadModel(k);
        s.setCullHint(CullHint.Always);
        return s;
    }

    private void dumpScene(Spatial s, int indent) {
        System.err.println(indentString.substring(0, indent) + s.getName() + " ("
                + s.getClass().getSimpleName() + ") / " + s.getLocalTransform().getTranslation().toString()
                + ", " + s.getLocalTransform().getRotation().toString() + ", "
                + s.getLocalTransform().getScale().toString());
        if (s instanceof Node) {
            Node n = (Node) s;
            for (Spatial spatial : n.getChildren()) {
                dumpScene(spatial, indent + 1);
            }
        }
    }
}
