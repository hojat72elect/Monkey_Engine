
package com.jme3.animation;

import com.jme3.export.Savable;
import com.jme3.util.TempVars;

@Deprecated
public interface Track extends Savable, Cloneable {

    /**
     * Sets the time of the animation.
     * 
     * Internally, the track will retrieve objects from the control
     * and modify them according to the properties of the channel and the
     * given parameters.
     * 
     * @param time The time in the animation
     * @param weight The weight from 0 to 1 on how much to apply the track
     * @param control The control which the track should affect
     * @param channel The channel which the track should affect
     * @param vars temporary storage
     */
    public void setTime(float time, float weight, AnimControl control, AnimChannel channel, TempVars vars);

    /**
     * @return the length of the track
     */
    public float getLength();

    /**
     * This method creates a clone of the current object.
     * @return a clone of the current object
     */
    public Track clone();
    
    /**
     * Get the times in seconds for all keyframes.
     * 
     * All keyframe times should be between 0.0 and {@link #getLength() length}.
     * Modifying the provided array is not allowed, as it may corrupt internal
     * state.
     * 
     * @return the keyframe times
     */
    public float[] getKeyFrameTimes();
}
