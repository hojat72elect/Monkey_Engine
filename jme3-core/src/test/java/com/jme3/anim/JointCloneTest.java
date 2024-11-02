
package com.jme3.anim;

import com.jme3.util.clone.Cloner;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cloning a Joint.
 *
 * @author Stephen Gold
 */
public class JointCloneTest {

    /**
     * Make sure the initial transform gets cloned. This was issue 1469 at
     * GitHub.
     */
    @Test
    public void testInitialTransform() {
        Joint testJoint = new Joint("testJoint");
        Assert.assertTrue(testJoint.getInitialTransform().isIdentity());

        Joint clone = Cloner.deepClone(testJoint);
        clone.getInitialTransform().setScale(2f);

        Assert.assertTrue(testJoint.getInitialTransform().isIdentity());
    }
}
