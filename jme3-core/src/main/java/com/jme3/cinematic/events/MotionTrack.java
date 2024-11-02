
package com.jme3.cinematic.events;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.scene.Spatial;

/** 
 *
 * @author Nehon
 * @deprecated use MotionEvent instead
 */
@Deprecated
public class MotionTrack extends MotionEvent {

      /**
     * Create MotionTrack,
     * when using this constructor don't forget to assign spatial and path
     */
    public MotionTrack() {
        super();
    }

    /**
     * Creates a MotionPath for the given spatial on the given motion path
     *
     * @param spatial the Spatial to move (not null)
     * @param path the path to be taken (alias created)
     */
    public MotionTrack(Spatial spatial, MotionPath path) {
        super(spatial, path);
    }

    /**
     * Creates a MotionPath for the given spatial on the given motion path
     *
     * @param spatial the Spatial to move (not null)
     * @param path the path to be taken (alias created)
     * @param initialDuration the desired duration (in seconds, default=10)
     */
    public MotionTrack(Spatial spatial, MotionPath path, float initialDuration) {
        super(spatial, path, initialDuration);
    }

    /**
     * Creates a MotionPath for the given spatial on the given motion path
     *
     * @param spatial the Spatial to move (not null)
     * @param path the path to be taken (alias created)
     * @param loopMode (default=DontLoop)
     */
    public MotionTrack(Spatial spatial, MotionPath path, LoopMode loopMode) {
        super(spatial, path, loopMode);
        
    }

    /**
     * Creates a MotionPath for the given spatial on the given motion path
     *
     * @param spatial the Spatial to move (not null)
     * @param path the path to be taken (alias created)
     * @param initialDuration the desired duration (in seconds, default=10)
     * @param loopMode (default=DontLoop)
     */
    public MotionTrack(Spatial spatial, MotionPath path, float initialDuration, LoopMode loopMode) {
        super(spatial, path, initialDuration, loopMode);
    }
}
