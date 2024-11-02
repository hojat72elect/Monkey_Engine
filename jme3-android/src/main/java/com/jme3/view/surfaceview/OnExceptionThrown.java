
package com.jme3.view.surfaceview;

/**
 * An interface designed to listen for exceptions and fire an event when an exception is thrown.
 *
 * @author pavl_g.
 * @see JmeSurfaceView#setOnExceptionThrown(OnExceptionThrown)
 */
public interface OnExceptionThrown {
    /**
     * Listens for a thrown exception or a thrown error.
     *
     * @param e the exception or the error that is throwable.
     */
    void onExceptionThrown(Throwable e);
}
