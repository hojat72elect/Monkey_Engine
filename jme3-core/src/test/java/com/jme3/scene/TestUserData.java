
package com.jme3.scene;

import junit.framework.TestCase;
import org.junit.Test;

public class TestUserData extends TestCase {

    public static void userDataTest(Spatial sp, Object v) {
        sp.setUserData("test", v);
        assertTrue("UserData is null", sp.getUserData("test") != null);
        assertEquals("UserData value is different than input value", sp.getUserData("test"), v);
        sp.setUserData("test", null);
    }

    @Test
    public void testLong() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Long.MAX_VALUE);
    }

    @Test
    public void testInt() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Integer.MAX_VALUE);
    }

    @Test
    public void testShort() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Short.MAX_VALUE);
    }

    @Test
    public void testByte() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Byte.MAX_VALUE);
    }

    @Test
    public void testDouble() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Double.MAX_VALUE);
    }

    @Test
    public void testFloat() throws Exception {
        Spatial sp = new Node("TestSpatial");
        userDataTest(sp, Math.random() * Float.MAX_VALUE);
    }
}
