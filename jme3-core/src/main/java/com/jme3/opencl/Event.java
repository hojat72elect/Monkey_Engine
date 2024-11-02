
package com.jme3.opencl;

/**
 * Wrapper for an OpenCL Event object.
 * Events are returned from kernel launches and all asynchronous operations.
 * They allow us to test whether an action has completed and block until the operation
 * is done.
 *
 * @author shaman
 */
public abstract class Event extends AbstractOpenCLObject {

    protected Event(ObjectReleaser releaser) {
        super(releaser);
    }

    @Override
    public Event register() {
        super.register();
        return this;
    }

    /**
     * Waits until the action has finished (blocking).
     * This automatically releases the event.
     */
    public abstract void waitForFinished();

    /**
     * Tests if the action is completed.
     * If the action is completed, the event is released.
     *
     * @return {@code true} if the action is completed
     */
    public abstract boolean isCompleted();
}
