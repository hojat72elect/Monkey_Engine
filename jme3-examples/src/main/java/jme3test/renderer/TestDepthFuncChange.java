
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class TestDepthFuncChange extends SimpleApplication {

    public static void main(String[] args) {
        TestDepthFuncChange app = new TestDepthFuncChange();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setMoveSpeed(20);
        
       
        //top of the screen
        //default depth func (less or equal) rendering.
        //2 cubes, a blue and a red. the red cube is offset by 0.2 WU to the right   
        //the red cube is put in the transparent bucket to be sure it's rendered after the blue one (but there is no transparency involved).
        //You should see a small part of the blue cube on the left and the whole red cube
        Box boxShape1 = new Box(1f, 1f, 1f);
        Geometry cube1 = new Geometry("box", boxShape1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        
        cube1.setMaterial(mat);
        rootNode.attachChild(cube1);
        cube1.move(0, 1.5f, 0);      
        
        Geometry cube2 = cube1.clone(true);
        cube2.move(0.2f, 0 , 0);    
        cube2.setQueueBucket(RenderQueue.Bucket.Transparent);
        cube2.getMaterial().setColor("Color",  ColorRGBA.Red);
        rootNode.attachChild(cube2);
        
        //Bottom of the screen
        //here the 2 cubes are cloned and the depthFunc for the red cube's material is set to Less
        //You should see the whole blue cube and a small part of the red cube on the right
        Geometry cube3 = cube1.clone();
        Geometry cube4 = cube2.clone(true);
        cube4.getMaterial().getAdditionalRenderState().setDepthFunc(RenderState.TestFunction.Less);       
        cube3.move(0,-3,0);
        cube4.move(0,-3,0);
        rootNode.attachChild(cube3);
        rootNode.attachChild(cube4);
        
        //Note that if you move the camera z fighting will occur but that's expected.
                
        
    }
}
