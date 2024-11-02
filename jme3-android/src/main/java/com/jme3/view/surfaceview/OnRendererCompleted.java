
package com.jme3.view.surfaceview;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;

/**
 * An interface used for invoking an event when the user delay finishes, on the first update of the game.
 *
 * @author pavl_g.
 * @see JmeSurfaceView#setOnRendererCompleted(OnRendererCompleted)
 */
public interface OnRendererCompleted {
    /**
     * Invoked when the user delay finishes, on the first update of the game, the event is dispatched on the
     * enclosing Activity context thread.
     *
     * @param application the current jme game instance.
     * @param appSettings the current window settings of the running jme game.
     * @see JmeSurfaceView#update()
     */
    void onRenderCompletion(LegacyApplication application, AppSettings appSettings);
}
