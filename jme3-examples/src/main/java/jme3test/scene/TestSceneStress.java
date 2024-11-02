

package jme3test.scene;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import java.util.Random;


/**
 *  Tests a deep scene with an unrecommended amount of objects.
 *
 *  @author    Paul Speed
 */
public class TestSceneStress extends SimpleApplication {
 
    final private static Box BOX = new Box(2f, 0.5f, 0.5f);
 
    private Material mat;
    final private Random random = new Random(0);
 
    private int totalNodes = 0;
    private int totalGeometry = 0;
    private int totalControls = 0;
    
    public static void main( String... args ) {
        
        TestSceneStress test = new TestSceneStress();
        test.start();
    }
    
    public TestSceneStress() {
        super(new StatsAppState(), new DebugKeysAppState(), new BasicProfilerState(false),
              new FlyCamAppState(),
              new ScreenshotAppState("", System.currentTimeMillis())); 
    }
    
    @Override
    public void simpleInitApp() {
 
        stateManager.getState(FlyCamAppState.class).getCamera().setMoveSpeed(10);
    
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);

        // Create a deep, mostly static scene        
        Spatial oct = createOctSplit("root", 500, 5);
        
        rootNode.attachChild(oct);
 
        // Position to see most of it       
        cam.setLocation(new Vector3f(400.8009f, 370.16455f, -408.17984f));
        cam.setRotation(new Quaternion(0.24906662f, -0.3756747f, 0.105560325f, 0.88639235f));
        
        System.out.println("Total nodes:" + totalNodes + "  Total Geometry:" + totalGeometry + "  Total controls:" + totalControls );        
    }
    
    protected Spatial createOctSplit( String name, int size, int depth ) {
        
        if( depth == 0 ) {
            // Done splitting
            Geometry geom = new Geometry(name, BOX);
            totalGeometry++;
            geom.setMaterial(mat);
            
            if( random.nextFloat() < 0.01 ) {
                RotatorControl control = new RotatorControl(random.nextFloat(), random.nextFloat(), random.nextFloat());
                geom.addControl(control);
                totalControls++;
            }
            
            return geom;
        }
        
        Node root = new Node(name);
        totalNodes++;
 
        int half = size / 2;
        float quarter = half * 0.5f;
 
        for( int i = 0; i < 2; i++ ) {
            float x = i * half - quarter;
            for( int j = 0; j < 2; j++ ) {
                float y = j * half - quarter;
                for( int k = 0; k < 2; k++ ) {
                    float z = k * half - quarter;
                    
                    Spatial child = createOctSplit(name + "(" + i + ", " + j + ", " + k + ")", 
                                                   half, depth - 1);
                    child.setLocalTranslation(x, y, z);                                                   
                    root.attachChild(child);   
                }
            }
        }
       
        return root;        
    }
    
    private class RotatorControl extends AbstractControl {
        final private float[] rotate;
        
        public RotatorControl( float... rotate ) {
            this.rotate = rotate;
        }

        @Override
        protected void controlUpdate( float tpf ) {
            if( spatial != null ) {
                spatial.rotate(rotate[0] * tpf, rotate[1] * tpf, rotate[2] * tpf);
            }
        }

        @Override
        protected void controlRender( RenderManager rm, ViewPort vp ) {
        }        
    }  
}
