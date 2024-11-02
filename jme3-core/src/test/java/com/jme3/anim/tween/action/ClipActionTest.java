
package com.jme3.anim.tween.action;

import com.jme3.anim.AnimClip;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test for ClipAction.
 *
 * @author Saichand Chowdary
 */
public class ClipActionTest {

    /**
     * Test to verify setTransitionLength on BlendableAction does not accept negative values.
     */
    @Test
    public void testSetTransitionLength_negativeInput_exceptionThrown() {
        AnimClip animClip = new AnimClip("clip");
        ClipAction clipAction = new ClipAction(animClip);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> clipAction.setTransitionLength(-1));
        assertEquals("transitionLength must be greater than or equal to 0", thrown.getMessage());
    }

    /**
     * Test to verify setTransitionLength on BlendableAction accepts zero.
     */
    @Test
    public void testSetTransitionLength_zeroInput_noExceptionThrown() {
        AnimClip animClip = new AnimClip("clip");
        ClipAction clipAction = new ClipAction(animClip);
        clipAction.setTransitionLength(0);
        assertEquals(0, clipAction.getTransitionLength(), 0);
    }

    /**
     * Test to verify setTransitionLength on BlendableAction accepts positive values.
     */
    @Test
    public void testSetTransitionLength_positiveNumberInput_noExceptionThrown() {
        AnimClip animClip = new AnimClip("clip");
        ClipAction clipAction = new ClipAction(animClip);
        clipAction.setTransitionLength(1.23d);
        assertEquals(1.23d, clipAction.getTransitionLength(), 0);
    }

}
