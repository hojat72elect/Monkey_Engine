

package com.jme3.environment.baker;

import java.util.function.Predicate;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.TextureCubeMap;

/**
 * An environment baker to bake a 3d environment into a cubemap
 *
 * @author Riccardo Balbo
 */
public interface EnvBaker {
    /**
     * Bakes the environment.
     * 
     * @param scene
     *            The scene to bake
     * @param position
     *            The position of the camera
     * @param frustumNear
     *            The near frustum
     * @param frustumFar
     *            The far frustum
     * @param filter
     *            A filter to select which geometries to bake
     */
    public void bakeEnvironment(Spatial scene, Vector3f position, float frustumNear, float frustumFar, Predicate<Geometry> filter);

    /**
     * Gets the environment map.
     * 
     * @return The environment map
     */
    public TextureCubeMap getEnvMap();

    /**
     * Cleans the environment baker This method should be called when the baker
     * is no longer needed It will clean up all the resources.
     */
    public void clean();

    /**
     * Specifies whether textures should be pulled from the GPU.
     * 
     * @param v
     */
    public void setTexturePulling(boolean v);

    /**
     * Gets if textures should be pulled from the GPU.
     * 
     * @return
     */
    public boolean isTexturePulling();
}
