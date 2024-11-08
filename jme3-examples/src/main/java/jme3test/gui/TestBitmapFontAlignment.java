

package jme3test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class TestBitmapFontAlignment extends SimpleApplication {

    public static void main(String[] args) {
        TestBitmapFontAlignment test = new TestBitmapFontAlignment();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        int width = getCamera().getWidth();
        int height = getCamera().getHeight();

        // VAlign.Top
        BitmapText labelAlignTop = guiFont.createLabel("This text has VAlign.Top.");
        Rectangle textboxAlignTop = new Rectangle(width * 0.2f, height * 0.7f, 120, 120);
        labelAlignTop.setBox(textboxAlignTop);
        labelAlignTop.setVerticalAlignment(BitmapFont.VAlign.Top);
        getGuiNode().attachChild(labelAlignTop);

        Geometry backgroundBoxAlignTop = new Geometry("", new Quad(textboxAlignTop.width, -textboxAlignTop.height));
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        backgroundBoxAlignTop.setMaterial(material);
        backgroundBoxAlignTop.setLocalTranslation(textboxAlignTop.x, textboxAlignTop.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignTop);

        // VAlign.Center
        BitmapText labelAlignCenter = guiFont.createLabel("This text has VAlign.Center");
        Rectangle textboxAlignCenter = new Rectangle(width * 0.4f, height * 0.7f, 120, 120);
        labelAlignCenter.setBox(textboxAlignCenter);
        labelAlignCenter.setVerticalAlignment(BitmapFont.VAlign.Center);
        getGuiNode().attachChild(labelAlignCenter);

        Geometry backgroundBoxAlignCenter = backgroundBoxAlignTop.clone(false);
        backgroundBoxAlignCenter.setLocalTranslation(textboxAlignCenter.x, textboxAlignCenter.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignCenter);

        // VAlign.Bottom
        BitmapText labelAlignBottom = guiFont.createLabel("This text has VAlign.Bottom");
        Rectangle textboxAlignBottom = new Rectangle(width * 0.6f, height * 0.7f, 120, 120);
        labelAlignBottom.setBox(textboxAlignBottom);
        labelAlignBottom.setVerticalAlignment(BitmapFont.VAlign.Bottom);
        getGuiNode().attachChild(labelAlignBottom);

        Geometry backgroundBoxAlignBottom = backgroundBoxAlignTop.clone(false);
        backgroundBoxAlignBottom.setLocalTranslation(textboxAlignBottom.x, textboxAlignBottom.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignBottom);

        // VAlign.Top + Align.Right
        BitmapText labelAlignTopRight = guiFont.createLabel("This text has VAlign.Top and Align.Right");
        Rectangle textboxAlignTopRight = new Rectangle(width * 0.2f, height * 0.3f, 120, 120);
        labelAlignTopRight.setBox(textboxAlignTopRight);
        labelAlignTopRight.setVerticalAlignment(BitmapFont.VAlign.Top);
        labelAlignTopRight.setAlignment(BitmapFont.Align.Right);
        getGuiNode().attachChild(labelAlignTopRight);

        Geometry backgroundBoxAlignTopRight = backgroundBoxAlignTop.clone(false);
        backgroundBoxAlignTopRight.setLocalTranslation(textboxAlignTopRight.x, textboxAlignTopRight.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignTopRight);

        // VAlign.Center + Align.Center
        BitmapText labelAlignCenterCenter = guiFont.createLabel("This text has VAlign.Center and Align.Center");
        Rectangle textboxAlignCenterCenter = new Rectangle(width * 0.4f, height * 0.3f, 120, 120);
        labelAlignCenterCenter.setBox(textboxAlignCenterCenter);
        labelAlignCenterCenter.setVerticalAlignment(BitmapFont.VAlign.Center);
        labelAlignCenterCenter.setAlignment(BitmapFont.Align.Center);
        getGuiNode().attachChild(labelAlignCenterCenter);

        Geometry backgroundBoxAlignCenterCenter = backgroundBoxAlignCenter.clone(false);
        backgroundBoxAlignCenterCenter.setLocalTranslation(textboxAlignCenterCenter.x, textboxAlignCenterCenter.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignCenterCenter);

        // VAlign.Bottom + Align.Left
        BitmapText labelAlignBottomLeft = guiFont.createLabel("This text has VAlign.Bottom and Align.Left");
        Rectangle textboxAlignBottomLeft = new Rectangle(width * 0.6f, height * 0.3f, 120, 120);
        labelAlignBottomLeft.setBox(textboxAlignBottomLeft);
        labelAlignBottomLeft.setVerticalAlignment(BitmapFont.VAlign.Bottom);
        labelAlignBottomLeft.setAlignment(BitmapFont.Align.Left);
        getGuiNode().attachChild(labelAlignBottomLeft);

        Geometry backgroundBoxAlignBottomLeft = backgroundBoxAlignBottom.clone(false);
        backgroundBoxAlignBottomLeft.setLocalTranslation(textboxAlignBottomLeft.x, textboxAlignBottomLeft.y, -1);
        getGuiNode().attachChild(backgroundBoxAlignBottomLeft);

        // Large quad with VAlign.Center and Align.Center
        BitmapText label = guiFont.createLabel("This text is centered, both horizontally and vertically.");
        Rectangle box = new Rectangle(width * 0.05f, height * 0.95f, width * 0.9f, height * 0.1f);
        label.setBox(box);
        label.setAlignment(BitmapFont.Align.Center);
        label.setVerticalAlignment(BitmapFont.VAlign.Center);
        getGuiNode().attachChild(label);

        Geometry background = new Geometry("background", new Quad(box.width, -box.height));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        background.setMaterial(mat);
        background.setLocalTranslation(box.x, box.y, -1);
        getGuiNode().attachChild(background);
    }

}
