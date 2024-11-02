
package com.jme3.effect;

import com.jme3.math.Matrix3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;

/**
 * The <code>ParticleMesh</code> is the underlying visual implementation of a 
 * {@link ParticleEmitter particle emitter}.
 * 
 * @author Kirill Vainer
 */
public abstract class ParticleMesh extends Mesh {

    /**
     * Type of particle mesh
     */
    public enum Type {
        /**
         * The particle mesh is composed of points. Each particle is a point.
         * Note that point based particles do not support certain features such
         * as {@link ParticleEmitter#setRotateSpeed(float) rotation}, and
         * {@link ParticleEmitter#setFacingVelocity(boolean) velocity following}.
         */
        Point,
        
        /**
         * The particle mesh is composed of triangles. Each particle is 
         * two triangles making a single quad.
         */
        Triangle;
    }

    /**
     * Initialize mesh data.
     * 
     * @param emitter The emitter which will use this <code>ParticleMesh</code>.
     * @param numParticles The maximum number of particles to simulate
     */
    public abstract void initParticleData(ParticleEmitter emitter, int numParticles);
    
    /**
     * Set the images on the X and Y coordinates
     * @param imagesX Images on the X coordinate
     * @param imagesY Images on the Y coordinate
     */
    public abstract void setImagesXY(int imagesX, int imagesY);
    
    /**
     * Update the particle visual data. Typically called every frame.
     *
     * @param particles the particles to update
     * @param cam the camera to use for billboarding
     * @param inverseRotation the inverse rotation matrix
     */
    public abstract void updateParticleData(Particle[] particles, Camera cam, Matrix3f inverseRotation);

}
