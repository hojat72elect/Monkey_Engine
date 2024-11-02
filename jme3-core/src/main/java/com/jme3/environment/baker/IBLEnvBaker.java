

package com.jme3.environment.baker;

import com.jme3.texture.Texture2D;
import com.jme3.texture.TextureCubeMap;

/**
 * An environment baker, but this one is for Imaged Base Lighting.
 *
 * @author Riccardo Balbo
 */
public interface IBLEnvBaker extends EnvBaker {
    /**
     * Generates the BRDF texture.
     * 
     * @return The BRDF texture
     */
    public Texture2D genBRTF();

    /**
     * Bakes the irradiance map.
     */
    public void bakeIrradiance();

    /**
     * Bakes the specular IBL map.
     */
    public void bakeSpecularIBL();

    /**
     * Gets the specular IBL map.
     * 
     * @return The specular IBL map
     */
    public TextureCubeMap getSpecularIBL();

    /**
     * Gets the irradiance map.
     * 
     * @return The irradiance map
     */
    public TextureCubeMap getIrradiance();
}
