

package jme3test.app;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests the functionality of LegacyApplication.restart().
 * <p>
 * If successful, the test will wait for 3 seconds, change to fullscreen, wait 3
 * more seconds, change to a 500x400 window, wait 3 more seconds, and terminate.
 * <p>
 * If successful, the reshape() method will be logged twice: once for the
 * transition to fullscreen mode and again for the transition to windowed mode.
 */
public class TestContextRestart {

    final private static Logger logger
            = Logger.getLogger(TestContextRestart.class.getName());

    public static void main(String[] args) throws InterruptedException{
        logger.setLevel(Level.INFO);
        AppSettings settings = new AppSettings(true);

        final LegacyApplication app = new LegacyApplication() {
            @Override
            public void reshape(int width, int height) {
                super.reshape(width, height);
                logger.log(Level.INFO, "reshape(width={0} height={1})",
                        new Object[]{width, height});
            }
        };
        app.setSettings(settings);
        app.start();

        Thread.sleep(3000);
        /*
         * Restart with a fullscreen graphics context.
         */
        settings.setFullscreen(true);
        settings.setResolution(-1, -1);
        app.setSettings(settings);
        app.restart();

        Thread.sleep(3000);
        /*
         * Restart with a 500x400 windowed context.
         */
        settings.setFullscreen(false);
        settings.setResolution(500, 400);
        app.setSettings(settings);
        app.restart();

        Thread.sleep(3000);
        app.stop();
    }

}
