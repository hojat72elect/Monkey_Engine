
package com.jme3.scene.control;

import com.jme3.app.AppTask;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

/**
 * Allows for enqueueing tasks onto the update loop / rendering thread.
 * 
 * Usage:
 * mySpatial.addControl(new UpdateControl()); // add it once
 * mySpatial.getControl(UpdateControl.class).enqueue(new Callable() {
 *        public Object call() throws Exception {
 *            // do stuff here
 *            return null;
 *        }
 *    });
 * 
 * @author Brent Owens
 */
public class UpdateControl extends AbstractControl {

    private ConcurrentLinkedQueue<AppTask<?>> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * Enqueues a task/callable object to execute in the jME3
     * rendering thread.
     *
     * @param <V> type of result returned by the Callable
     * @param callable the Callable to run
     * @return a new instance
     */
    public <V> Future<V> enqueue(Callable<V> callable) {
        AppTask<V> task = new AppTask<>(callable);
        taskQueue.add(task);
        return task;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        AppTask<?> task = taskQueue.poll();
        toploop: do {
            if (task == null) break;
            while (task.isCancelled()) {
                task = taskQueue.poll();
                if (task == null) break toploop;
            }
            task.invoke();
        } while (((task = taskQueue.poll()) != null));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    @Override
    public Object jmeClone() {
        UpdateControl clone = (UpdateControl)super.jmeClone();
        clone.taskQueue = new ConcurrentLinkedQueue<>();
        
        // This is kind of questionable since the tasks aren't cloned and have
        // no reference to the new spatial or anything.  They'll get run again
        // but it's not clear to me why that would be desired.  I'm doing it
        // because the old cloneForSpatial() code does.  FIXME?   -pspeed
        clone.taskQueue.addAll(taskQueue);
        return clone;
    }     
}
