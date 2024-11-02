
package com.jme3.cinematic;

import com.jme3.cinematic.events.MotionEvent;

/**
 * Trigger the events happening on a motion path
 * @author Nehon
 */
public interface MotionPathListener {

    /**
     * Triggers every time the target reach a waypoint on the path
     * @param motionControl the MotionEvent objects that reached the waypoint
     * @param wayPointIndex the index of the way point reached
     */
    public void onWayPointReach(MotionEvent motionControl,int wayPointIndex);

}
