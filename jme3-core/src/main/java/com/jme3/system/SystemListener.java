
package com.jme3.system;

/**
 * The {@code SystemListener} provides a means for an application
 * to receive events relating to a context.
 */
public interface SystemListener {

    /**
     * Callback to indicate the application to initialize. This method
     * is called in the GL/Rendering thread so any GL-dependent resources
     * can be initialized.
     */
    public void initialize();

    /**
     * Called to notify the application that the resolution has changed.
     * @param width the new width of the display (in pixels, &ge;0)
     * @param height the new height of the display (in pixels, &ge;0)
     */
    public void reshape(int width, int height);

    /**
     * Called to notify the application that the scale has changed.
     * @param x the new horizontal scale of the display 
     * @param y the new vertical scale of the display
     */
    public default void rescale(float x, float y){

    }


    /**
     * Callback to update the application state, and render the scene
     * to the back buffer.
     */
    public void update();

    /**
     * Called when the user requests to close the application. This
     * could happen when he clicks the X button on the window, presses
     * the Alt-F4 combination, attempts to shut down the process from
     * the task manager, or presses ESC. 
     * @param esc If true, the user pressed ESC to close the application.
     */
    public void requestClose(boolean esc);

    /**
     * Called when the application gained focus. The display
     * implementation is not allowed to call this method before
     * initialize() has been called or after destroy() has been called.
     */
    public void gainFocus();

    /**
     * Called when the application lost focus. The display
     * implementation is not allowed to call this method before
     * initialize() has been called or after destroy() has been called.
     */
    public void loseFocus();

    /**
     * Called when an error has occurred. This is typically
     * invoked when an uncaught exception is thrown in the render thread.
     * @param errorMsg The error message, if any, or null.
     * @param t Throwable object, or null.
     */
    public void handleError(String errorMsg, Throwable t);

    /**
     * Callback to indicate that the context has been destroyed (either
     * by the user or requested by the application itself). Typically
     * cleanup of native resources should happen here. This method is called
     * in the GL/Rendering thread.
     */
    public void destroy();
}
