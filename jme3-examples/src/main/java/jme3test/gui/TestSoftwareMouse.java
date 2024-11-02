

package jme3test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

public class TestSoftwareMouse extends SimpleApplication {

    private Picture cursor;

    final private RawInputListener inputListener = new RawInputListener() {

        @Override
        public void beginInput() {
        }
        @Override
        public void endInput() {
        }
        @Override
        public void onJoyAxisEvent(JoyAxisEvent evt) {
        }
        @Override
        public void onJoyButtonEvent(JoyButtonEvent evt) {
        }
        @Override
        public void onMouseMotionEvent(MouseMotionEvent evt) {
            float x = evt.getX();
            float y = evt.getY();

            // Prevent mouse from leaving screen
            AppSettings settings = TestSoftwareMouse.this.settings;
            x = FastMath.clamp(x, 0, settings.getWidth());
            y = FastMath.clamp(y, 0, settings.getHeight());

            // adjust for hotspot
            cursor.setPosition(x, y - 64);
        }
        @Override
        public void onMouseButtonEvent(MouseButtonEvent evt) {
        }
        @Override
        public void onKeyEvent(KeyInputEvent evt) {
        }
        @Override
        public void onTouchEvent(TouchEvent evt) {
        }
    };

    public static void main(String[] args){
        TestSoftwareMouse app = new TestSoftwareMouse();

//        AppSettings settings = new AppSettings(true);
//        settings.setFrameRate(60);
//        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
//        inputManager.setCursorVisible(false);

        Texture tex = assetManager.loadTexture("Interface/Logo/Cursor.png");

        cursor = new Picture("cursor");
        cursor.setTexture(assetManager, (Texture2D) tex, true);
        cursor.setWidth(64);
        cursor.setHeight(64);
        guiNode.attachChild(cursor);
        /*
         * Position the software cursor
         * so that its upper-left corner is at the hotspot.
         */
        Vector2f initialPosition = inputManager.getCursorPosition();
        cursor.setPosition(initialPosition.x, initialPosition.y - 64f);

        inputManager.addRawInputListener(inputListener);

//        Image img = tex.getImage();
//        ByteBuffer data = img.getData(0);
//        IntBuffer image = BufferUtils.createIntBuffer(64 * 64);
//        for (int y = 0; y < 64; y++){
//            for (int x = 0; x < 64; x++){
//                int rgba = data.getInt();
//                image.put(rgba);
//            }
//        }
//        image.clear();
//
//        try {
//            Cursor cur = new Cursor(64, 64, 2, 62, 1, image, null);
//            Mouse.setNativeCursor(cur);
//        } catch (LWJGLException ex) {
//            Logger.getLogger(TestSoftwareMouse.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
