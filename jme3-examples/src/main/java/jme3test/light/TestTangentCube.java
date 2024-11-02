
package jme3test.light;

import com.jme3.app.ChaseCameraAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author Nehon
 */
public class TestTangentCube extends SimpleApplication {

    public static void main(String... args) {
        TestTangentCube app = new TestTangentCube();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box aBox = new Box(1, 1, 1);
        Geometry aGeometry = new Geometry("Box", aBox);
        TangentBinormalGenerator.generate(aBox);

        Material aMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        aMaterial.setTexture("DiffuseMap",
                assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        aMaterial.setTexture("NormalMap",
                assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg"));
        aMaterial.setBoolean("UseMaterialColors", false);
        aMaterial.setColor("Diffuse", ColorRGBA.White);
        aMaterial.setColor("Specular", ColorRGBA.White);
        aMaterial.setFloat("Shininess", 64f);
        aGeometry.setMaterial(aMaterial);

        // Rotate 45 degrees to see multiple faces
        aGeometry.rotate(FastMath.QUARTER_PI, FastMath.QUARTER_PI, 0.0f);
        rootNode.attachChild(aGeometry);

        /*
         * Must add a light to make the lit object visible!
         */
        PointLight aLight = new PointLight();
        aLight.setPosition(new Vector3f(0, 3, 3));
        aLight.setColor(ColorRGBA.Red);
        rootNode.addLight(aLight);
//
//        AmbientLight bLight = new AmbientLight();
//        bLight.setColor(ColorRGBA.Gray);
//        rootNode.addLight(bLight);

        
        ChaseCameraAppState chaser = new ChaseCameraAppState();
        chaser.setTarget(aGeometry);
        getStateManager().attach(chaser);
    }

}
