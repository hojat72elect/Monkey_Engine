
package jme3test.model.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.app.*;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.Limits;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.scene.plugins.gltf.GltfModelKey;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class TestGltfMorph extends SimpleApplication {
    private Node probeNode;
    private float t = -1;
    private int n = 0;

    public static void main(String[] args) {
        TestGltfMorph app = new TestGltfMorph();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        probeNode = (Node) assetManager.loadModel("Scenes/defaultProbe.j3o");
        rootNode.attachChild(probeNode);

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.1f, 100f);
        renderer.setDefaultAnisotropicFilter(Math.min(renderer.getLimits().get(Limits.TextureAnisotropy), 8));
        setPauseOnLostFocus(false);

        flyCam.setMoveSpeed(5);
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(true);
        viewPort.setBackgroundColor(new ColorRGBA().setAsSrgb(0.2f, 0.2f, 0.2f, 1.0f));

        setupFloor();

        Vector3f lightDir = new Vector3f(-1, -1, .5f).normalizeLocal();

        // To make shadows, sun
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(lightDir);
        dl.setColor(ColorRGBA.White);
        rootNode.addLight(dl);

        // Add ambient light
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.multLocal(0.4f));
        rootNode.addLight(al);

        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(getAssetManager(), SHADOWMAP_SIZE, 3);
        dlsr.setLight(dl);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        getViewPort().addProcessor(dlsr);

        loadModel("jme3test/morph/MorphStressTest.glb", new Vector3f(0, -1, 0), 1);
    }

    private void setupFloor() {
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMaterial.setColor("Diffuse", new ColorRGBA(.9f, .9f, .9f, .9f));

        Node floorGeom = new Node("floorGeom");
        Quad q = new Quad(20, 20);
        Geometry g = new Geometry("geom", q);
        g.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        g.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorGeom.attachChild(g);

        floorGeom.setMaterial(floorMaterial);

        floorGeom.move(-10f, -2f, 10f);

        rootNode.attachChild(floorGeom);
    }

    private void loadModel(String path, Vector3f offset, float scale) {
        GltfModelKey k = new GltfModelKey(path);
        Spatial s = assetManager.loadModel(k);
        s.scale(scale);
        s.move(offset);
        probeNode.attachChild(s);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        if (t == -1 || t > 5) {
            rootNode.depthFirstTraversal(sx -> {
                AnimComposer composer = sx.getControl(AnimComposer.class);
                if (composer != null) {
                    String anims[] = composer.getAnimClipsNames().toArray(new String[0]);
                    String anim = anims[n++ % anims.length];
                    System.out.println("Play " + anim);
                    composer.setCurrentAction(anim);
                }
            });
            t = 0;
        } else {
            t += tpf;
        }
    }

}
