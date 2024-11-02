

package com.jme3.input.android;

import android.view.MotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector2f;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * AndroidTouchHandler14 extends AndroidTouchHandler to process the onHover
 * events added in Android rev 14 (Android 4.0).
 *
 * @author iwgeric
 */
public class AndroidTouchInput14 extends AndroidTouchInput {
    private static final Logger logger = Logger.getLogger(AndroidTouchInput14.class.getName());
    final private HashMap<Integer, Vector2f> lastHoverPositions = new HashMap<>();

    public AndroidTouchInput14(AndroidInputHandler androidInput) {
        super(androidInput);
    }

    public boolean onHover(MotionEvent event) {
        boolean consumed = false;
        int action = getAction(event);
        int pointerId = getPointerId(event);
        int pointerIndex = getPointerIndex(event);
        Vector2f lastPos = lastHoverPositions.get(pointerId);
        float jmeX;
        float jmeY;

        numPointers = event.getPointerCount();

//        logger.log(Level.INFO, "onHover pointerId: {0}, action: {1}, x: {2}, y: {3}, numPointers: {4}",
//                new Object[]{pointerId, action, event.getX(), event.getY(), event.getPointerCount()});

        TouchEvent touchEvent;
        switch (action) {
            case MotionEvent.ACTION_HOVER_ENTER:
                jmeX = getJmeX(event.getX(pointerIndex));
                jmeY = invertY(getJmeY(event.getY(pointerIndex)));
                touchEvent = getFreeTouchEvent();
                touchEvent.set(TouchEvent.Type.HOVER_START, jmeX, jmeY, 0, 0);
                touchEvent.setPointerId(pointerId);
                touchEvent.setTime(event.getEventTime());
                touchEvent.setPressure(event.getPressure(pointerIndex));

                lastPos = new Vector2f(jmeX, jmeY);
                lastHoverPositions.put(pointerId, lastPos);

                addEvent(touchEvent);
                consumed = true;
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                // Convert all pointers into events
                for (int p = 0; p < event.getPointerCount(); p++) {
                    jmeX = getJmeX(event.getX(p));
                    jmeY = invertY(getJmeY(event.getY(p)));
                    lastPos = lastHoverPositions.get(event.getPointerId(p));
                    if (lastPos == null) {
                        lastPos = new Vector2f(jmeX, jmeY);
                        lastHoverPositions.put(event.getPointerId(p), lastPos);
                    }

                    float dX = jmeX - lastPos.x;
                    float dY = jmeY - lastPos.y;
                    if (dX != 0 || dY != 0) {
                        touchEvent = getFreeTouchEvent();
                        touchEvent.set(TouchEvent.Type.HOVER_MOVE, jmeX, jmeY, dX, dY);
                        touchEvent.setPointerId(event.getPointerId(p));
                        touchEvent.setTime(event.getEventTime());
                        touchEvent.setPressure(event.getPressure(p));
                        lastPos.set(jmeX, jmeY);

                        addEvent(touchEvent);

                    }
                }
                consumed = true;
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                jmeX = getJmeX(event.getX(pointerIndex));
                jmeY = invertY(getJmeY(event.getY(pointerIndex)));
                touchEvent = getFreeTouchEvent();
                touchEvent.set(TouchEvent.Type.HOVER_END, jmeX, jmeY, 0, 0);
                touchEvent.setPointerId(pointerId);
                touchEvent.setTime(event.getEventTime());
                touchEvent.setPressure(event.getPressure(pointerIndex));
                lastHoverPositions.remove(pointerId);

                addEvent(touchEvent);
                consumed = true;
                break;
            default:
                consumed = false;
                break;
        }

        return consumed;

    }

}
