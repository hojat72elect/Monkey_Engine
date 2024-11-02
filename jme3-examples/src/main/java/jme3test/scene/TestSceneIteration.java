
package jme3test.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphIterator;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Test suite for {@link SceneGraphIterator}.
 * <p>
 * The test succeeds if the rootNode and all its children,
 * except all spatials named "XXX", are printed on the console
 * with indents precisely indicating each spatial's distance
 * from the rootNode.
 * <p>
 * The test fails if
 * <ul>
 *   <li>Not all expected children are printed on the console.
 *   <li>An XXX is printed on the console (indicating faulty {@code ignoreChildren}).
 *   <li>Indents do not accurately indicate distance from the rootNode.
 * </ul>
 * 
 * @author codex
 */
public class TestSceneIteration extends SimpleApplication {
    
    /**
     * Launches the test application.
     * 
     * @param args no argument required
     */
    public static void main(String[] args) {
        new TestSceneIteration().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        // setup scene graph
        Node n1 = new Node("town");
        rootNode.attachChild(n1);
            n1.attachChild(new Node("car"));
            n1.attachChild(new Node("tree"));
            Node n2 = new Node("house");
            n1.attachChild(n2);
                n2.attachChild(new Node("chairs"));
                n2.attachChild(new Node("tables"));
                n2.attachChild(createGeometry("house-geometry"));
        Node n3 = new Node("sky");
        rootNode.attachChild(n3);
            n3.attachChild(new Node("airplane"));
            Node ignore = new Node("cloud");
            n3.attachChild(ignore);
                ignore.attachChild(new Node("XXX"));
                ignore.attachChild(new Node("XXX"));
                ignore.attachChild(new Node("XXX"));
            n3.attachChild(new Node("bird"));
        
        // iterate
        SceneGraphIterator iterator = new SceneGraphIterator(rootNode);
        for (Spatial spatial : iterator) {
            // create a hierarchy in the console
            System.out.println(constructTabs(iterator.getDepth()) + spatial.getName());
            // see if the children of this spatial should be ignored
            if (spatial == ignore) {
                // ignore all children of this spatial
                iterator.ignoreChildren();
            }
        }
        
        // exit the application
        stop();
        
    }
    
    private Geometry createGeometry(String name) {
        Geometry g = new Geometry(name, new Box(1, 1, 1));
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(m);
        return g;
    }
    
    private String constructTabs(int n) {
        StringBuilder render = new StringBuilder();
        for (; n > 0; n--) {
            render.append(" | ");
        }
        return render.toString();
    }
    
}
