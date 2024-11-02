
package jme3test.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;

/**
 * Display the renderer's maximum line width.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestLineWidth extends SimpleApplication {

    public static void main(String... args) {
        TestLineWidth app = new TestLineWidth();
        AppSettings set = new AppSettings(true);
        set.setRenderer(AppSettings.LWJGL_OPENGL2);
        app.setSettings(set);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        /*
         * Generate a message to report (1) which renderer is selected
         * and (2) the maximum line width.
         */
        String rendererName = settings.getRenderer();
        float maxWidth = renderer.getMaxLineWidth();
        String message = String.format(
                "using %s renderer%nmaximum line width = %.1f pixel%s",
                rendererName, maxWidth, (maxWidth == 1f) ? "" : "s");
        /*
         * Display the message, centered near the top of the display.
         */
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText text = new BitmapText(font);
        text.setSize(font.getCharSet().getRenderedSize());
        text.setText(message);
        float leftX = (cam.getWidth() - text.getLineWidth()) / 2;
        float topY = cam.getHeight();
        text.setLocalTranslation(leftX, topY, 0f);
        guiNode.attachChild(text);
        /*
         * Display a vertical green line on the left side of the display.
         */
        float lineWidth = Math.min(maxWidth, leftX);
        drawVerticalLine(lineWidth, leftX / 2, ColorRGBA.Green);
    }

    private void drawVerticalLine(float lineWidth, float x, ColorRGBA color) {
        Material material = new Material(assetManager, Materials.UNSHADED);
        material.setColor("Color", color.clone());
        material.getAdditionalRenderState().setLineWidth(lineWidth);

        float viewportHeight = cam.getHeight();
        Vector3f startLocation = new Vector3f(x, 0.1f * viewportHeight, 0f);
        Vector3f endLocation = new Vector3f(x, 0.9f * viewportHeight, 0f);
        Mesh wireMesh = new Line(startLocation, endLocation);
        Geometry wire = new Geometry("wire", wireMesh);
        wire.setMaterial(material);
        guiNode.attachChild(wire);
    }
}
