
package jme3test.renderer;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.material.MatParamOverride;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

/**
 * Test a Material with increasing numbers of texture parameters, to see what
 * happens when the renderer's dynamic limit is exceeded.
 *
 * If successful, this test throws an IllegalStateException with a helpful
 * diagnostic message.
 */
public class TestIssue37 extends SimpleApplication {

    /**
     * Edit this field to change how parameters are assigned (which determines
     * where the exception is caught): true to use mat param overrides, false to
     * use ordinary mat params.
     */
    final private boolean useOverrides = true;

    private int numTextures;
    private Material manyTexturesMaterial;
    private Texture testTexture;

    public static void main(String[] args) {
        Application application = new TestIssue37();
        application.start();
    }

    @Override
    public void simpleInitApp() {
        /*
         * Attach a test geometry to the scene.
         */
        Mesh cubeMesh = new Box(1f, 1f, 1f);
        Geometry cubeGeometry = new Geometry("Box", cubeMesh);
        rootNode.attachChild(cubeGeometry);
        /*
         * Apply a test material (with no textures assigned) to the geometry.
         */
        manyTexturesMaterial = new Material(assetManager,
                "jme3test/materials/TestIssue37.j3md");
        manyTexturesMaterial.setName("manyTexturesMaterial");
        cubeGeometry.setMaterial(manyTexturesMaterial);
        numTextures = 0;
        /*
         * Load the test texture.
         */
        String texturePath = "Interface/Logo/Monkey.jpg";
        testTexture = assetManager.loadTexture(texturePath);
    }

    /**
     * During each update, define another texture parameter until the dynamic
     * limit is reached.
     *
     * @param tpf ignored
     */
    @Override
    public void simpleUpdate(float tpf) {
        String parameterName = "ColorMap" + numTextures;
        if (useOverrides) {
            MatParamOverride override = new MatParamOverride(VarType.Texture2D,
                    parameterName, testTexture);
            rootNode.addMatParamOverride(override);
        } else {
            manyTexturesMaterial.setTexture(parameterName, testTexture);
        }
        ++numTextures;
    }
}
