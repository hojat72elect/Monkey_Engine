
package com.jme3.animation;

/**
 * <code>LoopMode</code> determines how animations repeat, or if they
 * do not repeat.
 */
@Deprecated
public enum LoopMode {
    /**
     * The animation will play repeatedly, when it reaches the end
     * the animation will play again from the beginning, and so on.
     */
    Loop,

    /**
     * The animation will not loop. It will play until the last frame, and then
     * freeze at that frame. It is possible to decide to play a new animation
     * when that happens by using an AnimEventListener.
     */
    DontLoop,

    /**
     * The animation will cycle back and forth. When reaching the end, the
     * animation will play backwards from the last frame until it reaches
     * the first frame.
     */
    Cycle,

}
