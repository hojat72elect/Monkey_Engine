
package com.jme3.animation;

/**
 *
 * @author Nehon
 */
public class AnimationUtils {
    private AnimationUtils() {
    }

    /**
     * Clamps the time according to duration and loopMode
     *
     * @param time the unclamped time value (in seconds)
     * @param duration the animation's duration (in seconds)
     * @param loopMode the animation's looping behavior (not null)
     * @return the clamped time (in seconds)
     */
    public static float clampWrapTime(float time, float duration, LoopMode loopMode) {
        if (time == 0 || duration == 0) {
            return 0; // prevent division by 0 errors
        }
        switch (loopMode) {
            case Cycle:
                boolean sign = ((int) (time / duration) % 2) != 0;
                return sign ? -(duration - (time % duration)) : time % duration;
            case DontLoop:
                return time > duration ? duration : (time < 0 ? 0 : time);
            case Loop:
                return time % duration;
        }
        return time;
    }
}
