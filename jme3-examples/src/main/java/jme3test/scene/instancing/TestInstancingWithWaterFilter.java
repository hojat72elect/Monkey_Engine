

package jme3test.scene.instancing;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.scene.shape.Box;
import com.jme3.water.WaterFilter;

/**
 * A test case for using instancing with shadow filter. This is a test case
 * for issue 2007 (Instanced objects are culled when using the WaterFilter).
 *
 * If test succeeds, all the boxes in the camera frustum will be rendered. If
 * test fails, some of the boxes that are in the camera frustum will be culled.
 *
 * @author Ali-RS
 */
public class TestInstancingWithWaterFilter extends SimpleApplication {
    public static void main(String[] args) {
        TestInstancingWithWaterFilter test = new TestInstancingWithWaterFilter();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);

        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-1, -1, -1));
        rootNode.addLight(light);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseInstancing", true);

        Box mesh = new Box(0.5f, 0.5f, 0.5f);

        InstancedNode instanceNode = new InstancedNode("TestInstancedNode");
        //instanceNode.setCullHint(Spatial.CullHint.Never);
        rootNode.attachChild(instanceNode);

        for (int i = 0; i < 200; i++) {
            Geometry obj = new Geometry("TestBox" + i, mesh);
            obj.setMaterial(mat);
            obj.setLocalTranslation(i, i, 0);
            instanceNode.attachChild(obj);
        }
        instanceNode.instance();

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        WaterFilter waterFilter = new WaterFilter(rootNode, light.getDirection());
        fpp.addFilter(waterFilter);
        viewPort.addProcessor(fpp);
    }
}
