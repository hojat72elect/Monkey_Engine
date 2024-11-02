
package jme3test.bullet;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;

/**
 * Test case for JME issue #883: extra physicsTicks in ThreadingType.PARALLEL.
 *
 * <p>If successful, physics time and frame time will advance at the same rate.
 */
public class TestIssue883 extends SimpleApplication {

    private boolean firstPrint = true;
    private float timeToNextPrint = 1f; // in seconds
    private double frameTime; // in seconds
    private double physicsTime; // in seconds

    public static void main(String[] args) {
        TestIssue883 app = new TestIssue883();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        BulletAppState bulletAppState = new BulletAppState() {
            @Override
            public void physicsTick(PhysicsSpace space, float timeStep) {
                physicsTime += timeStep;
            }
        };
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
    }

    @Override
    public void simpleUpdate(float tpf) {
        frameTime += tpf;

        if (timeToNextPrint > 0f) {
            timeToNextPrint -= tpf;
            return;
        }

        if (firstPrint) { // synchronize
            frameTime = 0.;
            physicsTime = 0.;
            firstPrint = false;
        }

        System.out.printf(" frameTime= %s   physicsTime= %s%n",
                frameTime, physicsTime);
        timeToNextPrint = 1f;
    }
}
