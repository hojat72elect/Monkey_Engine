
package jme3test.app;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

/**
 * Demonstrate what happens if you use a deleted texture with OpenGL.
 *
 * If assertions are enabled, an AssertionError is thrown NativeObjectManager.
 *
 * @author Kirill Vainer
 */
public class TestUseAfterFree extends SimpleApplication {

    private float time = 0;
    private Material mat;
    private Texture deletedTex;
    
    public static void main(String[] args) {
        TestUseAfterFree app = new TestUseAfterFree();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        if (time < 0) {
            if (deletedTex != null) {
                deletedTex.getImage().resetObject();
            }
            return;
        }
        
        time += tpf;
        if (time > 5) {
            System.out.println("Assigning texture to deleted object!");
            
            deletedTex = assetManager.loadTexture("Interface/Logo/Monkey.png");
            BufferUtils.destroyDirectBuffer(deletedTex.getImage().getData(0));
            mat.setTexture("ColorMap", deletedTex);
            
            time = -1;
        }
    }
}