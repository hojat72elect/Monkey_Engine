
package jme3test.bullet;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;

/**
 * Test case for JME issue #2282: VerifyError while creating
 * PhysicsSpace with AXIS_SWEEP_3 broadphase acceleration.
 *
 * <p>If successful, the application will print "SUCCESS" and terminate without
 * crashing. If unsuccessful, the application will terminate with a VerifyError
 * and no stack trace.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestIssue2282 extends SimpleApplication {

    /**
     * Main entry point for the TestIssue2282 application.
     *
     * @param args array of command-line arguments (unused)
     */
    public static void main(String[] args) {
        TestIssue2282 test = new TestIssue2282();
        test.start();
    }

    /**
     * Initialize the TestIssue2282 application.
     */
    @Override
    public void simpleInitApp() {
        new PhysicsSpace(PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        System.out.println("SUCCESS");
        stop();
    }
}
