
package com.jme3.anim;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test constructors and modification methods of the ArmatureMask class.
 */
public class ArmatureMaskTest {

    final private Joint j0 = createJoint("j0", 0);         // leaf
    final private Joint j1 = createJoint("j1", 1);         // leaf
    final private Joint j2 = createJoint("j2", 2, j0, j1);
    final private Joint j3 = createJoint("j3", 3, j2);     // root
    final private Joint j4 = createJoint("j4", 4);         // leaf
    final private Joint j5 = createJoint("j5", 5, j4);     // root
    final private Joint j6 = createJoint("j6", 6);         // root and leaf
    final private Joint[] jointList = {j0, j1, j2, j3, j4, j5, j6};
    final private Armature arm = new Armature(jointList);

    private Joint createJoint(String name, int id, Joint... children) {
        Joint result = new Joint(name);
        result.setId(id);
        for (Joint child : children) {
            result.addChild(child);
        }
        return result;
    }

    /**
     * Test various ways to instantiate a mask that affects all joints.
     */
    @Test
    public void testMaskAll() {
        ArmatureMask[] maskArray = new ArmatureMask[5];
        maskArray[0] = new ArmatureMask(arm);
        maskArray[1] = ArmatureMask.createMask(arm,
                "j0", "j1", "j2", "j3", "j4", "j5", "j6");

        maskArray[2] = ArmatureMask.createMask(arm, "j3");
        maskArray[2].addFromJoint(arm, "j5");
        maskArray[2].addFromJoint(arm, "j6");

        maskArray[3] = ArmatureMask.createMask(arm, "j3")
                .addAncestors(j4)
                .addAncestors(j6);

        maskArray[4] = ArmatureMask.createMask(arm, "j3");
        maskArray[4].addBones(arm, "j4", "j5", "j6");

        for (ArmatureMask testMask : maskArray) {
            for (Joint testJoint : jointList) {
                Assert.assertTrue(testMask.contains(testJoint));
            }
        }
    }

    /**
     * Instantiate masks that affect no joints.
     */
    @Test
    public void testMaskNone() {
        ArmatureMask[] maskArray = new ArmatureMask[4];
        maskArray[0] = new ArmatureMask();
        maskArray[1] = ArmatureMask.createMask(arm);

        maskArray[2] = ArmatureMask.createMask(arm, "j2")
                .removeAncestors(j0)
                .removeAncestors(j1);

        maskArray[3] = ArmatureMask.createMask(arm, "j0", "j1")
                .removeJoints(arm, "j0", "j1");

        for (ArmatureMask testMask : maskArray) {
            for (Joint testJoint : jointList) {
                Assert.assertFalse(testMask.contains(testJoint));
            }
        }
    }

    /**
     * Instantiate masks that affect only j1 and j2.
     */
    @Test
    public void testMask12() {
        ArmatureMask[] maskArray = new ArmatureMask[4];
        maskArray[0] = new ArmatureMask();
        maskArray[0].addBones(arm, "j1", "j2");

        maskArray[1] = ArmatureMask.createMask(arm, "j3")
                .removeJoints(arm, "j0", "j3");

        maskArray[2] = new ArmatureMask()
                .addAncestors(j1)
                .removeAncestors(j3);

        ArmatureMask mask0 = ArmatureMask.createMask(arm, "j0");
        maskArray[3] = ArmatureMask.createMask(arm, "j2")
                .remove(mask0);

        for (ArmatureMask testMask : maskArray) {
            for (Joint testJoint : jointList) {
                if (testJoint == j1 || testJoint == j2) {
                    Assert.assertTrue(testMask.contains(testJoint));
                } else {
                    Assert.assertFalse(testMask.contains(testJoint));
                }
            }
        }
    }
}
