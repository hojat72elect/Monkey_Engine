
package com.jme3.effect.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

/**
 * This influencer does not influence particle at all.
 * It makes particles not to move.
 * @author Marcin Roguski (Kaelthas)
 */
public class EmptyParticleInfluencer implements ParticleInfluencer {

    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }

    @Override
    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
    }

    @Override
    public void setInitialVelocity(Vector3f initialVelocity) {
    }

    @Override
    public Vector3f getInitialVelocity() {
        return null;
    }

    @Override
    public void setVelocityVariation(float variation) {
    }

    @Override
    public float getVelocityVariation() {
        return 0;
    }

    @Override
    public EmptyParticleInfluencer clone() {
        try {
            return (EmptyParticleInfluencer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
    }
}
