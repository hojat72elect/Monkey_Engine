
package com.jme3.view.surfaceview;

import android.view.View;
import com.jme3.app.LegacyApplication;

/**
 * An interface used for invoking an event when the application is started explicitly from {@link JmeSurfaceView#startRenderer(int)}.
 * NB : This listener must be utilized before using {@link JmeSurfaceView#startRenderer(int)}, ie : it would be ignored if you try to use {@link JmeSurfaceView#setOnRendererStarted(OnRendererStarted)} after
 * {@link JmeSurfaceView#startRenderer(int)}.
 *
 * @author pavl_g.
 * @see JmeSurfaceView#setOnRendererStarted(OnRendererStarted)
 */
public interface OnRendererStarted {
    /**
     * Invoked when the game application is started by the {@link LegacyApplication#start()}, the event is dispatched on the
     * holder Activity context thread.
     *
     * @param application the game instance.
     * @param layout      the enclosing layout.
     * @see JmeSurfaceView#startRenderer(int)
     */
    void onRenderStart(LegacyApplication application, View layout);
}
