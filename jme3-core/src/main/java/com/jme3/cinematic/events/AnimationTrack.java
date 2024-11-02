
package com.jme3.cinematic.events;

import com.jme3.animation.LoopMode;
import com.jme3.scene.Spatial;

/**
 * @deprecated use AnimationEvent instead
 * @author Nehon
 */
@Deprecated
public class AnimationTrack extends AnimationEvent {

    public AnimationTrack() {
        super();
    }

    public AnimationTrack(Spatial model, String animationName) {
        super(model, animationName);
    }

    public AnimationTrack(Spatial model, String animationName, float initialDuration) {
        super(model, animationName, initialDuration);
    }

    public AnimationTrack(Spatial model, String animationName, LoopMode loopMode) {
        super(model, animationName, loopMode);

    }

    public AnimationTrack(Spatial model, String animationName, float initialDuration, LoopMode loopMode) {
        super(model, animationName, initialDuration, loopMode);
    }
}
