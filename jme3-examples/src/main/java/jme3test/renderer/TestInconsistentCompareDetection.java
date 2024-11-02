
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * Changes a material's texture from another thread while it is rendered.
 * This should trigger the sorting function's inconsistent compare detection.
 * 
 * @author Kirill Vainer
 */
public class TestInconsistentCompareDetection extends SimpleApplication {

    private static Texture t1, t2;
    
    public static void main(String[] args){
        TestInconsistentCompareDetection app = new TestInconsistentCompareDetection();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(-11.674385f, 7.892636f, 33.133106f));
        cam.setRotation(new Quaternion(0.06426433f, 0.90940624f, -0.15329266f, 0.38125014f));
        
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.White);
        
        t1 = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        t2 = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        
        Box b = new Box(1, 1, 1);

        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                Geometry g = new Geometry("g_" + x + "_" + y, b);
                Node monkey = new Node("n_" + x + "_" + y);
                monkey.attachChild(g);
                monkey.move(x * 2, 0, y * 2);

                Material newMat = m.clone();
                g.setMaterial(newMat);

                if (FastMath.rand.nextBoolean()) {
                    newMat.setTexture("ColorMap", t1);
                } else {
                    newMat.setTexture("ColorMap", t2);
                }

                rootNode.attachChild(monkey);
            }
        }
        
        Thread evilThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                
                // begin randomly changing textures after 1 sec.
                while (true) {
                    for (Spatial child : rootNode.getChildren()) {
                        Geometry g = (Geometry) (((Node)child).getChild(0));
                        Material m = g.getMaterial();
                        Texture curTex = m.getTextureParam("ColorMap").getTextureValue();
                        if (curTex == t1) {
                            m.setTexture("ColorMap", t2);
                        } else {
                            m.setTexture("ColorMap", t1);
                        }
                    }
                }
            }
        });
        evilThread.setDaemon(true);
        evilThread.start();
    }
}

