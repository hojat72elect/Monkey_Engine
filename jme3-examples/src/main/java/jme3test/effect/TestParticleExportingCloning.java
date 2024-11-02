

package jme3test.effect;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;

public class TestParticleExportingCloning extends SimpleApplication {

    public static void main(String[] args){
        TestParticleExportingCloning app = new TestParticleExportingCloning();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        ParticleEmitter emit = new ParticleEmitter("Emitter", Type.Triangle, 200);
        emit.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        emit.setGravity(0, 0, 0);
        emit.setLowLife(5);
        emit.setHighLife(10);
        emit.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        emit.setImagesX(15);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        emit.setMaterial(mat);

        ParticleEmitter emit2 = emit.clone();
        emit2.move(3, 0, 0);
        
        rootNode.attachChild(emit);
        rootNode.attachChild(emit2);
        
        ParticleEmitter emit3 = BinaryExporter.saveAndLoad(assetManager, emit);
        emit3.move(-3, 0, 0);
        rootNode.attachChild(emit3);
    }

}
