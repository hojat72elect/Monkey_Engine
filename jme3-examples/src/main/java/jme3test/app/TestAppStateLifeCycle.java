

package jme3test.app;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;


/**
 *  Tests the app state lifecycles.
 *
 *  @author    Paul Speed
 */
public class TestAppStateLifeCycle extends SimpleApplication {

    public static void main(String[] args){
        TestAppStateLifeCycle app = new TestAppStateLifeCycle();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        System.out.println("Attaching test state.");
        stateManager.attach(new TestState());
        
        System.out.println("Attaching test state with an ID.");
        stateManager.attach(new TestState("Test ID"));        
    }

    @Override
    public void simpleUpdate(float tpf) {
    
        if(stateManager.getState(TestState.class) != null) {
            System.out.println("Detaching test state."); 
            stateManager.detach(stateManager.getState(TestState.class));
            System.out.println("Done"); 
        }
        
        if( stateManager.hasState("Test ID") ) {
            System.out.println("Detaching test state with an ID."); 
            stateManager.detach(stateManager.getState("Test ID", TestState.class));
            System.out.println("Done"); 
        }
    }
    
    public class TestState extends AbstractAppState {
 
        public TestState() {
        }
        
        public TestState( String id ) {
            super(id);
        }
 
        @Override
        public void initialize(AppStateManager stateManager, Application app) {
            super.initialize(stateManager, app);
            System.out.println("Initialized, id:" + getId());
        }
 
        @Override
        public void stateAttached(AppStateManager stateManager) {
            super.stateAttached(stateManager);
            System.out.println("Attached, id:" + getId());
        }
 
        @Override
        public void update(float tpf) {
            super.update(tpf);
            System.out.println("update, id:" + getId());
        }

        @Override
        public void render(RenderManager rm) {
            super.render(rm);
            System.out.println("render, id:" + getId());
        }

        @Override
        public void postRender() {
            super.postRender();
            System.out.println("postRender, id:" + getId());
        }

        @Override
        public void stateDetached(AppStateManager stateManager) {
            super.stateDetached(stateManager);
            System.out.println("Detached, id:" + getId());
        }
 
        @Override
        public void cleanup() {
            super.cleanup();
            System.out.println("Cleanup, id:" + getId()); 
        }

    }    
}
