
package jme3test.bullet;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.util.logging.Logger;

/**
 * Test case for JME issue #1125: heightfield collision shapes don't match
 * TerrainQuad.
 * <p>
 * If successful, just one set of grid diagonals will be visible. If
 * unsuccessful, you'll see both green diagonals (the TerrainQuad) and
 * perpendicular blue diagonals (physics debug).
 * <p>
 * Use this test with jme3-bullet only; it can yield false success with
 * jme3-jbullet due to JME issue #1129.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestIssue1125 extends SimpleApplication {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final public static Logger logger
            = Logger.getLogger(TestIssue1125.class.getName());
    // *************************************************************************
    // fields

    /**
     * height array for a small heightfield
     */
    final private float[] nineHeights = new float[9];
    /**
     * green wireframe material for the TerrainQuad
     */
    private Material quadMaterial;
    /**
     * space for physics simulation
     */
    private PhysicsSpace physicsSpace;
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the TestIssue1125 application.
     *
     * @param ignored array of command-line arguments (not null)
     */
    public static void main(String[] ignored) {
        new TestIssue1125().start();
    }
    // *************************************************************************
    // SimpleApplication methods

    /**
     * Initialize this application.
     */
    @Override
    public void simpleInitApp() {
        configureCamera();
        configureMaterials();
        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.2f, 0.2f, 1f));
        configurePhysics();
        initializeHeightData();
        addTerrain();
        showHints();
    }
    // *************************************************************************
    // private methods

    /**
     * Add 3x3 terrain to the scene and the PhysicsSpace.
     */
    private void addTerrain() {
        int patchSize = 3;
        int mapSize = 3;
        TerrainQuad quad
                = new TerrainQuad("terrain", patchSize, mapSize, nineHeights);
        rootNode.attachChild(quad);
        quad.setMaterial(quadMaterial);

        CollisionShape shape = CollisionShapeFactory.createMeshShape(quad);
        float massForStatic = 0f;
        RigidBodyControl rbc = new RigidBodyControl(shape, massForStatic);
        rbc.setPhysicsSpace(physicsSpace);
        quad.addControl(rbc);
    }

    /**
     * Configure the camera during startup.
     */
    private void configureCamera() {
        float fHeight = cam.getFrustumTop() - cam.getFrustumBottom();
        float fWidth = cam.getFrustumRight() - cam.getFrustumLeft();
        float fAspect = fWidth / fHeight;
        float yDegrees = 45f;
        float near = 0.02f;
        float far = 20f;
        cam.setFrustumPerspective(yDegrees, fAspect, near, far);

        flyCam.setMoveSpeed(5f);

        cam.setLocation(new Vector3f(2f, 4.7f, 0.4f));
        cam.setRotation(new Quaternion(0.348f, -0.64f, 0.4f, 0.556f));
    }

    /**
     * Configure materials during startup.
     */
    private void configureMaterials() {
        quadMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        quadMaterial.setColor("Color", ColorRGBA.Green.clone());
        RenderState ars = quadMaterial.getAdditionalRenderState();
        ars.setWireframe(true);
    }

    /**
     * Configure physics during startup.
     */
    private void configurePhysics() {
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        physicsSpace = bulletAppState.getPhysicsSpace();
    }

    /**
     * Initialize the height data during startup.
     */
    private void initializeHeightData() {
        nineHeights[0] = 1f;
        nineHeights[1] = 0f;
        nineHeights[2] = 1f;
        nineHeights[3] = 0f;
        nineHeights[4] = 0.5f;
        nineHeights[5] = 0f;
        nineHeights[6] = 1f;
        nineHeights[7] = 0f;
        nineHeights[8] = 1f;
    }

    private void showHints() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        int numLines = 3;
        BitmapText lines[] = new BitmapText[numLines];
        for (int i = 0; i < numLines; ++i) {
            lines[i] = new BitmapText(guiFont);
        }

        String p = "Test for jMonkeyEngine issue #1125";
        if (isNativeBullet()) {
            lines[0].setText(p + " with native Bullet");
        } else {
            lines[0].setText(p + " with JBullet (may yield false success)");
        }
        lines[1].setText("Use W/A/S/D/Q/Z/arrow keys to move the camera.");
        lines[2].setText("F5: render stats, C: camera pos, M: mem stats");

        float textHeight = guiFont.getCharSet().getLineHeight();
        float viewHeight = cam.getHeight();
        float viewWidth = cam.getWidth();
        for (int i = 0; i < numLines; ++i) {
            float left = Math.round((viewWidth - lines[i].getLineWidth()) / 2f);
            float top = viewHeight - i * textHeight;
            lines[i].setLocalTranslation(left, top, 0f);
            guiNode.attachChild(lines[i]);
        }
    }

    /**
     * Determine which physics library is in use.
     *
     * @return true for C++ Bullet, false for JBullet (jme3-jbullet)
     */
    private boolean isNativeBullet() {
        try {
            Class clazz = Class.forName("com.jme3.bullet.util.NativeMeshUtil");
            return clazz != null;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}
