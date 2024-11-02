
package com.jme3.cinematic;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.scene.Node;
import org.junit.Test;

/**
 *
 * @author davidB
 */
public class CinematicTest {
    
    /**
     * No NPE or any exception when clear() a new Cinematic
     */
    @Test
    public void clearEmpty() {
        Cinematic sut = new Cinematic();
        sut.clear();
    }
    
    /**
     * No ClassCastException when clear() a Cinematic with AnimationEvent
     */
    @Test
    public void clearAnimationEvent() {
        Cinematic sut = new Cinematic();
        Node model = new Node("model");
        AnimControl ac = new AnimControl();
        ac.addAnim(new Animation("animName", 1.0f));
        model.addControl(ac);
        sut.enqueueCinematicEvent(new AnimationEvent(model, "animName"));
        sut.initialize(null, null);
        sut.clear();
    }
}
