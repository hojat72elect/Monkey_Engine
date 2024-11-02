
package com.jme3.environment.generation;

/**
 *
 * Abstract runnable that can report its progress
 *
 * @author Nehon
 */
public abstract class RunnableWithProgress implements Runnable {

    private int progress;
    private int end;
    protected JobProgressListener listener;

    public RunnableWithProgress() {
    }

    public RunnableWithProgress(JobProgressListener listener) {
        this.listener = listener;
    }
    

    /**
     * set the end step value of the process.
     *
     * @param end the desired end value (default=0)
     */
    protected void setEnd(int end) {
        this.end = end;
    }

    /**
     * return the current progress of the process.
     *
     * @return fraction (&ge;0, &le;1)
     */
    public double getProgress() {
        return progress / (double) end;
    }

    /**
     * adds one progression step to the process.
     */
    protected void progress() {
        progress++;
        if (listener != null) {
            listener.progress(getProgress());
        }
    }

    /**
     * resets the progression of the process.
     */
    protected void reset() {
        progress = 0;
    }

}
