

package com.jme3.input.lwjgl;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.system.lwjgl.LwjglAbstractDisplay;
import com.jme3.system.lwjgl.LwjglTimer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class LwjglMouseInput implements MouseInput {

    private static final Logger logger = Logger.getLogger(LwjglMouseInput.class.getName());

    private LwjglAbstractDisplay context;

    private RawInputListener listener;
    private boolean cursorVisible = true;

    /**
     * We need to cache the cursors
     * (https://github.com/jMonkeyEngine/jmonkeyengine/issues/537)
     */
    private Map<JmeCursor, Cursor> cursorMap = new HashMap<>();

    private int curX, curY, curWheel;

    public LwjglMouseInput(LwjglAbstractDisplay context){
        this.context = context;
    }

    @Override
    public void initialize() {
        if (!context.isRenderable())
            return;

        try {
            Mouse.create();
            logger.fine("Mouse created.");
            Cursor.getCapabilities();

            // Recall state that was set before initialization
            Mouse.setGrabbed(!cursorVisible);
        } catch (LWJGLException ex) {
            logger.log(Level.SEVERE, "Error while creating mouse", ex);
        }

        if (listener != null) {
            sendFirstMouseEvent();
        }
    }

    @Override
    public boolean isInitialized(){
        return Mouse.isCreated();
    }

    @Override
    public int getButtonCount(){
        return Mouse.getButtonCount();
    }

    @Override
    public void update() {
        if (!context.isRenderable())
            return;

        while (Mouse.next()){
            int btn = Mouse.getEventButton();

            int wheelDelta = Mouse.getEventDWheel();
            int xDelta = Mouse.getEventDX();
            int yDelta = Mouse.getEventDY();
            int x = Mouse.getX();
            int y = Mouse.getY();

            curWheel += wheelDelta;
            if (cursorVisible){
                xDelta = x - curX;
                yDelta = y - curY;
                curX = x;
                curY = y;
            }else{
                x = curX + xDelta;
                y = curY + yDelta;
                curX = x;
                curY = y;
            }

            if (xDelta != 0 || yDelta != 0 || wheelDelta != 0){
                MouseMotionEvent evt = new MouseMotionEvent(x, y, xDelta, yDelta, curWheel, wheelDelta);
                evt.setTime(Mouse.getEventNanoseconds());
                listener.onMouseMotionEvent(evt);
            }
            if (btn != -1){
                MouseButtonEvent evt = new MouseButtonEvent(btn,
                        Mouse.getEventButtonState(), x, y);
                evt.setTime(Mouse.getEventNanoseconds());
                listener.onMouseButtonEvent(evt);
            }
        }
    }

    @Override
    public void destroy() {
        if (!context.isRenderable())
            return;

        Mouse.destroy();

        // Destroy the cursor cache
        for (Cursor cursor : cursorMap.values()) {
            cursor.destroy();
        }
        cursorMap.clear();

        logger.fine("Mouse destroyed.");
    }

    @Override
    public void setCursorVisible(boolean visible){
        cursorVisible = visible;
        if (!context.isRenderable())
            return;

        Mouse.setGrabbed(!visible);
    }

    @Override
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
        if (listener != null && Mouse.isCreated()) {
            sendFirstMouseEvent();
        }
    }

    /**
     * Send the input listener a special mouse-motion event with zero deltas in
     * order to initialize the listener's cursor position.
     */
    private void sendFirstMouseEvent() {
        assert listener != null;
        assert Mouse.isCreated();

        int x = Mouse.getX();
        int y = Mouse.getY();
        int xDelta = 0;
        int yDelta = 0;
        int wheelDelta = 0;
        MouseMotionEvent evt = new MouseMotionEvent(x, y, xDelta, yDelta,
                curWheel, wheelDelta);
        evt.setTime(Mouse.getEventNanoseconds());

        listener.onMouseMotionEvent(evt);
    }

    @Override
    public long getInputTimeNanos() {
        return Sys.getTime() * LwjglTimer.LWJGL_TIME_TO_NANOS;
    }

    @Override
    public void setNativeCursor(JmeCursor jmeCursor) {
        try {
            Cursor newCursor = null;
            if (jmeCursor != null) {
                newCursor = cursorMap.get(jmeCursor);
                if (newCursor == null) {
                    newCursor = new Cursor(
                            jmeCursor.getWidth(),
                            jmeCursor.getHeight(),
                            jmeCursor.getXHotSpot(),
                            jmeCursor.getYHotSpot(),
                            jmeCursor.getNumImages(),
                            jmeCursor.getImagesData(),
                            jmeCursor.getImagesDelay());

                    // Add to cache
                    cursorMap.put(jmeCursor, newCursor);
                }
            }
            Mouse.setNativeCursor(newCursor);
        } catch (LWJGLException ex) {
            Logger.getLogger(LwjglMouseInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
