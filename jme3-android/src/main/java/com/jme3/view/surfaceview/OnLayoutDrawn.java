
package com.jme3.view.surfaceview;

import android.view.View;
import com.jme3.app.LegacyApplication;

/**
 * An interface used for dispatching an event when the layout holding the {@link android.opengl.GLSurfaceView} is drawn,
 * the event is dispatched on the user activity context thread.
 *
 * @author pavl_g.
 */
public interface OnLayoutDrawn {
    /**
     * Dispatched when the layout is drawn on the screen.
     *
     * @param legacyApplication the application instance.
     * @param layout            the current layout.
     */
    void onLayoutDrawn(LegacyApplication legacyApplication, View layout);
}
