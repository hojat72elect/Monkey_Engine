
package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.RectangleMesh;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.util.TangentBinormalGenerator;

public class TestTransparentShadow extends SimpleApplication {

    public static void main(String[] args){
        TestTransparentShadow app = new TestTransparentShadow();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(5.700248f, 6.161693f, 5.1404157f));
        cam.setRotation(new Quaternion(-0.09441641f, 0.8993388f, -0.24089815f, -0.35248178f));

        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        RectangleMesh rm = new RectangleMesh(
                new Vector3f(-10, 0, 10),
                new Vector3f(10, 0, 10),
                new Vector3f(-10, 0, -10)
        );
        rm.scaleTextureCoordinates(Vector2f.UNIT_XY.mult(10));
        TangentBinormalGenerator.generate(rm);

        Geometry geom = new Geometry("floor", rm);
        Material mat = assetManager.loadMaterial("Textures/Terrain/Pond/Pond.j3m");
        geom.setMaterial(mat);
        geom.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(geom);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.7f));
        rootNode.addLight(al);

        DirectionalLight dl1 = new DirectionalLight();
        dl1.setDirection(new Vector3f(0, -1, 0.5f).normalizeLocal());
        dl1.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(dl1);
        
        // create the geometry and attach it
        Spatial tree = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
        tree.setQueueBucket(Bucket.Transparent);
        tree.setShadowMode(ShadowMode.CastAndReceive);

        rootNode.attachChild(tree);

        // Uses Texture from jme3-testdata library!
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setShadowMode(ShadowMode.Cast);
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(0.6f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(1.5f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setLocalTranslation(5.0f, 0, 1.0f);
        fire.setLocalScale(0.3f);
        fire.setQueueBucket(Bucket.Translucent);
        rootNode.attachChild(fire);

        Material mat2 = assetManager.loadMaterial("Common/Materials/RedColor.j3m");

        Geometry ball = new Geometry("sphere", new Sphere(16, 16, 0.5f));
        ball.setMaterial(mat2);
        ball.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(ball);
        ball.setLocalTranslation(-1.0f, 1.5f, 1.0f);

        final DirectionalLightShadowRenderer dlsRenderer = new DirectionalLightShadowRenderer(assetManager, 1024, 1);
        dlsRenderer.setLight(dl1);
        dlsRenderer.setLambda(0.55f);
        dlsRenderer.setShadowIntensity(0.8f);
        dlsRenderer.setShadowCompareMode(CompareMode.Software);
        dlsRenderer.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        dlsRenderer.displayDebug();
        viewPort.addProcessor(dlsRenderer);
        inputManager.addMapping("stabilize", new KeyTrigger(KeyInput.KEY_B));

        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals("stabilize") && isPressed) {
                    dlsRenderer.setEnabledStabilization(!dlsRenderer.isEnabledStabilization()) ;
                }
            }
        }, "stabilize");
   }
}
