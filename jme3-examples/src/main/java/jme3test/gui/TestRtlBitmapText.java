
package jme3test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.*;

/**
 * Test case for JME issue #1158: BitmapText right to left line wrapping not work
 */
public class TestRtlBitmapText extends SimpleApplication {

    private String text = "This is a test right to left text.";
    private BitmapFont fnt;
    private BitmapText txt;

    public static void main(String[] args) {
        TestRtlBitmapText app = new TestRtlBitmapText();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        float x = 400;
        float y = 500;
        getStateManager().detach(stateManager.getState(StatsAppState.class));
        fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        fnt.setRightToLeft(true);

        // A right to left BitmapText
        txt = new BitmapText(fnt);
        txt.setBox(new Rectangle(0, 0, 150, 0));
        txt.setLineWrapMode(LineWrapMode.Word);
        txt.setAlignment(BitmapFont.Align.Right);
        txt.setText(text);

        txt.setLocalTranslation(x, y, 0);
        guiNode.attachChild(txt);
    }
}
