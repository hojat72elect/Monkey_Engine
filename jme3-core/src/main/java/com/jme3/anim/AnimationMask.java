
package com.jme3.anim;

/**
 * Created by Nehon
 * An AnimationMask is defining a subset of elements on which an animation will be applied.
 * Most used implementation is the ArmatureMask that defines a subset of joints in an Armature.
 */
public interface AnimationMask {

    /**
     * Test whether the animation should be applied to the specified element.
     *
     * @param target the target element
     * @return true if animation should be applied, otherwise false
     */
    boolean contains(Object target);

}
