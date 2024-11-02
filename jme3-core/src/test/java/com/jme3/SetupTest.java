
package com.jme3;

import org.junit.Test;

/**
 *
 * @author davidB
 */
public class SetupTest {
    
   @Test(expected=AssertionError.class)
   public void testAssertionEnabled() {
       assert false;
   }
}
