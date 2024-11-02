
package jme3test.light.pbr;

import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.light.LightProbe;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic logger for environment map rendering progress.
 * @author nehon
 */
public class ConsoleProgressReporter extends JobProgressAdapter<LightProbe>{

    private static final Logger logger = Logger.getLogger(ConsoleProgressReporter.class.getName());
    
    private long time;

    @Override
    public void start() {
        time = System.currentTimeMillis();
        logger.log(Level.INFO,"Starting generation");
    }

    @Override
    public void progress(double value) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Progress : {0}%", (value * 100));
        }
    }

    @Override
    public void step(String message) {       
        logger.info(message);
    }
    
    @Override
    public void done(LightProbe result) {
        long end = System.currentTimeMillis();
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Generation done in {0}", (end - time) / 1000f);
        }
    }
    
}
