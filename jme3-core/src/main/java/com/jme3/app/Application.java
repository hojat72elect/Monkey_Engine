
package com.jme3.app;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;
import com.jme3.input.InputManager;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The <code>Application</code> interface represents the minimum exposed
 * capabilities of a concrete jME3 application.
 */
public interface Application {

    /**
     * Determine the application's behavior when unfocused.
     *
     * @return The lost focus behavior of the application.
     */
    public LostFocusBehavior getLostFocusBehavior();

    /**
     * Changes the application's behavior when unfocused.
     *
     * By default, the application will
     * {@link LostFocusBehavior#ThrottleOnLostFocus throttle the update loop}
     * so as not to use 100% of the CPU when it is out of focus, e.g.
     * alt-tabbed, minimized, or obstructed by another window.
     *
     * @param lostFocusBehavior The new lost focus behavior to use.
     *
     * @see LostFocusBehavior
     */
    public void setLostFocusBehavior(LostFocusBehavior lostFocusBehavior);

    /**
     * Returns true if pause on lost focus is enabled, false otherwise.
     *
     * @return true if pause on lost focus is enabled
     *
     * @see #getLostFocusBehavior()
     */
    public boolean isPauseOnLostFocus();

    /**
     * Enables or disables pause on lost focus.
     * <p>
     * By default, pause on lost focus is enabled.
     * If enabled, the application will stop updating
     * when it loses focus or becomes inactive (e.g. alt-tab).
     * For online or real-time applications, this might not be preferable,
     * so this feature should be disabled. For other applications,
     * it is best to keep it enabled so that CPU is not used
     * unnecessarily.
     *
     * @param pauseOnLostFocus True to enable pause on lost focus, false
     * otherwise.
     *
     * @see #setLostFocusBehavior(com.jme3.app.LostFocusBehavior)
     */
    public void setPauseOnLostFocus(boolean pauseOnLostFocus);

    /**
     * Set the display settings to define the display created.
     * <p>
     * Examples of display parameters include display pixel width and height,
     * color bit depth, z-buffer bits, anti-aliasing samples, and update frequency.
     * If this method is called while the application is already running, then
     * {@link #restart() } must be called to apply the settings to the display.
     *
     * @param settings The settings to set.
     */
    public void setSettings(AppSettings settings);

    /**
     * Sets the Timer implementation that will be used for calculating
     * frame times.  By default, Application will use the Timer as returned
     * by the current JmeContext implementation.
     * 
     * @param timer the desired timer (alias created)
     */
    public void setTimer(Timer timer);

    public Timer getTimer();

    /**
     * @return The {@link AssetManager asset manager} for this application.
     */
    public AssetManager getAssetManager();

    /**
     * @return the {@link InputManager input manager}.
     */
    public InputManager getInputManager();

    /**
     * @return the {@link AppStateManager app state manager}
     */
    public AppStateManager getStateManager();

    /**
     * @return the {@link RenderManager render manager}
     */
    public RenderManager getRenderManager();

    /**
     * @return The {@link Renderer renderer} for the application
     */
    public Renderer getRenderer();

    /**
     * @return The {@link AudioRenderer audio renderer} for the application
     */
    public AudioRenderer getAudioRenderer();

    /**
     * @return The {@link Listener listener} object for audio
     */
    public Listener getListener();

    /**
     * @return The {@link JmeContext display context} for the application
     */
    public JmeContext getContext();

    /**
     * @return The main {@link Camera camera} for the application
     */
    public Camera getCamera();

    /**
     * Starts the application.
     * A bug occurring when using LWJGL3 prevents this method from
     * returning until after the application is stopped on macOS.
     */
    public void start();

    /**
     * Starts the application.
     * A bug occurring when using LWJGL3 prevents this method from
     * returning until after the application is stopped on macOS.
     *
     * @param waitFor true&rarr;wait for the context to be initialized,
     * false&rarr;don't wait
     */
    public void start(boolean waitFor);

    /**
     * Sets an AppProfiler hook that will be called back for
     * specific steps within a single update frame.  Value defaults
     * to null.
     * 
     * @param prof the profiler to use (alias created) or null for none
     * (default=null)
     */
    public void setAppProfiler(AppProfiler prof);

    /**
     * Returns the current AppProfiler hook, or null if none is set.
     *
     * @return the pre-existing instance, or null if none
     */
    public AppProfiler getAppProfiler();

    /**
     * Restarts the context, applying any changed settings.
     * <p>
     * Changes to the {@link AppSettings} of this Application are not
     * applied immediately; calling this method forces the context
     * to restart, applying the new settings.
     */
    public void restart();

    /**
     * Requests the context to close, shutting down the main loop
     * and making necessary cleanup operations.
     *
     * Same as calling stop(false)
     *
     * @see #stop(boolean)
     */
    public void stop();

    /**
     * Requests the context to close, shutting down the main loop
     * and making necessary cleanup operations.
     * After the application has stopped, it cannot be used anymore.
     * 
     @param waitFor true&rarr;wait for the context to be fully destroyed,
     * false&rarr;don't wait
     */
    public void stop(boolean waitFor);

    /**
     * Enqueues a task/callable object to execute in the jME3
     * rendering thread.
     * <p>
     * Callables are executed right at the beginning of the main loop.
     * They are executed even if the application is currently paused
     * or out of focus.
     *
     * @param <V> type of result returned by the Callable
     * @param callable The callable to run in the main jME3 thread
     * @return a new instance
     */
    public <V> Future<V> enqueue(Callable<V> callable);

    /**
     * Enqueues a runnable object to execute in the jME3
     * rendering thread.
     * <p>
     * Runnables are executed right at the beginning of the main loop.
     * They are executed even if the application is currently paused
     * or out of focus.
     *
     * @param runnable The runnable to run in the main jME3 thread
     */
    public void enqueue(Runnable runnable);

    /**
     * @return The GUI viewport. Which is used for the on screen
     * statistics and FPS.
     */
    public ViewPort getGuiViewPort();

    public ViewPort getViewPort();
}
