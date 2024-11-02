

package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * Checks two-sided lighting capability.
 * 
 * @author Kirill Vainer
 */
public class TestTwoSideLighting extends SimpleApplication {

    private float angle;
    private PointLight pl;
    private Geometry lightMdl;

    public static void main(String[] args){
        TestTwoSideLighting app = new TestTwoSideLighting();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Two-sided lighting requires single pass.
        renderManager.setPreferredLightMode(TechniqueDef.LightMode.SinglePass);
        renderManager.setSinglePassLightBatchSize(4);
        
        cam.setLocation(new Vector3f(5.936224f, 3.3759952f, -3.3202777f));
        cam.setRotation(new Quaternion(0.16265652f, -0.4811838f, 0.09137692f, 0.8565368f));
        
        Geometry quadGeom = new Geometry("quad", new Quad(1, 1));
        quadGeom.move(1, 0, 0);
        Material mat1 = assetManager.loadMaterial("Textures/BumpMapTest/SimpleBump.j3m");
        
        // Display both front and back faces.
        mat1.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        
        quadGeom.setMaterial(mat1);
        // SimpleBump material requires tangents.
        TangentBinormalGenerator.generate(quadGeom);
        rootNode.attachChild(quadGeom);
        
        Geometry teapot = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot.move(-1, 0, 0);
        teapot.setLocalScale(2f);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setFloat("Shininess", 25);
        mat2.setBoolean("UseMaterialColors", true);
        mat2.setColor("Ambient",  ColorRGBA.Black);
        mat2.setColor("Diffuse",  ColorRGBA.Gray);
        mat2.setColor("Specular", ColorRGBA.Gray);
        
        // Only display backfaces.
        mat2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        
        teapot.setMaterial(mat2);
        rootNode.attachChild(teapot);

        lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.getMesh().setStatic();
        rootNode.attachChild(lightMdl);

        pl = new PointLight();
        pl.setColor(ColorRGBA.White);
        pl.setRadius(4f);
        rootNode.addLight(pl);
    }

    @Override
    public void simpleUpdate(float tpf){
        angle += tpf;
        angle %= FastMath.TWO_PI;
        
        pl.setPosition(new Vector3f(FastMath.cos(angle) * 3f, 0.5f, FastMath.sin(angle) * 3f));
        lightMdl.setLocalTranslation(pl.getPosition());
    }

}
