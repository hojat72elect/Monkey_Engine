
package com.jme3.environment.generation;

import com.jme3.app.Application;
import com.jme3.environment.util.EnvMapUtils;
import com.jme3.light.LightProbe;
import com.jme3.math.Vector3f;
import com.jme3.texture.TextureCubeMap;
import java.util.concurrent.Callable;


/**
 * Generates the Irradiance map for PBR. This job can be launched from a separate
 * thread.
 * <p>
 * This is not used as we use spherical harmonics directly now, but we may need this code again at some point
 *
 * @author Nehon
 */
public class IrradianceSphericalHarmonicsGenerator extends RunnableWithProgress {

    private TextureCubeMap sourceMap;
    private LightProbe store;
    private final Application app;

    /**
     * Creates an Irradiance map generator. The app is needed to enqueue the
     * call to the EnvironmentCamera when the generation is done, so that this
     * process is thread safe.
     *
     * @param app      the Application
     * @param listener to monitor progress (alias created)
     */
    public IrradianceSphericalHarmonicsGenerator(Application app, JobProgressListener<Integer> listener) {
        super(listener);
        this.app = app;
    }

    /**
     * Fills all the generation parameters
     *
     * @param sourceMap the source cube map
     *                  {@link com.jme3.environment.util.EnvMapUtils.FixSeamsMethod}
     * @param store     The cube map to store the result in.
     */
    public void setGenerationParam(TextureCubeMap sourceMap, LightProbe store) {
        this.sourceMap = sourceMap;

        this.store = store;
        reset();
    }

    @Override
    public void run() {
        app.enqueue(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                listener.start();
                return null;
            }
        });
        try {
            Vector3f[] shCoefficients = EnvMapUtils.getSphericalHarmonicsCoefficents(sourceMap);
            EnvMapUtils.prepareShCoefs(shCoefficients);
            store.setShCoeffs(shCoefficients);

        } catch (Exception e) {
            e.printStackTrace();
        }
        app.enqueue(new Callable<Void>() {

            @Override
            @SuppressWarnings("unchecked")
            public Void call() throws Exception {
                listener.done(6);
                return null;
            }
        });
    }

}
